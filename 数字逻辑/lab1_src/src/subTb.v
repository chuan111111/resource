module subTb( ); //verilog
reg [2:0] in1,in2;
wire [2:0] result;
wire overflow;
subtraction usubO2(in1,in2,result,overflow);
initial begin
{in1,in2} = 6'b0;
$monitor( "%3b-%3b: result = %3b(%d)",
in1,in2,result,$signed(result) );
repeat(63) #10 {in1,in2} = {in1,in2} + 1;
#10 $finish();
end
endmodule