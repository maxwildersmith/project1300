package Game;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private JLabel statusbar;

    public Tetris() {

        initUI();
    }

    private void initUI() {
        statusbar = new JLabel("Start!");
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusbar, BorderLayout.SOUTH);

        Board board = new Board(this);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }

    public void restart(){
        Tetris game = new Tetris();
        game.setVisible(true);
        Tetris.this.dispose();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}