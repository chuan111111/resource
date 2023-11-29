`timescale 1ps/1ps
module UnsignedMultiplier (
    input [1:0] in1,in2,
    output [3:0] pro
);
assign pro[0]=in1[0]&in2[0];
assign pro[1]=(in1[1]&~in1[0]&in2[0])|(in1[1]&~in2[1]&in2[0])|(~in1[1]&in1[0]&in2[1])|(in1[0]&in2[1]&~in2[0]);
assign pro[2]=(in1[1]&~in1[0]&in2[1])|(in1[1]&in2[1]&~in2[0]);
assign pro[3]=in1[1]&in1[0]&in2[0]&in2[1];
   
endmodule