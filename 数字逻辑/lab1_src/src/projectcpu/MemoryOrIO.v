module MemoryOrIO( mRead, mWrite, ioRead, ioWrite,addr_in, addr_out, m_rdata, io_rdata, r_wdata, r_rdata, write_data, LEDCtrlLEFT, SwitchCtrl,LEDCtrlone,TubeCtrl,test_index ,InputCtrl,LEDCtrltwinkle);
input mRead; // read memory, from Controller
input mWrite; // write memory, from Controller
input ioRead; // read IO, from Controller
input ioWrite; // write IO, from Controller
input[31:0] addr_in; // from alu_result in ALU
output[31:0] addr_out; // address to Data-Memory
input[31:0] m_rdata; // data read from Data-Memory
input[15:0] io_rdata; // data read from IO,16 bits
output reg[31:0] r_wdata; // data to Decoder(register file)
input[31:0] r_rdata; // data read from Decoder(register file)
output reg[31:0] write_data; // data to memory or I/O（m_wdata, io_wdata)
output LEDCtrlLEFT; // LED Chip Select
output SwitchCtrl; // Switch Chip Select
output LEDCtrlone;  // one led select
output TubeCtrl;  //tube chip select
input[2:0] test_index; //input test_index
 input InputCtrl; //input control signal
output LEDCtrltwinkle;

assign addr_out= addr_in;
// The data wirte to register file may be from memory or io. // While the data is from io, it should be the lower 16bit of r_wdata. assign r_wdata = 锛燂紵锟???
// Chip select signal of Led and Switch are all active high;
always @(*) begin
        if(mRead == 1'b1) begin
            r_wdata <= m_rdata;  //the data from memory 
        end 
        else  begin // data from io
          if (addr_in == 32'hffff_fc70) begin//input data
                r_wdata <= io_rdata;
            end
            else if(addr_in == 32'hffff_fc7c) begin//input ctrl
                r_wdata <= InputCtrl;
            end
            else if(addr_in == 32'hffff_fc78)  begin//input test_index
                r_wdata <= test_index;
            end else
                r_wdata <= 0;
        end 
        
    end

    assign LEDCtrltwinkle = ((addr_in == 32'hffff_fc62)  && (ioWrite == 1))? 1 : 0; //determin led
assign LEDCtrlLEFT = ((addr_in == 32'hffff_fc60)  && (ioWrite == 1))? 1 : 0; //determin led
assign SwitchCtrl= ((addr_in == 32'hffff_fc70)  && (ioRead == 1))? 1 : 0; // determin switch
assign LEDCtrlone = ((addr_in == 32'hffff_fc68)   && (ioWrite == 1))? 1 : 0;//determin one led
assign TubeCtrl= ((addr_in == 32'hffff_fc80)  && (ioWrite == 1))? 1 : 0; //determine tube 
always @* begin
if((mWrite==1) || (ioWrite==1))
//wirte_data could go to either memory or IO. where is it from?
write_data = r_rdata;//store data
else
write_data = 32'hZZZZZZZZ;
end
endmodule