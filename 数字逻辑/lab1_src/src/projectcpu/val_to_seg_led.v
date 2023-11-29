module val_to_seg_led (
    input clk,                 //clock signal
    input rst_n,               // reset signal from fpga
    input seg_cs,              // TubeCtrl from MemoryOrIo
    input [31 : 0] write_data, // write_data from MemoryOrIo
    output reg[7 : 0] seg_en,  // seg control
    output [7 : 0] seg_out0,   // left seg
    output [7 : 0] seg_out1    // right seg
);

 reg clkout;                   // div clock
 reg [31 : 0] cnt;
 reg [2 : 0] scan_cnt;
 reg [3 : 0] num;              // number to be shown
 wire [2 : 0]scan_cnt_plus = scan_cnt + 1;

 parameter period = 20000;


reg [31 : 0] val = 0;
 
 always @(posedge seg_cs or posedge rst_n)    
     begin
         if(rst_n == 1)
          val <=0;
         else if(seg_cs == 1)
             val <= write_data;
     end

 

 always @(posedge clk, negedge rst_n)
 begin
    if (rst_n) begin
        cnt <= 0;
        clkout <= 0;
    end
    else begin
        if (cnt == (period >> 1) - 1) begin
            clkout <= ~clkout;
            cnt <= 0;
        end
        else 
          cnt <= cnt+1;
    end
 end


 always @(posedge clkout, negedge rst_n)
 begin
    if (rst_n)
      {scan_cnt, num} <= {7'b0};
      else begin 
        case (scan_cnt)
            3'b000 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[3 : 0]}; 
            3'b001 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[7 : 4]};
            3'b010 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[11 : 8]};
            3'b011 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[15 : 12]};
            3'b100 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[19 : 16]};
            3'b101 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[23 : 20]};
            3'b110 : {scan_cnt, num} <= {scan_cnt_plus[2 : 0], val[27 : 24]};    
            default: {scan_cnt, num} <= {3'b0, val[31 : 28]}; 
        endcase
        end
 end

  always @(scan_cnt) begin
//   if (seg_cs)
    case (scan_cnt)
        3'b000 : seg_en = 8'h80;
        3'b001 : seg_en = 8'h01;
        3'b010 : seg_en = 8'h02;
        3'b011 : seg_en = 8'h04;
        3'b100 : seg_en = 8'h08;
        3'b101 : seg_en = 8'h10;
        3'b110 : seg_en = 8'h20;
        3'b111 : seg_en = 8'h40;
        default: seg_en = 8'h00;
    endcase
//    else begin
//      seg_en = 8'h00;
//    end
 end

 wire [7 : 0] useless_seg_en0, useless_seg_en1;
 light_7seg_ego1 u0(.sw(num), .seg_out(seg_out0), .seg_en(useless_seg_en0));//instantiate the submodule
  light_7seg_ego1 u1(.sw(num), .seg_out(seg_out1), .seg_en(useless_seg_en1));//instantiate the submodule

  endmodule