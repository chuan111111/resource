`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2022/10/26 20:51:19
// Design Name: 
// Module Name: light_half_7seg
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module light_half_7seg(input sel, input [3:0]sw,output [7:0] seg_out0,seg_out1, output  reg [7:0] seg_en);

//module light_7seg_ego1(input [3:0]sw,output reg [7:0] seg_out, output [7:0] seg_en);
wire [7:0] seg_en0,seg_en1;
light_7seg_ego1 u0(sw,seg_out0,seg_en0);
light_7seg_ego1 u1(sw,seg_out1,seg_en1);

always @* 
    if(sel==1'b0) 
        seg_en = {4'hf,4'h0};
    else
        seg_en = {4'h0,4'hf};    
        
endmodule
