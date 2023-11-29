`timescale 1ps/1ps
module a2t3_bd (
    input [3:0] x,
    output [3:0] y
);
reg [3:0] y;
always @(*)
begin
 y[3]=(~x[3]&x[1]) | (~x[3]&x[0]) | (~x[3]&x[2]) | (x[3]&~x[2]&~x[1]&~x[0]);
 y[2]=(x[2]&~x[1]&~x[0]) | (~x[2]&x[1]) | (~x[2]&x[0]);
 y[1]=(~x[1]&x[0]) | (x[1]&~x[0]);
 y[0]=x[0];
end
endmodule