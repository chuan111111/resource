import copy

import networkx as nx
import argparse
import random

G1 = nx.DiGraph()
G2 = nx.DiGraph()


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", type=str)
    parser.add_argument("-i", type=str)
    parser.add_argument("-b", type=str)
    parser.add_argument("-k", type=int)

    args = parser.parse_args()
    k=args.k
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
        all_node = Graph1.keys()

    with open(args.i, 'r') as f:
        line = f.readline()
        over = line.strip().split(" ")
        seed1_number = over[0]
        seed2_number = over[1]
        seed1 = []
        seed2 = []
        for i in range(int(seed1_number)):
            line = f.readline()
            seed1.append(line.strip())
        for i in range(int(seed2_number)):
            line = f.readline()
            seed2.append(line.strip())
    seed1_balanced=[]
    seed2_balanced=[]

    while len(seed1_balanced) + len(seed2_balanced) < k:
        new_Grapgh1 = delete(Graph1)
        new_Grapgh2 = delete(Graph2)

        h0 = monte(new_Grapgh1, set(seed1).union(set(seed1_balanced)), new_Grapgh2,
                   set(seed2).union(set(seed2_balanced)), all_node)
        h1_max = 0
        h1_max_node = None
        h2_max = 0
        h2_max_node = None
        node1 = select(new_Grapgh1, set(seed1).union(set(seed1_balanced)))
        node2 = select(new_Grapgh2, set(seed2).union(set(seed2_balanced)))
        for node, value in node1:
            new_seed1_b = seed1_balanced[:]
            new_seed1_b.append(node)

            h1 = monte(new_Grapgh1, set(seed1).union(set(new_seed1_b)), new_Grapgh2, set(seed2).union(seed2_balanced),
                       all_node) - h0
            if h1 > h1_max:
                h1_max = h1
                h1_max_node = node

        for node, value in node2:
            new_seed2_b = seed2_balanced[:]
            new_seed2_b.append(node)

            h2 = monte(new_Grapgh1, set(seed1).union(set(seed1_balanced)), new_Grapgh2,
                       set(seed2).union(set(new_seed2_b)), all_node) - h0
            if h2 > h2_max:
                h2_max = h2
                h2_max_node = node

        if h1_max > h2_max:
            seed1_balanced.append(h1_max_node)
        else:
            seed2_balanced.append(h2_max_node)

    with open(args.b, "w") as objective_value_file:
        s = '{} {}'.format(str(len(seed1_balanced)), str(len(seed2_balanced)))
        objective_value_file.write(f"{s}\n")

        for node in seed1_balanced:
            objective_value_file.write(f"{node}\n")
        for nodes in seed2_balanced:
            objective_value_file.write(f"{nodes}\n")
        objective_value_file.close()

def select(Graph,seed):
    list_degree={}
    for key in Graph :
        if key not in seed:
           list_degree[key]=len(Graph[key])

    new_node = sorted(list_degree.items(), key=lambda x: x[1], reverse=True)
    return new_node[:40]
def monte(Grapgh1,seeds1,Grapgh2,seeds2,all_node):
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
