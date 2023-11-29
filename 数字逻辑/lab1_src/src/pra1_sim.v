`timescale 1ps/1ps
module pra1_sim ( );
reg [1:0] simp,simq;
wire o1_sim,o2_sim,o3_sim;
pra1 u1(simp,simq,o1_sim,o2_sim,o3_sim);

initial begin
    {simp,simq} = 4'b0000;
 repeat(15)
    begin
        #10 {simp,simq} = {simp,simq}+1;
    end
    #10 $finish();
end
    
endmodule