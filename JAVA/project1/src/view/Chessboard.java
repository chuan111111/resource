package view;

import chessComponent.*;
import controller.GameController;
import model.Click;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {

    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    private final SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];

    // use it to know the current player 
    private GameController controller = new GameController(); 

    private void resetAllComponents() {
        for (int i = 0; i < ROW_SIZE; ++i) {
            for (int j = 0; j < COL_SIZE; ++j) {
                squareComponents[i][j].value = controller.chesses[i * COL_SIZE + j];
            }
        }
        for (int i = 0; i < ROW_SIZE; ++i) {
            for (int j = 0; j < COL_SIZE; ++j) {
                squareComponents[i][j].repaint();
            }
        }
    }

    public boolean isRedPlayer() {
        return controller.ops.size() % 2 == 0; 
    }

    public boolean isBlackPlayer() {
        return !isRedPlayer(); 
    }

    private final int CHESS_SIZE;

    public Chessboard(int width, int height) {

        // setLayout(null); // Use absolute layout.
        setLayout(new GridLayout(8, 4));
        
        // set prefer size, to make layout work 
        setPreferredSize(new Dimension(width + 2, height));

        CHESS_SIZE = (height - 6) / 8;

        SquareComponent.setSpacingLength(CHESS_SIZE / 12);

        for (int i = 0; i < ROW_SIZE; ++i) {
            for (int j = 0; j < COL_SIZE; ++j) {
                var sc = new SquareComponent(CHESS_SIZE);
                squareComponents[i][j] = sc; 
                add(sc);
                var handle = new Click(); 
                handle.x = i; 
                handle.y = j; 
                sc.handle = handle; 
            }
        }

        controller.chesses = controller.init();
        resetAllComponents();

    }

    /**
     * 绘制棋盘格子
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

}