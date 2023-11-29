`timescale 1ps/1ps
module pra2 (
    input [3:0] sid,
    output [1:0] g
);
reg [1:0] g;
always @(*) begin
   case ({sid})
    4'b0000,4'b0100,4'b1000,4'b1100:
   g = 2'b00;
    4'b0001,4'b0101,4'b1001,4'b1101:
   g = 2'b01;
    4'b0010,4'b0110,4'b1010,4'b1110:
   g = 2'b10;
    default: g =2'b11;
   endcase
    
end
    
endmodule