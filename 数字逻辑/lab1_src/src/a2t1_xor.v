`timescale 1ps/1ps
module a2t1_xor (
    input [3:0] x,
    output y
);
    wire ou1,ou2;
    xor u1(ou1,x[0],x[1]); xor u2(ou2,x[2],x[3]);
    xor u3(y,ou1,ou2);
endmodule