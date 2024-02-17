package controller;

public interface Rule {
    boolean testOperator(int[] chesses, Operator op);
}
