import re

class verilog_parser:
    def __init__(self):
        self.file_name = ""
        self.modules = [] #multi module
        self.design_pattern=""
        self.input = []           # input变量名
        self.input_port = {}      # input的端口字典
        self.output = []          # output变量名
        self.output_port = {}     # output的端口字典
        self.assign_output = {}   # assign的output量字典
        self.assign_wire = {}     # assign的中间量字典 如{'led' : [7:0]}

    def read_netlist(self, file_name):
        self.file_name = file_name
        f = open(self.file_name, "r")
        self.codes = f.read()
        self.read_inout()          # input和output的读取
        self.read_assign()         # assign的读取

    def read_inout(self):
        m = []
        tl = len(self.codes)
        for i in range(tl):
            c = self.codes[i]
            m.append(c)
            if c.startswith('endmodule'):
                # get a complete module
                self.read_module(m)
                m = []



    def read_input(self, m):

        port_match1 = re.match(r'^(input|inout)\s+[wire]?\s+\[(\d+):(\d+)\]\s+([a-zA-Z_]+[\w]*)', m, re.S)
        if port_match1:
            all_port = re.findall(r'\b(\w+)\b',m)
            port_m = int(port_match1.group(2))
            port_l = int(port_match1.group(3))
            for i in all_port[3:]:
                self.input_port[i]=[port_m,port_l]

        else:
            port_match2 = re.match(r'^(input|output|inout)\s+[wire]?\s+\[(\d+)\]\s+([a-zA-Z_]+[\w]*)', m, re.S)
            if port_match2:
                all_port = re.findall(r'\b(\w+)\b', m)
                port_m = int(port_match1.group(2))
                port_l = int(port_match1.group(2))
                for i in all_port[2:]:
                    self.input_port[i] = [port_m, port_l]


            else:
                port_match3 = re.match(r'^(input|output|inout)\s+[wire]?\s+([a-zA-Z_]+[\w]*)', m, re.S)
                if port_match3:
                    all_port = re.findall(r'\b(\w+)\b', m)
                    for i in all_port[1:]:
                        self.input_port[i] = [0, 0]




    def read_output(self, m):
        pattern_1 = r'output[\s\S]*?[,,)]'
        # pattern_2 = r'input [\s\S]*?)'
        matches_1 = re.findall(pattern_1, m)
        for match in matches_1:
            match = re.sub(r'\s+', '', match)[6:-1]
            pattern_2 = r'[[][\s\S]*?[]]'
            matches_2 = re.findall(pattern_2, match)
            if len(matches_2) == 0:
                self.output.append(match)
                self.output_port[match] = [0, 0]
            else:
                input_name = match.replace(matches_2[0], '')
                self.output.append(input_name)
                pattern_3 = r'[[][\s\S]*?[:]'
                pattern_4 = r'[:][\s\S]*?[]]'
                matches_3 = re.findall(pattern_3, matches_2[0])
                matches_4 = re.findall(pattern_4, matches_2[0])
                left = int(matches_3[0][1:-1])
                right = int(matches_4[0][1:-1])
                self.output_port[input_name] = [left, right]

    def read_assign(self):
        pattern = r'assign [\s\S]*?;'
        matches = re.findall(pattern, self.codes)
        for match in matches:
            wire = False
            match = re.sub(r'\s+', '', match)[6:-1]
            match = match.replace('!', '')
            match = match.replace('~', '')
            match = match.replace('^', '')
            strings = match.split('=')
            pattern_2 = r'[[][\s\S]*?[]]'
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
                self.assign_wire[strings[0]] = f'{strings[1]}'
            else:
                self.assign_output[strings[0]] = strings[1]

        for key in self.assign_output.keys():
            expression=self.assign_output[key]

            expression.replace(" ", "")
            expression.replace("/n", "")
            # 提取出所有的符号位
            symbol_list = ""
            for i in range(len(expression)):
                if expression[i] == '&' or expression[i] == "|" or expression[i] == "(" or \
                        expression[i] == ')':
                    symbol_list = symbol_list + expression[i]
            # 匹配（&&&）|(&&&)...这类，括号可有可无
            sop_match = re.match(r'^[\[(]?[&]+[)]?|]+[(]?[&]+[)]?$', symbol_list,re.X)

            if sop_match:
                symbol_list.replace('(', '')
                symbol_list.replace(')', '')
                # 通过检测数量来判断是否包含所有项
                number = 0
                som_test = True
                for i in range(len(symbol_list)):
                    if symbol_list[i] == '&':
                        number += 1
                    elif number == len(self.input)-1:
                        number = 0
                    else:
                        som_test = False
                        break
            # 匹配（|||）&(|||)...括号必须有
            else:
                pos_match = re.match(r'^[(\S*)|]+', symbol_list)
                if pos_match:
                    # 通过检测数量来判断是否包含所有项
                    number = 0
                    pom_test = True
                    for i in range(len(symbol_list)):
                        if i != 0:
                            if symbol_list[i] == '|':
                                number += 1
                            elif number == len(self.input)-1:
                                number = 0
                                i += 1
                            else:
                                pom_test = False
                                break

        # print(matches)

'''
 while True:
                for wire_key in self.assign_wire.keys():
                    if wire_key in expression:
                        expression.replace(wire_key, self.assign_wire[wire_key])
                result1 = True
                for wire_keys in self.assign_wire.keys():
                    if wire_keys in expression:
                        expression.replace(wire_keys, self.assign_wire[wire_keys])
                        result1 = False
                if result1 == True:
                    break
'''
if __name__ == '__main__':
    parser = verilog_parser()
    parser.read_netlist("test.v")
    print(1)