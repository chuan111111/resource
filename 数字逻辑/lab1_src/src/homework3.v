`timescale 1ps/1ps
module homework3 (
    input A,B,C,D,
    output e1,e2,e3
);
assign e1=(~A&~B&~C&~D)|(~A&~B&C&~D)|(~A&B&C&D)|(~A&~B&C&D)|(~A&B&C&~D)|(A&~B&C&~D)|(A&~B&C&D)|(A&B&~C&~D)|(A&B&~C&D)|(A&B&C&D);
assign e2=(A|B|C|~D)&(A|~B|C|D)&(A|~B|C|~D)&(~A|~B|~C|D)&(~A|B|C|D)&(~A|B|C|~D);
assign e3=(~A&C)|(C&D)|(~B&C)|(A&B&~C)|(~A&~B&~D);
    
endmodule