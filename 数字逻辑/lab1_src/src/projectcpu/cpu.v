module cpu_top(
  input fpga_rst,  //Active High
  input fpga_clk, 
  input[7:0] switch2N4,
  input[2:0] test_index,
  input InputCtrl,
  
  
  output [7:0] LED_Data_left,
  output LED_Data_one,
    output      LED_Data_twinkle,
  output [7:0] seg_en,
  output [7:0] seg_out0,
  output [7:0] seg_out1

 
);  

  //used for other modules which don't relate to UART
  wire rst;


  wire clk;
  clock clock1(
    .clk_in        (fpga_clk),
    .clk_fpga_out  (clk)   // div clock signal
  );



    wire[31:0] Instruction;                    // the instruction fetched from this module
  wire[31:0] PC_plus_4;                      // (pc+4) to ALU which is used by branch type instruction
  wire[31:0] opcplus4;                       // (pc+4) to Decoder which is used by jal instruction
  wire[31:0] ALU_Result;                     // the calculated address/data from ALU
  wire[31:0] Addr_Result;                    // Calculated address result from ALU
  wire[31:0] write_data;                     // write data to memory
  wire[31:0] mread_data;                     // read data from memory
  wire[31:0] read_data;                      // data to Decoder(register file)
  wire[31:0] address;                        // read/write memory address
  wire[31:0] ioread_data;                    // data read from IO,16 bits
  wire[31:0] rom_adr_w;                      // PC
  wire[31:0] Read_data_1;                    // the address of instruction used by jr instruction
  wire[31:0] Read_data_2;                    // data read from Decoder(register file)
  wire[31:0] Sign_extend;                   //the sign_extend data

  wire Jr,RegDST,ALUSrc,RegWrite,MemWrite,Branch,nBranch,Jmp,Jal,I_format,Sftmd;
  wire[1:0] ALUOp;
 wire Zero;                                 // while Zero is 1, it means the ALUresult is zero
  wire MemRead;                              // read memory, from Controller
  
  IFetc32 Ifetch32_1(
    .Instruction      (Instruction),
    .branch_base_addr (PC_plus_4),
    .link_addr        (opcplus4),
    .clock            (clk),
    .reset            (fpga_rst),
    .Addr_result      (Addr_Result),
    .Zero             (Zero),
    .Read_data_1      (Read_data_1),
    .Branch           (Branch),
    .nBranch          (nBranch),
    .Jmp              (Jmp),
    .Jal              (Jal),
    .Jr               (Jr),
    .pco              (rom_adr_w)
);


  Executs32 Executs32_1(
    .Read_data_1      (Read_data_1),
    .Read_data_2      (Read_data_2),
    .Sign_extend      (Sign_extend),
    . Exe_opcode          (Instruction[31:26]),
    .Function_opcode  (Instruction[5:0]),
    .Shamt            (Instruction[10:6]),
    .PC_plus_4        (PC_plus_4),
    .ALUOp            (ALUOp),
    .ALUSrc           (ALUSrc),
    .I_format         (I_format),
    .Sftmd            (Sftmd),
    .ALU_Result       (ALU_Result),
    .Zero             (Zero),
    .Addr_Result      (Addr_Result)
  ); 
  dmemory32 dm_1(
  .clock(clk),
   .address(address),
     .read_data(mread_data),
    
  .write_data(Read_data_2),
  .Memwrite(MemWrite)
  
  );

  wire ioRead,ioWrite,memorIOtoReg;
  controll32 control32_1(
    .Opcode           (Instruction[31:26]),
    .Function_opcode  (Instruction[5:0]),
    .Alu_resultHigh   (ALU_Result[31:10]),
    .MemorIOtoReg     (memorIOtoReg),
    .MemRead          (MemRead),
    .IORead           (ioRead),   //read io
    .IOWrite          (ioWrite),   //write io
    .Jr               (Jr),
    .Jmp              (Jmp),
    .Jal              (Jal),
    .Branch           (Branch),
    .nBranch          (nBranch),
    .RegDST           (RegDST),
    .RegWrite         (RegWrite),
    .MemWrite         (MemWrite),
    .ALUSrc           (ALUSrc),
    .Sftmd            (Sftmd),
    .I_format         (I_format),
    .ALUOp            (ALUOp)
);

 decoder decoder_1(
    .Instruction      (Instruction),
    .mem_data         (read_data),
    .ALU_result       (ALU_Result),
    .Jal              (Jal),
    .RegWrite         (RegWrite),
    .MemtoReg         (memorIOtoReg),
    .RegDst           (RegDST),
    .clock            (clk),
    .reset            (fpga_rst),
    .opcplus4         (opcplus4),
    .read_data_1      (Read_data_1),
    .read_data_2      (Read_data_2),
    .Sign_extend (Sign_extend)
 );



  wire SwitchCtrl, LEDCtrlLEFT, SegCtrl, LEDCtrlone ,LEDCtrltwinkle;
  MemoryOrIO MemOrIO_1(
    .mRead            (MemRead),
    .mWrite           (MemWrite),
    .ioRead           (ioRead),
    .ioWrite          (ioWrite),
    .addr_in          (ALU_Result),
    .addr_out         (address),
    .m_rdata          (mread_data),
    .io_rdata         (ioread_data),
    .r_wdata          (read_data),
    .r_rdata          (Read_data_2),
    .write_data       (write_data),
    .LEDCtrlLEFT          (LEDCtrlLEFT),
    .SwitchCtrl       (SwitchCtrl),
    .LEDCtrlone         (LEDCtrlone),
    .TubeCtrl         (SegCtrl),
    .test_index       (test_index),
    .InputCtrl        (InputCtrl),
    .LEDCtrltwinkle    (LEDCtrltwinkle)
);

  switch switch_1(
    .switclk      (clk),
    .switrst       (fpga_rst),
    .switchread      (ioRead),
    .switchrdata     (ioread_data),
    .switch_i        (switch2N4)
  );

  led led_1(
    .LEDCtrlLEFT         (LEDCtrlLEFT),
   
	  .rst             (fpga_rst),
	  .write_data      (write_data),
	  .IO_LED_Data_left     (LED_Data_left)
  );

  ledright led_2(
    .LEDCtrlone        (LEDCtrlone),
      .LEDCtrltwinkle      (LEDCtrltwinkle),
	  .rst             (fpga_rst),
	  .write_data      (write_data),
	  .IO_LED_Data_one     (LED_Data_one),
	  .IO_LED_Data_twinkle (LED_Data_twinkle)
  );

  val_to_seg_led u_seg_led(
    .clk             (clk),
    .rst_n            (fpga_rst),
//    .seg_cs           (SegCtrl),
.seg_cs           (1'b1),
    .write_data              (32'b1111_1111_1111_1111_1111_1111_1111_1111),
    .seg_en           (seg_en),
    .seg_out0          (seg_out0),
    .seg_out1         (seg_out1)
    );

endmodule
  