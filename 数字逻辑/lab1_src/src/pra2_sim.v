`timescale 1ps/1ps
module pra2_sim ( );
reg [3:0] sid_sim;
wire [1:0] simg;
pra2 u1(sid_sim,simg);

initial begin
    sid_sim = 4'b0000;
 repeat(15)
    begin
        #10 sid_sim = sid_sim+1;
    end
    #10 $finish();
end
    
endmodule