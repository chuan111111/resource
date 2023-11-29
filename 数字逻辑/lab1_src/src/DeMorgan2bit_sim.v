`timescale 1ps/1ps
module DeMorgan2bit_sim ();
    reg [1:0] simA,simB;
    wire [1:0] o1_sim,o2_sim,p1_sim,p2_sim;
DeMorgan2bit_df usrc1(
    .A(simA),
    .B(simB),
    .o1(o1_sim),
    .o2(o2_sim),
    .p1(p1_sim),
    .p2(p2_sim)
);
DeMorgan2bit_sd usrc2(simA,simB,o1_sim,o2_sim,p1_sim,p2_sim);
initial begin
    {simA,simB} = 4'b0000;
    repeat(15)
    begin
        #10 {simA,simB} = {simA,simB}+1;
    end
    #10 $finish();
end
    
endmodule