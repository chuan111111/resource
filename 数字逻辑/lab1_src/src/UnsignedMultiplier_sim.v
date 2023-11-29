`timescale 1ps/1ps
module UnsignedMultiplier_sim ();
    reg [1:0] in1_sim,in2_sim;
    wire [3:0] pro_sim;
    UnsignedMultiplier usrc1(
        .in1(in1_sim),
        .in2(in2_sim),
        .pro(pro_sim)
    );
initial
begin
    {in1_sim,in2_sim} = 4'b0000;
    repeat(15)
    begin
        #10 {in1_sim,in2_sim} = {in1_sim,in2_sim}+1;
    end
    #10 $finish();
end
    
endmodule