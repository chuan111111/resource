`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2021/08/31 09:01:56
// Design Name: 
// Module Name: lab1_sw_led_8_sim
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


module lab1_sw_led_8_sim( );
//connect to input
reg [1:0] in1_sim,in2_sim;
//connect to output
wire [2:0] out_sim;
//instantiate the unit 
lab1_sw_led_8 usrc1(
.in1(in1_sim),
.in2(in2_sim),
.out(out_sim)
);

initial begin
in1_sim=2'b0;
in2_sim=2'b0;
 #10  $finishi();
 end
endmodule
