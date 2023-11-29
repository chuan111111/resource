module moving (
    input turn_left_signal,
    input turn_right_signal,
    input rgs,
    input clk,rst_n,
    output move_forward_signal,
    output move_backward_signal,
    output [1:0] lightsignal
);
reg move_backward_signal,move_forward_signal;
reg [1:0] lightsignal;
always @(*) begin
    if(rgs) {move_backward_signal,lightsignal}={1'b1,2'b00};
    if(~rgs) {move_forward_signal,lightsignal}={1'b1,2'b00};
end

always @(*) begin
    if(turn_left_signal) lightsignal=2'b10;
    if(turn_right_signal) lightsignal=2'b01;
end


always @(*) begin
    case(state)
    NS: begin if({th,cl}==2'b10) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ps,4'b0000};
    else if({th,br,cl}==3'b101) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ss,4'b0000};
        end
    Ss: begin if(br) {next_state,lightsignal,move_backward_signal,move_forward_signal}={NS,4'b1100}; 
    else if({th,br,cl}==3'b100) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ms,4'b0000};
        end
    Ms: begin if(~th) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ss,4'b0000}; 
    else if(cl) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ss,4'b0000}; 
    else if(br) {next_state,lightsignal,move_backward_signal,move_forward_signal}={NS,4'b1100}; 
    else if({rgs,cl}==2'b10) {next_state,lightsignal,move_backward_signal,move_forward_signal}={Ps,4'b0000}; 
    else if(th) 
    begin next_state=Ms;
    if(rgs) 
    begin
    {move_backward_signal,move_forward_signal}=2'b10;
    if({turn_left_signal,turn_right_signal}==2'b10) lightsignal=2'b10;
    else if({turn_right_signal,turn_left_signal}==2'b10) lightsignal=2'b01;
    else lightsignal=2'b00;
    end
    else  
    begin
    {move_forward_signal,move_backward_signal}=2'b10;
    if({turn_left_signal,turn_right_signal}==2'b10) lightsignal=2'b10;
    else if({turn_right_signal,turn_left_signal}==2'b10) lightsignal=2'b01;
    else lightsignal=2'b00;
    end
    end
    endcase
end



always @(*) begin
    case(state)
    NS: if({th,cl}==2'b10) {next_state,lightsignal}={Ps,2'b00}; else if({th,br,cl}==3'b101) {next_state,lightsignal}={Ss,2'b00};

    Ss: if(br) {next_state,lightsignal}={NS,2'b11}; else if({th,br,cl}==3'b100) {next_state,lightsignal}={Ms,2'b00};

    Ms: if(~th) {next_state,lightsignal}={Ss,2'b00}; 
    else if(cl) {next_state,lightsignal}={Ss,2'b00}; 
    else if(br) {next_state,lightsignal}={NS,2'b11}; 
    else if({rgs,cl}==2'b10) {next_state,lightsignal}={Ps,2'b00}; 
    else if(th) begin next_state=Ms,
    if(rgs) move_backward_signal=1'b1;
    if(~rgs) move_forward_signal=1'b1;
    
     end
 endcase
end

always @(posedge clk,negedge rst_n) begin
    if (Ms) begin
        if(turn_left_signal)

        else if(turn_right_signal)

        else if({turn_left_signal,turn_right_signal}=={2'b00})
            lightsignal=2'b00;
        
    end
end
    
endmodule