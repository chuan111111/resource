package model;

public class Click implements Runnable {

    public int x, y;

    public ClickController clickController; 

    @Override
    public void run() {
        System.err.printf("\u001b[32;1mClick (%d, %d) on the chess board.\u001b[0m\n", x, y );
        // clickController.click(x, y);
    } 
}
