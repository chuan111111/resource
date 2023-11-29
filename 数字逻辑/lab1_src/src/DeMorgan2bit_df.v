`timescale 1ps/1ps
module DeMorgan2bit_df (
    input [1:0] A,B,
    output [1:0] o1,o2,p1,p2

);
assign o1[1]=~A[1]&~B[1];
assign o1[0]=~A[0]&~B[0];
assign o2[1]=~(A[1]|B[1]);
assign o2[0]=~(A[0]|B[0]);
assign p1[1]=~(A[1]&B[1]);
assign p1[0]=~(A[0]&B[0]);
assign p2[1]=~A[1]|~B[1];
assign p2[0]=~A[0]|~B[0];
    
endmodule