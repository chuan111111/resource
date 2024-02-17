.data 
.text
###每次执行完对应的测试用例均跳转至begin重新输入测试用例
start: lui   $28,0xFFFF			
       ori   $28,$28,0xF000		
begin_1:
lw $s7,0xC7C($28)
bne $s7,$zero,begin_1
begin_2:
lw $s7,0xC7C($28)
beq $s7,$zero,begin_2
lw $a3,0xC78($28)#a3存放的是测试用例

sw $zero,0xC68($28)
sw $zero,0xC62($28)
sw $zero,0xC80($28)
sw $zero,0xC60($28)
addi $s2,$zero,0
beq $a3,$s2,test0_1
addi $s2,$s2,1
beq $a3,$s2,test1_1
addi $s2,$s2,1
beq $a3,$s2,test2_1
addi $s2,$s2,1
beq $a3,$s2,test3_1
addi $s2,$s2,1
beq $a3,$s2,test4_1
addi $s2,$s2,1
beq $a3,$s2,test5_1
addi $s2,$s2,1
beq $a3,$s2,test6_1
addi $s2,$s2,1
beq $a3,$s2,test7_1

####################################
test0_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test0_1
test0_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test0_2
lw $v0,0xC70($28)#v0存放a的值，a存在C70字的低16位
sw $v0,0xC60($28)#将v0也就是a的值显示在led灯上

addi $s0,$v0,0
addi $t7,$v0,0 #将v0存入s0，方便比较

sll $t7,$t7,24
srl $t7,$t7,31

beq $t7,$zero,contine
lui $v0,1
sw $v0,0xC62($28)#负数，则led灯亮
j begin_1

contine:

addi $a0,$zero,0
addi $t1,$zero,0
addi $t1,$t1,1

Loop1:
beq $t1,$s0,over1
add $a0,$a0,$t1
addi $t1,$t1,1

j Loop1


over1:

addi $s0,$zero,0
addi $s0,$s0,255

sltu $t4,$s0,$a0
beq $t4,$zero,noover

lui $v0,1
sw $v0,0xC68($28)#负数，则led灯亮


noover:
add $a0,$a0,$t1
sw $a0,0xC60($28)#展示和
sw $a0,0xC80($28)#展示和
j begin_1



####################################

test1_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test1_1
test1_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test1_2
lw $v0,0xC70($28)

addiu $s0,$v0,0 #s0保存a的值



j begin_1
####################################
test2_1:
lw $s7,0xC7C($28)#s7表示使能信号，拨下开关，开始计算
bne $s7,$zero,test2_1
test2_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test2_2
and $a0,$s0,$s1
sw $a0,0xC60($28)
j begin_1
####################################
test3_1:
lw $s7,0xC7C($28)#s7表示使能信号，拨下开关，开始计算
bne $s7,$zero,test3_1
test3_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test3_2
or  $a0,$s0,$s1
sw  $a0,0xC60($28)
j begin_1
####################################
test4_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test4_1
test4_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test4_2
lw $v0,0xC70($28)
addi $s0,$v0,0#s0保存a的值
sw  $s0,0xC60($28)

sll $t3, $s0, 24
srl $t3, $t3, 31
beq $t3, $zero, inputb4_1

lui $t6, 0xFFFF
ori $t6, $t6, 0xFF00
or $s0, $s0, $t6

inputb4_1:
lw $s7,0xC7C($28)#s7表示使能信号，输入完a后，拨上拨码，从板子读取b的值，接着需要拨下开关，继续读取b
bne $s7,$zero,inputb4_1
inputb4_2:
lw $s7,0xC7C($28)
beq $s7,$zero,inputb4_2
lw $v0,0xC70($28)
addi $s1,$v0,0#s1存放b的值

sll $t3, $s1, 24
srl $t3, $t3, 31
beq $t3, $zero, test4_b_not1

lui $t6, 0xFFFF
ori $t6, $t6, 0xFF00
or $s1, $s1, $t6

test4_b_not1:
add $a0,$s1,$s0
sw  $a0,0xC80($28)
sw  $a0,0xC60($28)

sll $t0,$s0,24
srl $t1,$t0,31

sll $t0,$s1,24
srl $t2,$t0,31

add $t2,$t2,$t1
addi $t2,$t2,-1
beq $t2,$zero,no2


sll $t0,$a0,24
srl $t2,$t0,31

beq $t1,$t2,no2

lui $v0,1
sw $v0,0xC68($28)#溢出，则led灯亮
j begin_1

no2:
lui $v0,0
sw $v0,0xC68($28)#未溢出，则led灯不亮

j begin_1
####################################
test5_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test5_1
test5_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test5_2
lw $v0,0xC70($28)
addi $s0,$v0,0#s0保存a的值

##符号拓展
sll $t3, $s0, 24
srl $t3, $t3, 31
beq $t3, $zero, inputb5_1

lui $t6, 0xFFFF
ori $t6, $t6, 0xFF00
or $s0, $s0, $t6

inputb5_1:
lw $s7,0xC7C($28)#s7表示使能信号，输入完a后，拨上拨码，从板子读取b的值，接着需要拨下开关，继续读取b
bne $s7,$zero,inputb5_1
inputb5_2:
lw $s7,0xC7C($28)
beq $s7,$zero,inputb5_2
lw $v0,0xC70($28)
addi $s1,$v0,0#s1存放b的值

##符号拓展
sll $t3, $s1, 24
srl $t3, $t3, 31
beq $t3, $zero, test5_sign_extend_not

lui $t6, 0xFFFF
ori $t6, $t6, 0xFF00
or $s1, $s1, $t6


