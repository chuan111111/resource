module IFetc32(Instruction, branch_base_addr, link_addr, clock, reset, Addr_result, Read_data_1, Branch, nBranch, Jmp, Jal, Jr, Zero,pco);
    output[31:0] Instruction; // the instruction fetched from this module
    output[31:0] branch_base_addr; // (pc+4) to ALU which is used by branch type instruction
    output reg [31:0] link_addr; // (pc+4) to Decoder which is used by jal instruction
    input clock, reset; // Clock and reset
    // from ALU
    input[31:0] Addr_result; // the calculated address from ALU
    input Zero; // while Zero is 1, it means the ALUresult is zero

    // from Decoder
    input[31:0] Read_data_1; // the address of instruction used by jr instruction

    // from Controller
    input Branch; // while Branch is 1,it means current instruction is beq
    input nBranch; // while nBranch is 1,it means current instruction is bnq
    input Jmp; // while Jmp 1, it means current instruction is jump
    input Jal; // while Jal is 1, it means current instruction is jal
    input Jr; // while Jr is 1, it means current instruction is jr
output  [31:0]  pco;
    reg[31:0] PC=0, Next_PC=0;  

    assign branch_base_addr = PC + 4;

    always @(Branch or nBranch or Zero or Addr_result or Read_data_1 or Jr or Jmp or Jal or PC or Instruction) 
    begin
        if((Branch == 1 && Zero == 1) || (nBranch == 1 && Zero == 0)) // the calculated new value for PC
            Next_PC = Addr_result;
        else if(Jr == 1)
            Next_PC = Read_data_1; // the value of $31 regist
        else if(Jal == 1 || Jmp == 1)
            Next_PC = {PC[31:28], Instruction[25:0], 2'b00};
        else
            Next_PC = PC + 4;
    end


//updata the value of PC
    always @( negedge clock)
    begin 
        if(reset == 1) PC <= 32'h0000_0000; 
        else PC <= Next_PC; 
    end

    always @(negedge clock)
    begin
        if ((Jmp == 1) || (Jal == 1)) 
            link_addr <= (PC + 4);
        else
            link_addr <= link_addr;  // (pc+4) to Decoder which is used by ‘jal'
    end

    assign branch_base_addr = PC + 4; // (pc+4) to ALU which is used by branch type instruction
     assign pco = PC;

    // wire kickOff = 1;
    prgrom instmem(.clka(clock),.addra(PC[15:2]),.douta(Instruction));

endmodule