`timescale 1ps/1ps
module manual (
    input clk,rst_n,th,br,cl,rgs,//从左至右分别为时钟信号（获取系统原始的时钟信号），复位信号（点按复位按钮将回到指定的初始态），油门信号，刹车信号，离合信号，倒挡（用来驾驶小车）
    input turn_left_signal,//输入的左转信号
    input turn_right_signal,//输入的右转信号

    output reg turn_left_signal_over,//经过处理的输入的左转信号
    output reg turn_right_signal_over,//经过处理的输入的右转信号
    output reg [1:0] state,//手动模块涉及的四个状态
    output reg [1:0] lightsignal,//灯光信号
    output reg move_backward_signal,//后退信号
    output reg move_forward_signal//前进信号
   );

reg [1:0] next_state;
parameter NS =2'b00,Ss=2'b01,Ms=2'b10,Ps=2'b11;

always @(posedge clk,negedge rst_n) begin
  if(~rst_n)
  state<=2'b00;
  else
  state<=next_state;
end
reg switch;
    reg prergs = 1'b0;
    always@(posedge clk, posedge rgs) begin
        if(rgs ^ prergs) switch = 1'b1;
        else switch = 1'b0;
        prergs = rgs;
    end

always @(*) begin
    case(state)
    NS: begin 
     casex ({th,br,cl})
        3'b101: next_state=Ss;
        3'b1x0: next_state=Ps;
        default: next_state=NS;
     endcase   
        end

    Ss: begin 
      casex ({th,br,cl})
        3'bx1x: next_state=NS; 
        3'b100: next_state=Ms;
        default: next_state=Ss;
      endcase   
        end

    Ms: begin 
      casex ({th,br,cl,switch})
        4'b0xxx: next_state=Ss;
        4'bxx1x: next_state=Ss;
        4'bx1xx: next_state=NS;
        4'bxx01: next_state=Ps; 
        default: next_state=Ms;
        endcase
        end
    Ps: begin
        next_state = NS;
        end
     endcase
     end   

always @(*) begin
    if(state==Ms)
    begin
    if(rgs)
    {move_backward_signal,move_forward_signal}=2'b10;
    else if(~rgs) {move_backward_signal,move_forward_signal}=2'b01;
   end
else  {move_backward_signal,move_forward_signal}=2'b00;
end

wire  clk_20ms;
divclk_my u2(.clk(clk),.btnclk(clk_20ms));
always @(posedge clk_20ms) begin
if(state==NS)
  lightsignal<=2'b11;
else if(state==Ms || state==Ss)
  case ({turn_left_signal,turn_right_signal})
   2'b10 :begin lightsignal[1]<=~lightsignal[1]; lightsignal[0]<=1'b0;end
   2'b01 :begin lightsignal[0]<=~lightsignal[0]; lightsignal[1]<=1'b0;end
    default: lightsignal<=2'b00;
  endcase
  else lightsignal<=2'b00;
end

always @(*) begin
  if(state==Ms || state==Ss)
  case ({turn_left_signal,turn_right_signal})
   2'b10 :{turn_left_signal_over,turn_right_signal_over}=2'b10;
   2'b01 :{turn_left_signal_over,turn_right_signal_over}=2'b01;
   default: {turn_left_signal_over,turn_right_signal_over}=2'b00;
   endcase
   else {turn_left_signal_over,turn_right_signal_over}=2'b00;
end
      
    endmodule


module divclk_my(input clk,
output btnclk);

reg[31:0] cntclk_cnt=0;
reg btnclk=0;
always@(posedge clk)//20MS: 100M/50 = 2000 000   5HZ
begin
    if(cntclk_cnt==10000000)
    begin
        btnclk=~btnclk;
        cntclk_cnt=0;
    end
    else
        cntclk_cnt=cntclk_cnt+1'b1;
end
endmodule

