module lab3_sim ();
    reg [1:0] sima,simb;
    wire [2:0] sum_sim;
    lab31 usrc1(
        .a(sima),
        .b(simb),
        .sum(sum_sim)
    );
initial
begin
    {sima,simb} = 4'b0000;
    repeat(15)
    begin
        #10 {sima,simb} = {sima,simb}+1;
    end
    #10 $finish();
    end
    
endmodule
