package controller;

public class Utils {
    public static final Rule moveRule = new Rule() {
        @Override
        public boolean testOperator(int[] chesses, Operator op) {
            if (chesses[op.index2] == 0 && GameController.getDis(op.index1, op.index2) == 1) {
                return true; 
            }
            return false; 
        }
    }; 
    public static final Rule flipRule = (chesses, op) -> {
        if (op.index1 == op.index2) {
            if (chesses[op.index1] < -10 || chesses[op.index1] > 10) {
                return true; 
            }
        }
        return false; 
    }; 
}
