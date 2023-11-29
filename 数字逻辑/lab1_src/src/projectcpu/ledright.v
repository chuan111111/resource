module ledright(
   input clk,
   input wire LEDCtrlone,//the signal control one clk,(overflow ,ect)
   input wire LEDCtrltwinkle,//the signal control one clk twinkle,(negative ,ect)
	input wire rst,//restoration
	input wire [31:0] write_data, //data form memoryorio
	output wire  IO_LED_Data_one, //output led signal
    output reg IO_LED_Data_twinkle// output led signal
);

    reg  IO_WriteData = 0;
    reg  IO_WriteData2 = 0;
//    reg clkout;
     reg [31 : 0] cnt;
      parameter period = 20000000;
    
    always @(posedge clk, posedge rst)//implement twinkle
     begin
        if (rst) begin
            cnt <= 0;
            IO_LED_Data_twinkle <= 1'b0;
        end
        else begin
            if (cnt == (period >> 1) - 1) begin
                if(IO_WriteData2)
                IO_LED_Data_twinkle <= !IO_LED_Data_twinkle;
                cnt <= 0;
            end
            else 
              cnt <= cnt+1;
        end
     end

    always @(posedge LEDCtrlone or posedge rst  )    
    begin
        if(rst == 1)
         IO_WriteData <=0;
        else if(LEDCtrlone == 1)//high active 
                IO_WriteData <=write_data[16];
    end

    assign IO_LED_Data_one = IO_WriteData;


    always @(posedge LEDCtrltwinkle or posedge rst  )    
    begin
        if(rst == 1)
         IO_WriteData2 <=0;
        else if(LEDCtrltwinkle == 1)//control signal
                IO_WriteData2 <=write_data[16];
    end

endmodule