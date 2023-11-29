`timescale 1ps/1ps
module pra1 (
    input [1:0] p,q,
    output      o1,o2,o3
);
wire [1:0] ou1,ou2;
wire ou3,ou4,ou5,ou6,ou7,ou8,ou9,ou10,ou11,ou12;
not u1_1(ou1[1],p[1]); not u1_0(ou1[0],p[0]); not u2_1(ou2[1],q[1]); not u2_0(ou2[0],q[0]);
and u3(ou3,ou1[1],ou1[0],ou2[1],ou2[0]); and u4(ou4,ou1[1],p[0],ou2[1],q[0]); 
and u5(ou5,p[1],p[0],q[1],q[0]); and u6(ou6,p[1],q[1],ou1[0],ou2[0]);
and u7(ou7,ou1[1],q[1]); and u8(ou8,ou1[1],ou1[0],q[0]); and u9(ou9,ou1[0],q[1],q[0]);
and u10(ou10,p[1],ou2[1]); and u11(ou11,p[0],ou2[1],ou2[0]); and u12(ou12,p[1],p[0],ou2[0]);
or u13(o1,ou3,ou4,ou5,ou6); or u14(o2,ou7,ou8,ou9); or u15(o3,ou10,ou11,ou12);   
endmodule