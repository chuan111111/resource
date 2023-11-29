`timescale 1ps/1ps
module a2t2_somin (
    input [3:0] x,
    output y
);
wire ou0,ou1,ou2,ou3,ou4,ou5,ou6,ou7,ou8,ou9,ou10,ou11,ou12,ou13;
not u0(ou0,x[3]); not u1(ou1,x[2]); not u2(ou2,x[1]); not u3(ou3,x[0]);
and u4(ou4,ou0,ou1,ou2,ou3); and u5(ou5,ou0,ou1,ou2,x[0]); and u6(ou6,ou0,ou1,x[1],ou3); and u7(ou7,ou0,ou1,x[1],x[0]);
and u8(ou8,ou0,x[2],ou2,ou3); and u9(ou9,ou0,x[2],ou2,x[0]); and u10(ou10,ou0,x[2],x[1],ou3); and u11(ou11,ou0,x[2],x[1],x[0]);
and u12(ou12,x[3],ou1,ou2,ou3); and u13(ou13,x[3],ou1,ou2,x[0]); or u14(y,ou4,ou5,ou6,ou7,ou8,ou9,ou10,ou11,ou12,ou13);

    
endmodule