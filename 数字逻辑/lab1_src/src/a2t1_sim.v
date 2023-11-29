`timescale 1ps/1ps
module a2t1_sim ();
reg [3:0] simx;
wire simy1;
wire simy2;
a2t1_xor usrc1(simx,simy1);

a2t1_nand usrc2(simx,simy2);


initial begin
    {simx} = 4'b0000;
    repeat(15)
    begin
        #10 {simx} = {simx}+1;
    end
    #10 $finish();
end
endmodule