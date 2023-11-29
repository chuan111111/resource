`timescale 1ps/1ps
module d74139 (
    input ne,
    input [1:0] x,
    output [3:0] y
);
reg [3:0] y;

always @(*) begin
if (~ne) 
    case (x)
      2'b00: y=4'b1110;
      2'b01: y=4'b1101;
      2'b10: y=4'b1011;
        default: y=4'b0111;
    endcase
else
y=4'hf;
end
endmodule