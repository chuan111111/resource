package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类表示游戏窗体，窗体上包含：
 * 1 Chessboard: 棋盘
 * 2 JLabel:  标签
 * 3 JButton： 按钮
 */
public class ChessGameFrame extends JFrame {

    public final int getChessBoardSize() {
        return getWidth() * 4 / 5; 
    }

    private GameController gameController;
    
    private static JLabel statusLabel;
    private static JLabel redpointsLabel;
    private static JLabel blackpointLaber;
    private static JLabel processLaber;

    public ChessGameFrame() { 
        // set the title, hard codes 
        setTitle("2022 CS109 Project Demo"); 

        // set the size 
        setSize(720, 720); 

        // Center the window.
        setLocationRelativeTo(null); 

        // set the back ground 
        getContentPane().setBackground(Color.WHITE);

        // close the program 
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        
        // remove the layout manager  
        // setLayout(null);

        // use the normal layout 
        // setLayout(new BorderLayout());

        setLayout(new FlowLayout());

        // get chess board 
        {

        }

        addChessboard();
        addLabel();
        addHelloButton();
        addLoadButton();
        addfiledButton();
        addLabel2();
        addLabel3();
        addprocessLabel();
    }


    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard() {
        var size = getChessBoardSize(); 
        Chessboard chessboard = new Chessboard(size / 2, size);
        gameController = new GameController();
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        add(chessboard);
    }

    // private void chess

    /**
     * 在游戏窗体中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("BLACK's TURN");
        statusLabel.setLocation(WIDTH * 3 / 5, HEIGHT / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    private void addLabel2(){
        blackpointLaber = new JLabel("BLACK's POINTS");
        blackpointLaber.setLocation(WIDTH * 3 / 5, HEIGHT* 6/ 10);
        blackpointLaber.setSize(200, 60);
        blackpointLaber.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(blackpointLaber);

    }
    private void addLabel3(){
        redpointsLabel = new JLabel("RED's POINTS");
        redpointsLabel.setLocation(WIDTH * 3 / 5, HEIGHT* 7/ 10);
        redpointsLabel.setSize(200, 60);
        redpointsLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(redpointsLabel);
    }

    private void addprocessLabel(){
        processLaber = new JLabel("In process");
        processLaber.setLocation(WIDTH * 3 / 5, HEIGHT* 2/ 10);
        processLaber.setSize(200, 60);
        processLaber.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(processLaber);
    }
    public static JLabel getStatusLabel() {
        return statusLabel;
    }


    public static JLabel getRedpointsLabel() {
        return redpointsLabel;
    }

    public static JLabel getBlackpointLaber() {
        return blackpointLaber;
    }

    public static JLabel getProcessLaber() {
        return processLaber;
    }

    /**
     * 在游戏窗体中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "Restart");
            //TODO 
            // gameController.restartgame();
        });
        button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 120);
        button.setSize(180, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

   private void addfiledButton(){
       JButton button = new JButton("filed");
       button.setLocation(WIDTH * 3 / 5, HEIGHT / 10 + 240);
       button.setSize(180, 60);
       button.setFont(new Font("Rockwell", Font.BOLD, 20));
       button.setBackground(Color.LIGHT_GRAY);
       add(button);

       button.addActionListener(e -> {
           System.out.println("load");
           String path = JOptionPane.showInputDialog(this, "Input Path here");
           gameController.writeFileByFileWriter(path);
       });
   }
    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(WIDTH * 3 / 5, HEIGHT*2 / 10 + 240);
        button.setSize(180, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this, "Input Path here");

            if (!path.endsWith("txt")){
                JOptionPane.showMessageDialog(this,"Wrong File Path");
            }else {
            }
        });
    }
}
