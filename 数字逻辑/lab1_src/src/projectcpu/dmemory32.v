`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2021/05/05 12:17:26
// Design Name: 
// Module Name: dmemory32
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


module dmemory32(clock,address,Memwrite,write_data,read_data);

    input               clock; // clock siganl
    input   [31:0]      address; // read/write memory address
    input               Memwrite; //  determin whether write
    input   [31:0]      write_data; // write data to memory
    output  [31:0]      read_data; // read data from memory


   
    wire clk;
    assign clk = !clock;

    RAM ram (
        .clka(clk), // input wire clka
        .wea(Memwrite), // input wire [0 : 0] wea
        .addra(address[15:2]), // input wire [13 : 0] addra
        .dina(write_data), // input wire [31 : 0] dina
        .douta(read_data) // output wire [31 : 0] douta
    );

endmodule
