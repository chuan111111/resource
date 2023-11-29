`timescale 1ps/1ps
module a2t4_mux (
    input [4:0] x,
    output y
);
wire a,b,c,d;

wire ou1,ou2,ou3,ou4,ou5;
and u1(ou1,~x[4],~x[3],~x[2],~x[1],x[0]);
and u2(ou2,a,~x[0]);
and u3(ou3,b,~x[0]);
and u4(ou4,c,~x[0]);
and u5(ou5,d,~x[0]);
or u6(y,ou1,ou2,ou3,ou4,ou5);

mux16to1 u13(16'b00000000000000010, {x[4], x[3], x[2], x[1]}, a);
mux16to1 u9(16'b00000000000000100, {x[4], x[3], x[2], x[1]}, b);
mux16to1 u10(16'b00000000000010000, {x[4], x[3], x[2], x[1]}, c);
mux16to1 u11(16'b00000000100000000, {x[4], x[3], x[2], x[1]}, d);
    
endmodule