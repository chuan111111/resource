module led(
	input wire LEDCtrlLEFT,//control signal
   
	input wire rst,//resetation signal 
	input wire [31:0] write_data, //data from memoryorio
	output wire [7:0] IO_LED_Data_left //output led signal
);

    reg [7:0] IO_WriteData = 0;
    


    always @(posedge LEDCtrlLEFT or posedge rst)    
    begin
        if(rst == 1)
         IO_WriteData <=0;
        else if(LEDCtrlLEFT == 1)//high level active
            IO_WriteData <= write_data[7:0];//store data
    
    end

    assign IO_LED_Data_left = IO_WriteData;

endmodule