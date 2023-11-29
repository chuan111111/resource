`timescale 1ps/1ps
module decoder_mux_sim ();
reg sen;
reg [1:0] x1_sim;
reg [15:0] x2_sim;
reg [3:0] ssel;
wire [3:0] y1_sim;
wire y2_sim;
    

d74139 u3(sen,x1_sim,y1_sim);
mux16to1 u4(x2_sim,ssel,y2_sim);

initial
begin
    {sen,x1_sim} = 3'b000;
    repeat(7)
    begin
        #10 {sen,x1_sim} = {sen,x1_sim}+1;
    end
    #10
ssel = 4'h0; x2_sim = 16'b0000_0000_0000_0001;
repeat(15)
    begin
        #10 {ssel} = {ssel}+1;
        case (ssel)
        4'h0: x2_sim = 16'b0000_0000_0000_0001;
        4'h1: x2_sim = 16'b0000_0000_0000_0010;
        4'h2: x2_sim = 16'b0000_0000_0000_0100;
        4'h3: x2_sim = 16'b0000_0000_0000_1000;
        4'h4: x2_sim = 16'b0000_0000_0001_0000;
        4'h5: x2_sim = 16'b0000_0000_0010_0000;
        4'h6: x2_sim = 16'b0000_0000_0100_0000;
        4'h7: x2_sim = 16'b0000_0000_1000_0000;
        4'h8: x2_sim = 16'b0000_0001_0000_0000;
        4'h9: x2_sim = 16'b0000_0010_0000_0000;
        4'ha: x2_sim = 16'b0000_0100_0000_0000;
        4'hb: x2_sim = 16'b0000_1000_0000_0000;
        4'hc: x2_sim = 16'b0001_0000_0000_0000;
        4'hd: x2_sim = 16'b0010_0000_0000_0000;
        4'he: x2_sim = 16'b0100_0000_0000_0000;
        4'hf: x2_sim = 16'b1000_0000_0000_0000; 
    endcase
    end
    #10 $finish();
    
end
endmodule