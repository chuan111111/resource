module controll32 (Opcode,Function_opcode,Alu_resultHigh,MemorIOtoReg,MemRead,IORead,IOWrite,Jr,Jmp,Jal,Branch,nBranch,RegDST,RegWrite,MemWrite,ALUSrc,Sftmd,I_format,ALUOp);
      input[5:0] Opcode; // instruction[31:26], opcode
    input[5:0] Function_opcode; // instructions[5:0], funct
    input[21:0] Alu_resultHigh; // From the execution unit Alu_Result[31..10]

    output MemorIOtoReg; //1 indicates that read date from memory or I/O to
    output MemRead; // 1 indicates that reading from the memory to get data
    output IORead; // 1 indicates I/O read
    output IOWrite;//1 indicates io write
    output Jr ; // 1 indicates the instruction is "jr", otherwise it's not "jr" output Jmp 
    output Jmp;// 1 indicate the instruction is "j", otherwise it's not
    output Jal; // 1 indicate the instruction is "jal", otherwise it's not
    output Branch; // 1 indicate the instruction is "beq" , otherwise it's not
    output nBranch; // 1 indicate the instruction is "bne", otherwise it's not
    output RegDST; // 1 indicate destination register is "rd"(R),otherwise it's "rt"(I)
   
    output RegWrite; // 1 indicate write register(R,I(lw)), otherwise it's not
    output MemWrite; // 1 indicate write data memory, otherwise it's not
    output ALUSrc; // 1 indicate the 2nd data is immidiate (except "beq","bne")
    output Sftmd; // 1 indicate the instruction is shift
    output I_format; //identify if the instruction is I_type(except for beq, bne, lw and sw)
    output[1:0] ALUOp; //ALUOp is multi bit width port, if the instruction is R-type or I_format, ALUOp is 2'b10;if “beq” or “bne“, ALUOp is 2'b01；if “lw” or “sw“, ALUOp is 2'b00


    

    wire R_format; 
    assign R_format = (Opcode==6'b000000)? 1'b1:1'b0;
    assign RegDST = R_format;

    wire Lw; 
    assign Lw = (Opcode==6'b100011)? 1'b1:1'b0;
    wire Sw;
    assign Sw = (Opcode==6'b101011) ? 1'b1:1'b0;

    assign MemRead = ((Lw==1) && (Alu_resultHigh[21:0]!= 22'h3FFFFF))?1'b1:1'b0; // Read memory
    assign IORead = ((Lw==1) && (Alu_resultHigh[21:0]== 22'h3FFFFF))?1'b1:1'b0; // Read input port
    assign IOWrite = ((Sw==1) && (Alu_resultHigh[21:0]== 22'h3FFFFF))?1'b1:1'b0; // Write output port
// Read operations require reading data from memory or I/O to write to the register

    assign Jr =((Opcode==6'b000000)&&(Function_opcode==6'b001000)) ? 1'b1 : 1'b0;
    assign Jmp      = (Opcode==6'b000010) ? 1'b1:1'b0;
    assign Jal      = (Opcode==6'b000011) ? 1'b1:1'b0;
    assign Branch   = (Opcode==6'b000100) ? 1'b1:1'b0;
    assign nBranch  = (Opcode==6'b000101) ? 1'b1:1'b0;

    assign RegWrite = (R_format || Lw || Jal|| I_format) && !(Jr);
    assign I_format = (Opcode[5:3]==3'b001) ? 1'b1 : 1'b0; //whether i format
    assign ALUOp = { (R_format || I_format) , (Branch || nBranch)};
    assign Sftmd = (((Function_opcode==6'b000000)||(Function_opcode==6'b000010)
                    ||(Function_opcode==6'b000011)||(Function_opcode==6'b000100)
                    ||(Function_opcode==6'b000110)||(Function_opcode==6'b000111))
                    && R_format)? 1'b1 : 1'b0;   //whether shift
     
    assign MemWrite = ((Sw==1) && (Alu_resultHigh[21:0] != 22'h3FFFFF)) ? 1'b1:1'b0;//whether write data memory
    assign ALUSrc = I_format || Lw || Sw;//whether the 2nd data is immidiate (except "beq","bne")
    assign MemorIOtoReg = IORead || MemRead; // Read operations require reading data from memory or I/O to write to the register

endmodule