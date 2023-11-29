`timescale 1ps/1ps
module a2t1_nand (
    input [3:0] x,
    output y
);
    wire ou1,ou2,ou3,ou4,ou5,ou6,ou7,ou8,ou9,ou10,ou11;
    nand u1(ou1,x[0],x[1]); nand u2(ou2,x[0],ou1); nand u3(ou3,x[1],ou1); nand u4(ou4,ou2,ou3);
    nand u5(ou5,x[2],x[3]); nand u6(ou6,x[2],ou5); nand u7(ou7,x[3],ou5); nand u8(ou8,ou6,ou7);
    nand u9(ou9,ou4,ou8); nand u10(ou10,ou4,ou9); nand u11(ou11,ou8,ou9); nand u12(y,ou10,ou11);
endmodule