test5_sign_extend_not:
sub $a0,$s0,$s1
sw  $a0,0xC80($28)
sw  $a0,0xC60($28)

sll $t0,$s0,24
srl $t1,$t0,31

sll $t0,$s1,24
srl $t2,$t0,31

add $t2,$t2,$t1
addi $t2,$t2,-1
beq $t2,$zero,check

lui $v0,0
sw $v0,0xC68($28)#未溢出，则led灯不亮
j begin_1

check:
sll $t0,$a0,24
srl $t2,$t0,31
beq $t2,$t1,no5


lui $v0,1
sw $v0,0xC68($28)#溢出，则led灯亮
j begin_1

no5:
lui $v0,0
sw $v0,0xC68($28)#未溢出，则led灯不亮

j begin_1
####################################
test6_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test6_1
test6_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test6_2
lw $v0,0xC70($28)
addi $s0,$v0,0#s0保存a的值

##符号拓展
##sll $t3, $s0, 24
##srl $t3, $t3, 31
##beq $t3, $zero, inputb6_1

##lui $t6, 0xFFFF
##ori $t6, $t6, 0xFF00
##or $s0, $s0, $t6

inputb6_1:
lw $s7,0xC7C($28)#s7表示使能信号，输入完a后，拨上拨码，从板子读取b的值，接着需要拨下开关，继续读取b
bne $s7,$zero,inputb6_1
inputb6_2:
lw $s7,0xC7C($28)
beq $s7,$zero,inputb6_2
lw $v0,0xC70($28)
addi $s1,$v0,0 #s1存放b的值

add $s6, $zero, $zero
add $s7, $zero, $zero

sll $t7, $s0, 24
srl $t7, $t7, 31
beq $t7, $zero, not_jump_1

addi $s6, $s6, 1
lui $t9, 0xFFFF
ori $t9, $t9, 0xFF00
or $s0, $t9, $s0
sub $s0, $zero, $s0

not_jump_1:
sll $t7, $s1, 24
srl $t7, $t7, 31
beq $t7, $zero, not_jump_2

addi $s7, $s7 ,1
lui $t9, 0xFFFF
ori $t9, $t9,0xFF00
or $s1, $t9, $s1
sub $s1, $zero, $s1

not_jump_2:

addi $t3,$zero,7
addi $t4,$zero,0

sll $s0,$s0,7

loop:
sll $t0,$s1,31
srl $t1,$t0,31
beq $t1,$zero,skip
add $s1,$s1,$s0
skip:
srl $s1,$s1,1
addi $t4,$t4,1
bne $t4,$t3,loop

beq $s6, $s7, Exit
sub $s1, $zero, $s1

Exit:

sw  $s1,0xC80($28)


j begin_1
####################################
test7_1:
lw $s7,0xC7C($28)
bne $s7,$zero,test7_1
test7_2:
lw $s7,0xC7C($28)
beq $s7,$zero,test7_2
lw $v0,0xC70($28)
addi $s0,$v0,0 #s0保存a的值
sw $s0,0xC60($28)
##符号拓展
##sll $t3, $s0, 24
##srl $t3, $t3, 31
#beq $t3, $zero, inputb7_1

#lui $t6, 0xFFFF
#ori $t6, $t6, 0xFF00
#or $s0, $s0, $t6

inputb7_1:
lw $s7,0xC7C($28)#s7表示使能信号，输入完a后，拨上拨码，从板子读取b的值，接着需要拨下开关，继续读取b
bne $s7,$zero,inputb7_1
inputb7_2:
lw $s7,0xC7C($28)
beq $s7,$zero,inputb7_2
lw $v0,0xC70($28)
addi $s1,$v0,0 #s1存放b的值
sw $s1,0xC60($28)

##符号拓展
#sll $t3, $s1, 24
#srl $t3, $t3, 31
#beq $t3, $zero, sign_extend_test7

#lui $t6, 0xFFFF
#ori $t6, $t6, 0xFF00
#or $s1, $s1, $t6


add $s6, $zero, $zero
add $s7, $zero, $zero

sll $t7, $s0, 24
srl $t7, $t7, 31
beq $t7, $zero, not_jump_3

addi $s6, $s6, 1
lui $t9, 0xFFFF
ori $t9, $t9, 0xFF00
or $s0, $t9, $s0
sub $s0, $zero, $s0

not_jump_3:
sll $t7, $s1, 24
srl $t7, $t7, 31
beq $t7, $zero, not_jump_4

addi $s7, $s7 ,1
lui $t9, 0xFFFF
ori $t9, $t9,0xFF00
or $s1, $t9, $s1
sub $s1, $zero, $s1

not_jump_4:

addi $t3,$zero,9
addi $t4,$zero,0
addi $a0,$zero,0

sll $s1,$s1,8

loop7:
slt $t0,$s0,$s1
sll $a0,$a0,1
bne $t0,$zero,skip1
sub $s0,$s0,$s1
addi $a0,$a0,1
skip1:
srl $s1,$s1,1
addi $t4,$t4,1
bne $t3,$t4,loop7

beq $s6, $s7, Exit1
sub $a0, $zero, $a0

Exit1:
beq $s6, $zero,Exit2
sub $s0, $zero, $s0

Exit2:  

sw  $a0,0xC80($28)
add $t8, $zero, $zero
addi $t8, $t8, 30000
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8
add $t8, $t8, $t8


add $t9, $zero, $zero

run1:
addi $t9, $t9, 1
beq $t9, $t8, next_number
j run1

next_number:
sw  $s0,0xC80($28)
add $t9, $zero, $zero

run2:
addi $t9, $t9, 1
beq $t9, $t8, Exit2
j run2