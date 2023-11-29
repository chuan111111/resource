module multiplexer74151 ( EN,S2,S1,S0,D7,D6,D5,D4,D3,D2,D1,D0,Y,W );
    input  EN,S2,S1,S0,D7,D6,D5,D4,D3,D2,D1,D0;
    output reg Y;
    output W;
    always @(*) 
if (~EN) 
    case ({S2,S1,S0})
         3'b000: Y=D0;
         3'b001: Y=D1;
         3'b010: Y=D2;
         3'b011: Y=D3;
         3'b100: Y=D4;
         3'b101: Y=D5;
         3'b110: Y=D6;
         3'b111: Y=D7;
         
    endcase
else
    Y=1'b0;
assign W=~Y;

endmodule




module fun_c_b_a_use_mux (
    input C,B,A,
    output F
);
wire sen,sd7,sd6,sd5,sd4,sd3,sd2,sd1,sd0;
wire snf;
assign {sen,sd7,sd6,sd5,sd4,sd3,sd2,sd1,sd0}=9'b0_1100_0111;
multiplexer74151 u74151(
    .EN(sen),
    .S2(C),
    .S1(B),
    .S0(A),
    .D7(sd7),
    .D6(sd6),
    .D5(sd5),
    .D4(sd4),
    .D3(sd3),
    .D2(sd2),
    .D1(sd1),
    .D0(sd0),
    .Y(F),
    .W(snf)
);
    
endmodule