`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2022/05/20 22:09:22
// Design Name: 
// Module Name: decode32
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

module decoder(read_data_1,read_data_2,Instruction,mem_data,ALU_result,
                 Jal,RegWrite,MemtoReg,RegDst,Sign_extend,clock,reset,opcplus4);
    output[31:0] read_data_1;               //     output data1
    output[31:0] read_data_2;               //    output data2
    input[31:0]  Instruction;              // The instruction fetched  
    input[31:0]  mem_data;   				// DATA taken from the DATA RAM or I/O port for writing data to a specified register     
    input[31:0]  ALU_result;   				// The result of an operation from the Arithmetic logic unit ALU used to write data to a specified register
    input        Jal;                       // From the control unit Controller, when the value is 1, it indicates that it is a JAL instruction  
    input        RegWrite;                 // From the control unit Controller, when the value is 1, do register write; When the value is 0, no write is performed
    input        MemtoReg;               // From the control unit Controller, indicating that DATA is written to a register after it is removed from the DATA RAM
    input        RegDst;              //Control write to which specified register in instructions. when RegDst is 1, write to the target register, such as R type Rd; Is 0, writes the second register, such as I type is Rt
    output[31:0] Sign_extend;               // output sign_extend data     
    input		 clock,reset;                //Clock signal,Reset signal, active high level and clear all registers. Writing is not allowed here.
    input[31:0]  opcplus4;                // The JAL instruction is used to write the return address to the $ra register, what we have got here is PC + 4 

    reg[31:0] r[0:31];
    reg[4:0]  wreg=0;
    reg[31:0] wdata=0;
    
    wire[4:0] rreg_1;       //ins[25:21]
    wire[4:0] rreg_2;       //ins[20:16]
    wire[4:0] wreg_I;       //ins[20:16]
    wire[4:0] wreg_R;       //ins[15:11]
    wire[15:0] imm;         //ins[15:0]
    wire[5:0] opcode;       //ins[31:26]
    wire sign;              //ins[15]       
    assign rreg_1=Instruction[25:21];
    assign rreg_2=Instruction[20:16];
    assign wreg_I=Instruction[20:16];
    assign wreg_R=Instruction[15:11];
    assign imm=Instruction[15:0];
    assign sign=Instruction[15];
    assign opcode=Instruction[31:26];
    
       // immediate number extension
    wire [15:0]sign_ex_16;
    assign sign_ex_16=sign ? 16'b1111111111111111:16'b0000000000000000;
    assign Sign_extend = (12 == opcode || 13 == opcode||opcode==14||opcode==11||opcode==9)?{16'b0000000000000000,imm}:{sign_ex_16,imm};

      //read data
    assign read_data_1 = r[rreg_1];
    assign read_data_2 = r[rreg_2];
        //initialize regiter 
    integer i;
    initial begin
        for (i=0;i<32;i=i+1)r[i] <= 0;
    end

        //store target data 
    always @(posedge clock or posedge reset)begin
        if (reset==1)begin
            for (i=0;i<32;i=i+1)r[i] <= 0;
        end
        else if (RegWrite)begin
            r[wreg]<=wdata;
        end
    end
    always @* begin
        if ((opcode==3)&&Jal)begin
            wdata=opcplus4;
        end
        else if(~MemtoReg)begin
            wdata=ALU_result;
        end
        else begin
            wdata = mem_data;
        end
    end

      //destination of register 
    always @* begin
        if(RegWrite)
            wreg = Jal ? 5'b11111:(RegDst ? wreg_R : wreg_I);
    end
endmodule