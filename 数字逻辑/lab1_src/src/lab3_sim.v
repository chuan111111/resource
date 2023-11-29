module lab3_sim ( );
   reg simx,simy,simz;
   wire simo1,simo2,simo3;
   lab3 usrc1(
    .x(simx),
    .y(simy),
    .z(simz),
    .o1(simo1),
    .o2(simo2),
    .o3(simo3)
   );
   initial
   begin
    simx=0
    simy=0
    simz=0
    #10
    simx=0
    simy=1
    simz=0
    #10
    simx=1
    simy=0
    simz=1
    end
    
endmodule