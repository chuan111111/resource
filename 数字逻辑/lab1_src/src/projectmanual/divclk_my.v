module divclk_my(input clk,
output btnclk);

reg[31:0] cntclk_cnt=0;
reg btnclk=0;
always@(posedge clk)//20MS: 100M/50 = 2000 000   5HZ
begin
    if(cntclk_cnt==5000000)
    begin
        btnclk=~btnclk;
        cntclk_cnt=0;
    end
    else
        cntclk_cnt=cntclk_cnt+1'b1;
end
endmodule