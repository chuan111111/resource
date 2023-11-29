`timescale 1ps/1ps
module a2t3_sim ();
reg [3:0] simx;
wire [3:0] simy1;
wire [3:0] simy2;
a2t3_df usrc1(
    .x(simx),
    .y(simy1)
);

a2t3_bd usrc2(simx,simy2);

initial begin
    {simx} = 4'b0000;
    repeat(15)
    begin
        #10 {simx} = {simx}+1;
    end
    #10 $finish();
end
endmodule