`timescale 1ps/1ps
module DeMorgan2bit_sd (
    input [1:0] A,B,
    output [1:0] o1,o2,p1,p2

);
wire [1:0] ou1,ou2;
not u1_1(ou1[1],A[1]); not u1_0(ou1[0],A[0]); not u2_1(ou2[1],B[1]); not u2_0(ou2[0],B[0]);
and u3_1(o1[1],ou1[1],ou2[1]); and u3_0(o1[0],ou1[0],ou2[0]); nor u4_1(o2[1],A[1],B[1]); nor u4_0(o2[0],A[0],B[0]);
nand u5_1(p1[1],A[1],B[1]); nand u5_0(p1[0],A[0],B[0]); or u6_1(p2[1],ou1[1],ou2[1]); or u6_0(p2[0],ou1[0],ou2[0]);

    
endmodule