#是否使用指定的模块(包括逻辑门，关键字)的指定个数，包括建模方式的检测

import re
# import pyverilog

class port:
    def __init__(self):
        self.name = "default"
        self.direction = "input"
        self.type = "wire"
        self.maxbit = 0
        self.leastbit = 0

    def new_port(self, name, direction="input", type="wire", maxbit=0, leastbit=0):
        self.direction = direction
        self.maxbit = maxbit
        self.leastbit = leastbit
        self.name = name

class wire:
    def __init__(self, name):
        self.name = name
        self.connection = []  # a wire can connect to multiple port

    def add_connection(self, instantiation, port):
        self.connection.append([instantiation, port]) #(None,\w+) input/output/inout port name
                                                      #(instantiation,\w+) connected instantiation and name of port in instantiation


class module:
    def __init__(self, name):
        self.name = name
        self.ports = []
        self.wires = []
        self.instantiations = []
        self.design_pattern = ""  # structural modeling, data flow modeling, behavioral modeling

    def add_port(self, port):
        self.ports.append(port)

    def add_wire(self, wire):
        self.wires.append(wire)

    def add_ins(self, ins):
        self.instantiations.append(ins)

    def whether_exist_wire(self, name):
        for wire in self.wires:
            if wire.name == name:
                return True
        return False

    def find_wire(self, name):
        for i in range(len(self.wires)):
            if self.wires[i].name==name:
                return i
        return None

class instantiation:

    def __init__(self, name):
        self.name = name
        self.module_name = "default"
        self.ports_map = [] #module_name instantiation_name(.A(a),.B(b))

    def new_instantiation(self, module_name, name):
        self.module_name = module_name
        self.name = name

    def add_port_map(self, port_name, wire_name):
        self.ports_map.append([port_name, wire_name])

    def add_port_maps(self, port_map):
        for port_pair in port_map:
            self.ports_map.append(port_pair)

