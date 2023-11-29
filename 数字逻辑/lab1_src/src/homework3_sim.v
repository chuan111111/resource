`timescale 1ps/1ps
module homework3_sim ();
reg simA,simB,simC,simD;
wire e1_sim,e2_sim,e3_sim;
homework3 usrc1(
    .A(simA),
    .B(simB),
    .C(simC),
    .D(simD),
    .e1(e1_sim),
    .e2(e2_sim),
    .e3(e3_sim)
);
initial begin
     {simA,simB,simC,simD} = 4'b0000;
 repeat(15)
    begin
        #10 {simA,simB,simC,simD} = {simA,simB,simC,simD}+1;
    end
    #10 $finish();
end
    
endmodule