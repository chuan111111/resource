`timescale 1ps/1ps
module a2t4_sim ();
reg [4:0] simx;
wire simy1;
wire simy2;


a2t4_mux u1(simx,simy1);
a2t4_dc u2(simx,simy2);

initial begin
    {simx} = 5'b00000;
    repeat(31)
    begin
        #10 {simx} = {simx}+1;
    end
    #10 $finish();
end
    
endmodule