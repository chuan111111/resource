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
    parser.add_argument("-o", type=str)

    args = parser.parse_args()
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

    with open(args.b, 'r') as f:
        line = f.readline()
        over_balanced = line.strip().split(" ")
        seed1_number_balanced = over_balanced[0]
        seed2_number_balanced = over_balanced[1]
        seed1_balanced = []
        seed2_balanced = []
        for i in range(int(seed1_number_balanced)):
            line = f.readline()
            seed1_balanced.append(line.strip())
        for i in range(int(seed2_number_balanced)):
            line = f.readline()
            seed2_balanced.append(line.strip())

    num = 1500
    total = 0
    for i in range(num):
        exposed_node1 = evaluators(Graph1, seed1, seed1_balanced)
        exposed_node2 = evaluators(Graph2, seed2, seed2_balanced)
        all = set(set(exposed_node1).intersection(set(exposed_node2)))
        unex_node1 = set(all_node).difference(set(exposed_node1))
        unex_node2 = set(all_node).difference(set(exposed_node2))
        unex_node = set(unex_node2).intersection(set(unex_node1))

        total += len(set(all).union(unex_node))

    reault = total / num

    with open(args.o, "w") as objective_value_file:
        objective_value_file.write(f"{reault}\n")


def evaluators(Graph, seed,balance_seed):
    activated_node = set(seed).union(set(balance_seed))
    list1 = set(seed).union(set(balance_seed))
    exposed_node = set(seed).union(set(balance_seed))

    while len(list1) != 0:
        current = list1.pop()
        for node, value1 in Graph[current]:
            if node not in activated_node:
                randoms = random.random()
                node_probability = value1
                if randoms < node_probability:
                    activated_node.add(node)
                    list1.add(node)
                exposed_node.add(node)

    return exposed_node


if __name__ == "__main__":
    main()
