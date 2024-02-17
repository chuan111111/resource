import itertools
import random

import numpy as np
import time

N = 100 # You shoule change the test size here !!!

def my_range(start, end):
    if start <= end:
        return range(start, end + 1)
    else:
        return range(start, end - 1, -1)


class Problem:
    char_mapping = ('·', 'Q')

    def __init__(self, n=4):
        self.n = n

    def is_valid(self, state):
        """
        check the state satisfy condition or not.
        :param state: list of points (in (row id, col id) tuple form)
        :return: bool value of valid or not
        """
        board = self.get_board(state)
        res = True
        for point in state:
            i, j = point
            condition1 = board[:, j].sum() <= 1
            condition2 = board[i, :].sum() <= 1
            condition3 = self.pos_slant_condition(board, point)
            condition4 = self.neg_slant_condition(board, point)
            res = res and condition1 and condition2 and condition3 and condition4
            if not res:
                break
        return res

    def is_satisfy(self, state):
        """
        check if the current board meets the win condition
        """
        return self.is_valid(state) and len(state) == self.n

    def pos_slant_condition(self, board, point):
        i, j = point
        tmp = min(self.n - i - 1, j)
        start = (i + tmp, j - tmp)
        tmp = min(i, self.n - j - 1)
        end = (i - tmp,  j + tmp)
        rows = my_range(start[0], end[0])
        cols = my_range(start[1], end[1])
        return board[rows, cols].sum() <= 1

    def neg_slant_condition(self, board, point):
        i, j = point
        tmp = min(i, j)
        start = (i - tmp, j - tmp)
        tmp = min(self.n - i - 1, self.n - j - 1)
        end = (i + tmp, j + tmp)
        rows = my_range(start[0], end[0])
        cols = my_range(start[1], end[1])
        return board[rows, cols].sum() <= 1

    def get_board(self, state):
        board = np.zeros([self.n, self.n], dtype=int)
        for point in state:
            board[point] = 1
        return board

    def print_state(self, state):
        board = self.get_board(state)
        print('_' * (2 * self.n + 1))
        for row in board:
            for item in row:
                print(f'|{Problem.char_mapping[item]}', end='')
            print('|')
        print('-' * (2 * self.n + 1))

    def count_invalid(self, state, point):
        sum = 0
        for queen in state:
            if queen[0] != point[0]:
               if queen[0] == point[0] or queen[1] == point[1] or queen[0] - point[0] == queen[1] - point[1] or queen[0] - point[0] == point[1] - queen[1]:
                    sum += 1
        return sum


def bts(problem):
    action_stack = []
    permutation = []
    for x in range(0, n):
        permutation.append(x)
    all_permutation = list(itertools.permutations(permutation))
    for a_permutation in all_permutation:
        for x in range(0, n):
            action_stack.append((x, a_permutation[x]))
        if problem.is_satisfy(action_stack):
            yield action_stack
            action_stack = []
        else:
            action_stack = []


def improving_bts(problem):
    def backtrack(state):
        if problem.is_satisfy(state):
            yield state
            return
        row = len(state)
        for col in range(problem.n):
            if problem.is_valid(state + [(row - 1, col)]):
                yield from backtrack(state + [(row - 1, col)])

    action_stack = []
    for x in range(0, problem.n):
        action_stack.append((x, 0))
    for solution in backtrack(action_stack):
        yield solution


def min_conflict(problem):
    action_stack = []
    for x in range(0, n):
        y = random.randint(0, n - 1)
        action_stack.append((x, y))
    while not problem.is_satisfy(action_stack):
        # TODO: Implement min_conflict algorithm logic here
        invalid_point = []
        for y in range(0, n):
            i, j = action_stack[y]
            if problem.count_invalid(action_stack, (i, j)) != 0:
                invalid_point.append((i, j))
        rand = random.randint(0, len(invalid_point) - 1)
        point = invalid_point[rand]
        i, j = point
        initial_count = problem.count_invalid(action_stack, (i, j))
        if initial_count == 0:
            invalid_point.remove((i, j))
            continue
        else:
            min_count, min_index = initial_count, j
            for y in range(0, n):
                # action_stack[i] = (i, y)
                current_count = problem.count_invalid(action_stack, (i, y))
                if current_count < min_count:
                    min_count = current_count
                    min_index = y
                if current_count == min_count:
                    if y != j:
                        min_index = y
            action_stack[i] = (i, min_index)


    yield action_stack


    # test_block
n = N # Do not modify this parameter, if you want to change the size, go to the first line of whole program.
render = False # here to select GUI or not
method = min_conflict  # here to change your method; bts or improving_bts or min_conflict
p = Problem(n)
if render:
    import pygame
    w, h = 90 * n + 10, 90 * n + 10
    screen = pygame.display.set_mode((w, h))
    screen.fill('white')
    action_generator = method(p)
    clk = pygame.time.Clock()
    queen_img = pygame.image.load('./queen.png')
    while True:
        for event in pygame.event.get():
            if event == pygame.QUIT:
                exit()
        try:
            actions = next(action_generator)
            screen.fill('white')
            for i in range(n + 1):
                pygame.draw.rect(screen, 'black', (i * 90, 0, 10, h))
                pygame.draw.rect(screen, 'black', (0, i * 90, w, 10))
            for action in actions:
                i, j = action
                screen.blit(queen_img, (10 + 90 * j, 10 + 90 * i))
            pygame.display.flip()
        except StopIteration:
            pass
        clk.tick(5)
    pass
else:
    start_time = time.time()
    for actions in method(p):
        pass
    p.print_state(actions)
    print(time.time() - start_time)
