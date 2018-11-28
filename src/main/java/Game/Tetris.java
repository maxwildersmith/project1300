package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Tetris extends JFrame {

    private JLabel statusbar;
    private JMenuBar menuBar;
    private Board board;
    private JMenu view, file;
    private JCheckBoxMenuItem debug, grid;

    public Tetris() {
        initUI(false);
    }
    public Tetris(boolean computer){
        initUI(computer);
    }

    public enum Move{Right, Left, RotateL, RotateR, Drop};

    private void initMenu(){
        menuBar = new JMenuBar();
        view = new JMenu("View");
        file = new JMenu("File");
        debug = new JCheckBoxMenuItem("Debug");
        debug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.DATA_VIEW = debug.getState();
            }
        });

        grid = new JCheckBoxMenuItem("Grid");
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.GRID_VIEW = grid.getState();
            }
        });

        view.add(grid);
        view.addSeparator();
        view.add(debug);

        menuBar.add(file);
        menuBar.add(view);

        setJMenuBar(menuBar);
    }

    private void initUI(boolean computer) {

        setResizable(false);

        statusbar = new JLabel("Start!");
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusbar, BorderLayout.SOUTH);

        board = new Board(this, computer);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initMenu();
    }

    public void nextLine(){
        board.actionPerformed(new ActionEvent("sd",0,"thing"));
    }

    public int getScore(){
        return board.numLinesRemoved;
    }

    public void move(Move dir){
        switch (dir){
            case Left:
                board.keyPressed(new KeyEvent(this,0,10,0,KeyEvent.VK_LEFT));
                break;
            case Right:
                board.keyPressed(new KeyEvent(this,0,10,0,KeyEvent.VK_RIGHT));
                break;
            case RotateR:
                board.keyPressed(new KeyEvent(this,0,10,0,KeyEvent.VK_DOWN));
                break;
            case RotateL:
                board.keyPressed(new KeyEvent(this,0,10,0,KeyEvent.VK_UP));
                break;
            case Drop:
                board.keyPressed(new KeyEvent(this,0,10,0,KeyEvent.VK_SPACE));
                break;
        }
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