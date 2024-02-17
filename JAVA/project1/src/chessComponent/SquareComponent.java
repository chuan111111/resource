package chessComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 这个类是一个抽象类，主要表示8*4棋盘上每个格子的棋子情况。
 * 有两个子类：
 * 1. EmptySlotComponent: 空棋子
 * 2. ChessComponent: 表示非空棋子
 */
public class SquareComponent extends JComponent {

    private static final Color squareColor = new Color(250, 220, 190);
    protected static int spacingLength; 
    protected static final Font CHESS_FONT = new Font("宋体", Font.BOLD, 36);
    
    public static void setSpacingLength (int length ) {
        spacingLength = length; 
    }

    public Runnable handle; 
    public int value; 
    public boolean select; 

    public SquareComponent(int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        // setLocation(location);
        setSize(size, size);
    }

    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        // release ! 
        if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            handle.run(); 
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(squareColor);
        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        if (value != 0) {
            printChessTextAndMargin(g);
        }
    }

    private void printChessTextAndMargin(Graphics g) {

        // fill the background 
        g.setColor(Color.ORANGE);
        g.fillOval(spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength);

        // draw margin 
        g.setColor(Color.DARK_GRAY);
        g.drawOval(spacingLength, spacingLength, getWidth() - 2 * spacingLength, getHeight() - 2 * spacingLength);
        if (value > 10 || value < -10) {
            return ; 
        }

        // set the color of font 
        Color color; 
        if (value > 0) {
            color = Color.RED; 
        } else {
            color = Color.BLACK; 
        }
        g.setColor( color ); 

        g.setFont(CHESS_FONT);

        String name; 
        switch (value) {
            case 1: 
                name = "兵"; 
                break; 
            case -1: 
                name = "卒"; 
                break; 
            case 2: 
                name = "炮"; 
                break; 
            case -2: 
                name = "砲"; 
                break; 
            case 3: 
                name = "傌"; 
                break;
            case -3: 
                name = "馬"; 
                break; 
            case 4: 
                name = "俥";
                break; 
            case -4: 
                name = "車";
                break; 
            case 5: 
                name = "相";
                break; 
            case -5: 
                name = "象";
                break; 
            case 6: 
                name = "仕";
                break; 
            case -6: 
                name = "士";
                break; 
            case 7:
                name = "帥";
                break; 
            case -7: 
                name = "將";
                break; 
            default: 
                throw new RuntimeException("Invalid value of the piece: " + value ); 
        }

        g.drawString(name, this.getWidth() / 4, this.getHeight() * 2 / 3);

        if (select ) {
            g.setColor(Color.RED);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4f));
            g2.drawOval(spacingLength, spacingLength, getWidth() - 2 * spacingLength, getHeight() - 2 * spacingLength);
        }
    }
}
