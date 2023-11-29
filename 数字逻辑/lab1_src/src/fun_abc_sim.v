module fun_abc_sim ( );
reg sc,sb,sa;
wire sf_mux;

fun_a_b_c_use_mux uf_mux(sc,sb,sa,sf_mux);

initial begin
    {sc,sb,sa}=3'b000;
    repeat(7) 
    begin
        #100  {sc,sb,sa}={sc,sb,sa}+1;
    end
    #100 $finish();
    end
endmodule