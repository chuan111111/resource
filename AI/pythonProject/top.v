module top(
input a,b,c,d,
output more1_somin, more1_pomax
    );
    //a'bcd + ab'cd + abc'd + abcd' + abcd (m7 + m11 + m13+ m14 + m15)
    assign more1_somin = ~a&b&c&d | a&~b&c&d | a&b&~c&d | a&b&c&~d | a&b&c&d;
    //(M0,M1,M2,M3,M4,M5,M6,M8,M9,M10,M12
    assign more1_pomax = a|b|c|(d & a)|b|c|~d & (a|b|~c|d) & (a|b|~c|~d) & (a|~b|c|d) & (a|~b|c|~d) & (a|~b|~c|d) & (~a|b|c|d)
    & (~a|b|c|~d) & (~a|b|~c|d) & ( ~a|~b|c|d) ;
endmodule

 with open(f'./Testcase/Heuristic/map1/dataset1', 'r') as f: