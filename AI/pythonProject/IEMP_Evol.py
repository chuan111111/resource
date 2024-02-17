import copy

import networkx as nx
import argparse
import random

G1 = nx.DiGraph()
G2 = nx.DiGraph()
k=0;
seed1 = []
seed2 = []
all_node=[]
mutation_rate = 0.01
crossover_rate = 0.7
def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", type=str)
    parser.add_argument("-i", type=str)
    parser.add_argument("-b", type=str)
    parser.add_argument("-k", type=int)

    args = parser.parse_args()
    global k
    k = args.k
    with open(args.n, 'r') as f:
        line = f.readline()
        overal = line.strip().split(" ")
        edge_number = overal[1]
        for i in range(int(edge_number)):
            line = f.readline()
            edge = line.strip().split(" ")
            G1.add_edge(edge[0], edge[1], value1=float(edge[2]))
            G2.add_edge(edge[0], edge[1],  value2=float(edge[3]))
        Graph1 = dict([(u, []) for u, v, d1 in G1.edges(data=True)])
        Graph2= dict([(u, []) for u, v,  d2 in G2.edges(data=True)])
        for u, v, d1 in G1.edges(data=True):
            Graph1[u].append((v, d1["value1"]))
        for u, v, d2 in G2.edges(data=True):
            Graph2[u].append((v,  d2["value2"]))
        for node in G1:
            if node not in Graph1.keys():
                Graph1[node] = []
            if node not in Graph2.keys():
                Graph2[node]=[]
        global all_node
        all_node = Graph1.keys()
        size=len(all_node)

    with open(args.i, 'r') as f:
        line = f.readline()
        over = line.strip().split(" ")
        seed1_number = over[0]
        seed2_number = over[1]

        for i in range(int(seed1_number)):
            line = f.readline()
            seed1.append(line.strip())
        for i in range(int(seed2_number)):
            line = f.readline()
            seed2.append(line.strip())

    n=5
    population=[]
    new_Grapgh1 = delete(Graph1)
    new_Grapgh2 = delete(Graph2)

    list1_degree = {}
    for key in Graph1:
        if key not in seed1:
            list1_degree[key] = len(new_Grapgh1[key])

    list2_degree = {}
    for key in Graph2:
        if key not in seed2:
            list2_degree[key] = len(new_Grapgh2[key])

    new_node1 = iter(sorted(list1_degree.items(), key=lambda x: x[1], reverse=True))
    new_node2 = iter(sorted(list2_degree.items(), key=lambda x: x[1], reverse=True))

    pointer1 = next(new_node1, None)
    pointer2 = next(new_node2, None)
    #初始解
    for i in range(n):
        lst1 = [0 for _ in range(size*2)]
        count = 0
        while pointer1 or pointer2:
            if pointer1 and (not pointer2 or pointer1[1] >= pointer2[1]):
                lst1[int(pointer1[0])] = 1
                count += 1
                pointer1 = next(new_node1, None)
            else:
                lst1[int(pointer2[0]) + size] = 1
                count += 1
                pointer2 = next(new_node2, None)

            if count == k:
                population.append(lst1)
                break




    generations=50
    for generation in range(generations):
        # 评估种群中每个解的适应度
        fitness_scores = [monte(new_Grapgh1,new_Grapgh2,solution) for solution in population]

        # 二进制锦标赛选择新的种群
        selected_population = binary_tournament_selection(population, n,fitness_scores)

        # 进行交叉和变异生成新解

        for i in range(0, n-1, 2):
            parent1, parent2 = selected_population[i], selected_population[i + 1]
            if random.random() < crossover_rate:
                child1 = crossover(parent1, parent2)
                child2 = crossover(parent2, parent1)
                f = monte(new_Grapgh1, new_Grapgh2, child1)
                d = monte(new_Grapgh1, new_Grapgh2, child2)
            else:
                child1, child2 = parent1[:], parent2[:]

            child1 = mutation(child1, mutation_rate)
            child2 = mutation(child2, mutation_rate)
            f=monte(new_Grapgh1, new_Grapgh2, child1)
            d=monte(new_Grapgh1, new_Grapgh2, child2)
            population.extend([child1, child2])

        # 保留前 N 个适应度最高的解



        # 返回最优解
    population = [x for _, x in sorted(zip(fitness_scores, population), reverse=True)[:1]]
    best_solution = population[0]
    seed1_balanced = []
    seed2_balanced = []
    for i, element in enumerate(best_solution[:len(best_solution) // 2]):
        if element == 1:
            seed1_balanced.append(i)
    for i, element in enumerate(best_solution[len(best_solution) // 2:-1]):
        if element == 1:
            seed2_balanced.append(i)

    with open(args.b, "w") as objective_value_file:
        s = '{} {}'.format(str(len(seed1_balanced)), str(len(seed2_balanced)))
        objective_value_file.write(f"{s}\n")

        for node in seed1_balanced:
            objective_value_file.write(f"{node}\n")
        for nodes in seed2_balanced:
            objective_value_file.write(f"{nodes}\n")
        objective_value_file.close()


def monte(Grapgh1,Grapgh2,solution):
    seed1_balanced = []
    seed2_balanced = []
    for i, element in enumerate(solution[:len(solution) // 2]):
        if element == 1:
            seed1_balanced.append(str(i))
    for i, element in enumerate(solution[len(solution) // 2:-1]):
        if element == 1:
            seed2_balanced.append(str(i))
    if len(seed1_balanced) + len(seed2_balanced) > k:
        return -len(seed1_balanced) - len(seed2_balanced)

    else:
        seeds1=set(seed1).union(set(seed1_balanced))
        seeds2=set(seed2).union(set(seed2_balanced))
        num = 18
        total = 0
        for i in range(num):

            exposed_node1 = evaluators(Grapgh1, seeds1 )
            exposed_node2 = evaluators(Grapgh2, seeds2 )
            all = set(set(exposed_node1).intersection(set(exposed_node2)))
            unex_node1 = set(all_node).difference(set(exposed_node1))
            unex_node2 = set(all_node).difference(set(exposed_node2))
            unex_node = set(unex_node2).intersection(set(unex_node1))

            total += len(set(all).union(unex_node))

        reault = total / num
        return reault
def delete(Graph):
    new_graph=copy.deepcopy(Graph)
    for key in new_graph.keys():
        for node,value in new_graph[key]:
            randoms = random.random()
            if randoms>value:
                new_graph[key].remove((node, value))
    return new_graph


def mutation(solution,mutation_rate):
    mutated_solution = [bit ^ (random.random() < mutation_rate) for bit in solution]
    return mutated_solution

def crossover(parent1,parent2):
    point1 = random.randint(0, len(parent1) - 1)
    point2 = random.randint(point1, len(parent1) - 1)
    child = parent1[:point1] + parent2[point1:point2] + parent1[point2:]
    return child

def binary_tournament_selection(population, n,fitness_scores):
    selected_population = []
    while len(selected_population) < n:

        contestants1,contestants2 = random.sample(range(0,len(population)), 2)
        fit1=fitness_scores[contestants1]
        fit2=fitness_scores[contestants2]
        if fit1>fit2:
            winner=contestants1
        else:winner=contestants2

        selected_population.append(population[winner])
    return selected_population

def evaluators(Graph, seed):

    activated_node = set(seed)
    list1 = set(seed)
    exposed_node = set(seed)
    while len(list1) != 0:
        current = list1.pop()
        for node, value1 in Graph[current]:
            if node not in activated_node:
                randoms = random.random()
                if randoms < value1:
                    activated_node.add(node)
                    list1.add(node)
                exposed_node.add(node)

    return exposed_node


if __name__ == "__main__":
    main()
