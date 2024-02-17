import re


class verilog_parser:
    def __init__(self):
        self.file_name = ""
        self.modules = []  # multi module
        self.design_pattern = ""
        self.input = ['a', 'b', 'c', 'd']  # input变量名
        self.input_port = {'a': [0, 0], 'b': [0, 0], 'c': [0, 0], 'd': [0, 0]}  # input的端口字典
        self.output = ['more1_somin', 'more1_pomax']  # output变量名
        self.output_port = {'more1_somin':[0, 0], 'more1_pomax':[0, 0]}  # output的端口字典
        self.assign_output = {}  # assign的output量字典
        self.assign_wire = {}  # assign的wire字典

    def read_netlist(self, file_name):
        self.file_name = file_name
        f = open(self.file_name, "r")
        self.codes = f.read()
        self.read_assign()  # assign的读取
        print(self.check())

    def read_assign(self):
        pattern = r'assign [\s\S]*?;'
        matches = re.findall(pattern, self.codes)
        all_one_bit = True
        for key in self.input_port.keys():
            if self.input_port[key][0] != 0:
                all_one_bit = False
        for key in self.output_port.keys():
            if self.output_port[key][0] != 0:
                all_one_bit = False
        for match in matches:
            wire = False
            match = re.sub(r'\s+', '', match)[6:-1]
            # matches_4 = re.findall()
            match = match.replace('!', '')
            match = match.replace('~', '')
            match = match.replace('^', '')
            strings = match.split('=')
            for key in self.input_port.keys():
                if self.input_port[key][0] == 0:
                    strings[1] = strings[1].replace(f'({key})', f'{key}')
                else:
                    for i in range(0, self.input_port[key][0]):
                        strings[1] = strings[1].replace(f'({key}[{i}])', f'{key}[{i}]')
            pattern_2 = r'[\[][\s\S]*?[]]'
            matches_2 = re.findall(pattern_2, strings[0])
            if len(matches_2) == 1:
                if strings[0].replace(matches_2[0], '') not in self.output:
                    wire = True
            else:
                if strings[0] not in self.output:
                    wire = True

            for key in self.assign_wire.keys():
                if strings[1].count(key) != 0:
                    strings[1] = strings[1].replace(key, self.assign_wire[key])
            if wire:
                self.assign_wire[strings[0]] = f'({strings[1]})'
            else:
                self.assign_output[strings[0]] = strings[1]
        self.bracket_handle()

    def check(self):
        for key in self.assign_output.keys():
            if key == 'more1_somin':
                sample = self.assign_output[key]
                sample = sample.replace('(', '')
                sample = sample.replace(')', '')
                if (len(sample) + 1) % 8 != 0:
                    return False
                else:
                    number_division = (len(sample) + 1) / 8
                    for i in range(0, int(number_division)):
                        if i == number_division - 1:
                            sample_division = sample[8 * i:]
                            if sample_division.count('a') != 1 or sample_division.count(
                                    'b') != 1 or sample_division.count('c') != 1 or sample_division.count('d') != 1:
                                return False
                            if sample_division.count('&') != 3:
                                return False
                        else:
                            sample_division = sample[8 * i: 8 * (i + 1)]
                            if sample_division.count('a') != 1 or sample_division.count(
                                    'b') != 1 or sample_division.count('c') != 1 or sample_division.count('d') != 1:
                                return False
                            if sample_division.count('&') != 3 or sample_division[-1] != '|':
                                return False
            if key == 'more1_pomax':
                sample = self.assign_output[key]
                matches_pomax = re.findall(r'[\(][\s\S]*?[)]', sample)
                for match in matches_pomax:
                    if len(match) != 9:
                        return False
                    if match.count('a') != 1 or match.count(
                            'b') != 1 or match.count('c') != 1 or match.count('d') != 1:
                        return False
                    if match.count('|') != 3:
                        return False
                if (len(sample) + 1) % 10 != 0:
                    return False
                else:
                    number_division = (len(sample) + 1) / 10
                    for i in range(0, int(number_division) - 1):
                        if sample[10 * (i + 1) - 1] != '&':
                            return False
        return True

    def bracket_handle(self):
        for key in self.assign_output.keys():
            symbol_list = self.assign_output[key]
            while True:
                continuous_left = False
                change = False
                for i in range(len(symbol_list)):
                    if (symbol_list[i] == '(') | (symbol_list[i] == ')'):
                        if symbol_list[i] == '(':
                            if not continuous_left:
                                continuous_left = True
                                index_left = i
                            else:
                                symbol_list = symbol_list[:index_left] + symbol_list[index_left + 1:]
                                continuous_left = False
                                change = True
                                break
                        else:
                            continuous_left = False
                if not change:
                    break
            while True:
                continuous_right = False
                change = False
                for i in range(len(symbol_list)):
                    if (symbol_list[i] == '(') | (symbol_list[i] == ')'):
                        if symbol_list[i] == ')':
                            if not continuous_right:
                                continuous_right = True
                                index_right = i
                            else:
                                symbol_list = symbol_list[:i] + symbol_list[i + 1:]
                                continuous_right = False
                                change = True
                                break
                        else:
                            continuous_right = False
                if not change:
                    break
            self.assign_output[key] = symbol_list


if __name__ == '__main__':
    parser = verilog_parser()
    parser.read_netlist("top.v")
    print(1)
