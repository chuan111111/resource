`timescale 1ps/1ps
module a2t3_df (
    input [3:0] x,
    output [3:0] y
);
assign y[3]=(~x[3]&x[1]) | (~x[3]&x[0]) | (~x[3]&x[2]) | (x[3]&~x[2]&~x[1]&~x[0]);
assign y[2]=(x[2]&~x[1]&~x[0]) | (~x[2]&x[1]) | (~x[2]&x[0]);
assign y[1]=(~x[1]&x[0]) | (x[1]&~x[0]);
assign y[0]=x[0];
    
endmodule