class verilog_analyser:
    def __init__(self):
        self.file_name = ""
        self.modules = [] #multi module
        self.code_lines = []
        self.design_pattern="" #structural modeling, data flow modeling, behavioral modeling

    def read_netlist(self, file_name):
        self.file_name = file_name

        f = open(self.file_name, "r")
        self.codes = f.readlines()
        self.split_code()

    def split_code(self):
        m = []
        tl = len(self.codes)
        for i in range(tl):
            c = self.codes[i]
            m.append(c)
            if c.startswith('endmodule'):
                #get a complete module
                self.read_module(m)
                m = []

    def read_module(self, m):
        # remove comment lines and empty lines
        m1 = []
        comment_tag=False
        for l in m:
            if comment_tag:
                comment = re.match(r'^(.*)\*\/(.*)', l) # .....xxxxxxx */ XXXXX
                if comment:
                    if comment.group(2)!='':
                        m1.append(comment.group(2))
                    comment_tag=False
            else:
                if re.match(r'^\s*\/\/', l) or re.match(r'^\s*\n$', l):# //xxxxx or blank
                    continue
                comment1=re.match(r'^(.*)\/\/(.*)$',l) # xxxxxxxx//XXXXX
                if comment1:
                    m1.append(comment1.group(1))
                    continue
                comment2=re.match(r'^(.*)\/\*(.*)\*\/$',l) #xxxxx/*xxxxxxx*/
                if comment2:
                    if comment2.group(1)!='':
                        m1.append(comment2.group(1))
                    continue
                comment3=re.match(r'^(.*)\/\*(.*)',l) # xxxxxx /* XXXXXXX
                if comment3:
                    if comment3.group(1)!='':
                        m1.append(comment3.group(1))
                    comment_tag=True
                    continue
                m1.append(l)

        # divide module by ';'
        codes = ''.join(m1)
        lines = codes.split(';')

        for i in range(len(lines)):
            # module name
            i_match = re.match(r'^module\s+(\w+)\s+', lines[i], re.S)
            if i_match:
                module_name = i_match.group(1)
                module_temp = module(module_name)
                self.modules.append(module_temp)
                self.code_lines.append(lines[i])
                continue

            # port
            temp = re.sub(r'end\n', '', lines[i], re.S) #暂时处理 begin end(begin,end后无;进行语句分隔)
            temp = re.sub(r'\n', '', temp, re.S)
            self.code_lines.append(temp)
            port_match1 = re.match(r'^(input|output|inout)\s+[wire]?\s+\[(\d+):(\d+)\]\s+([a-zA-Z_]+[\w]*)', temp, re.S)
            if port_match1:
                print(port_match1)
                all_port = re.findall(r'\b(\w+)\b', temp)
                port_m=int(port_match1.group(2))
                port_l=int(port_match1.group(3))
                add_ports(all_port[3:],module_temp,port_m,port_l,port_match1.group(1))
                continue

            else:
                port_match2 = re.match(r'^(input|output|inout)\s+[wire]?\s+\[(\d+)\]\s+([a-zA-Z_]+[\w]*)', temp, re.S)
                if port_match2:
                    print(port_match2)
                    all_port = re.findall(r'\b(\w+)\b', temp)
                    port_m = int(port_match1.group(2))
                    port_l = int(port_match1.group(2))
                    add_ports(all_port[2:], module_temp,port_m,port_l,
                              port_match2.group(1))
                    continue

                else:
                    port_match3 = re.match(r'^(input|output|inout)\s+[wire]?\s+([a-zA-Z_]+[\w]*)', temp, re.S)
                    if port_match3:
                        print(port_match3)
                        all_port=re.findall(r'\b(\w+)\b',temp)
                        add_ports(all_port[1:], module_temp, 0, 0,
                                  port_match3.group(1))
                        continue

            # if port_found == 1:
            #     port_m = int(port_m)
            #     port_l = int(port_l)
            #
            #     port_temp = port()
            #     port_temp.new_port(port_name, port_direction, "wire", port_m, port_l)
            #     module_temp.add_port(port_temp)
            #
            #     for i in range(port_l, port_m+1):
            #         wire_temp = wire("{}{}".format(port_name, i))
            #         wire_temp.add_connection(None, "{}[{}]".format(port_name, i))
            #         module_temp.add_wire(wire_temp)
            #
            #     continue

            # wire
            wire_match = re.match(r'^wire\s+\[(\d+):(\d+)\]\s+(\w+)', temp, re.S)
            if wire_match:
                port_direction = "input"
                port_m = wire_match.group(1)
                port_l = wire_match.group(2)
                port_name = wire_match.group(3)

                port_m = int(port_m)
                port_l = int(port_l)

                for i in range(port_l, port_m+1):
                    wire_temp = wire("{}{}".format(port_name, i))
                    wire_temp.add_connection(None, "{}[{}]".format(port_name, i))
                    module_temp.add_wire(wire_temp)

                continue

            #reg    reg ([1:0] or [1])

            # assign e([7:0] or [0])=d([7:0] or [0])
            assign_match=re.match(r'^assign\s+(\w+)(.*)\s+=(.*)',temp,re.S)
            if assign_match:
                wire_a=assign_match.group(1)
                wire_lm=assign_match.group(2)
                expression=assign_match.group(3)
                tem_m=self.modules
                if tem_m[len(tem_m)-1].design_pattern=="":
                    self.modules[len(self.modules)-1].design_pattern="data flow modeling"

            # always/initial
            structural_match1=re.match(r'^always\s+@\(.*\)\s+begin',temp,re.S)
            if structural_match1:
                tem_m = self.modules
                if tem_m[len(tem_m) - 1].design_pattern == "":
                    self.modules[len(self.modules) - 1].design_pattern = "behavioral modeling"
            structural_match2 = re.match(r'^initial\s+@\(.*\)\s+begin', temp, re.S)
            if structural_match2:
                tem_m = self.modules
                if tem_m[len(tem_m) - 1].design_pattern == "":
                    self.modules[len(self.modules) - 1].design_pattern = "behavioral modeling"


            # endmodule
            ret = re.match(r'^\s*endmodule', temp, re.S)
            if ret:
                continue

            # instantiation
            instantiation_match = re.match(r'^\s*(\w+)\s+(\w+)\s+\((.*)\)\s*$', temp, re.S)
            if instantiation_match:
                tem_m = self.modules
                if tem_m[len(tem_m) - 1].design_pattern == "":
                    self.modules[len(self.modules) - 1].design_pattern = "structural modeling"
                module_name = instantiation_match.group(1)
                instantiation_name = instantiation_match.group(2)
                port_map_s = instantiation_match.group(3)

                instantiation_temp = instantiation(instantiation_name)
                instantiation_temp.new_instantiation(module_name, instantiation_name)

                port_map_s = re.sub(r'\n', '', port_map_s, re.S)
                port_map_s = re.sub(r'\s', '', port_map_s, re.S)
                port_map_l = port_map_s.split(',')
                for port_map in port_map_l:
                    r = re.match(r'\.(\w+)\((.*)\)', port_map)  # .* match xx[2], xx[2:0]
                    if r:
                        instantiation_temp.add_port_map(r.group(1), r.group(2))
                        if module_temp.whether_exist_wire(r.group(2)):
                            i = module_temp.find_wire(r.group(2))
                            module_temp.wires[i].add_connection(instantiation_name, r.group(1))
                        else:
                            wire_temp = wire(r.group(2))
                            wire_temp.add_connection(instantiation_name, r.group(1))
                            module_temp.add_wire(wire_temp)

                module_temp.add_ins(instantiation_temp)
            else:
                print(temp)

def add_ports(port_names,module,port_m,port_l,port_direction):
    for port_name in port_names:
        port_temp = port()
        port_temp.new_port(port_name, port_direction, "wire", port_m, port_l)
        module.add_port(port_temp)

        for i in range(port_l, port_m):
            wire_temp = wire("{}{}".format(port_name, i))
            wire_temp.add_connection(None, "{}[{}]".format(port_name, i))
            module.add_wire(wire_temp)


if __name__ == '__main__':
    parser = verilog_analyser()
    parser.read_netlist("test.v")
    print(1)
