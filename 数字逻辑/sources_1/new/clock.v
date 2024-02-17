`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/05/30 19:28:09
// Design Name: 
// Module Name: clock
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

module clock(
    input clk_in,
    output clk_fpga_out
  
);
//vivado自带clk ip�?
cpuclk cpuclk1(
    .clk_in1(clk_in),
    .clk_out1(clk_fpga_out)
  
); 
endmodule
