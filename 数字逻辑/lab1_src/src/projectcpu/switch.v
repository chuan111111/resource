`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////

module switch(switclk, switrst, switchread, switchrdata, switch_i);
    input switclk;			        // clock signal
    input switrst;			        //resetation signal   
    input switchread;			    //  control signal
    output reg [15:0] switchrdata;	    //  output data
    input [7:0] switch_i;		    //  input data

    always@(negedge switclk or posedge switrst) begin
        if(switrst) begin
            switchrdata <= 0;
        end
		else if(switchread) begin
				switchrdata[15:0] <= switch_i; // data output
			
        end
		else begin
            switchrdata <= switchrdata;
        end
    end
endmodule