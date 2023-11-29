`timescale 1ps/1ps
module a2t4_dc (
    input [4:0] x,
    output y
);
wire [3:0] p,q,l;    


wire ou1,ou2,ou3,ou4,ou5;
and u1(ou1,p[3],p[2],p[1],~p[0],q[3],q[2],q[1],~q[0],l[3],~l[2],l[1],l[0]);
and u2(ou2,p[3],p[2],p[1],~p[0],q[3],~q[2],q[1],q[0],l[3],l[2],l[1],~l[0]);
and u3(ou3,p[3],p[2],p[1],~p[0],q[3],q[2],~q[1],q[0],l[3],l[2],l[1],~l[0]);
and u4(ou4,p[3],~p[2],p[1],p[0],q[3],q[2],q[1],~q[0],l[3],l[2],l[1],~l[0]);
and u5(ou5,p[3],p[2],~p[1],p[0],q[3],q[2],q[1],~q[0],l[3],l[2],~l[1],l[0]);
or u6(y,ou1,ou2,ou3,ou4,ou5);

d74139 u0(0,{x[1],x[0]},p);
d74139 u7(0,{x[3],x[2]},q);
d74139 u8(0,{x[4],x[0]},l);


endmodule