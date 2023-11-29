`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2021/08/31 09:01:56
// Design Name: 
// Module Name: lab1_sw_led_24_sim
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


module lab1_sw_led_24_sim( );
//input
reg [23:0] sw=24'h000000;
//output
wire [23:0] led;
//instantiate the unit 
lab1_sw_led_24 usrc1(
.sw(sw),
.led(led)
);

always #10 sw=sw+1;
endmodule
