package controller;

public class Operator {
    public int index1, index2; 
    public Operator(int index1, int index2) {
        if (index1 < 0 || index1 > 32) {
            throw new RuntimeException("Invalid index of chess"); 
        }
        if (index2 < 0 || index2 > 32) {
            throw new RuntimeException("Invalid index of chess"); 
        }
        this.index1 = index1; 
        this.index2 = index2; 
    }
}
