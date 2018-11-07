package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tetris extends JFrame {

    private JLabel statusbar;
    private JMenuBar menuBar;
    private Board board;
    private JMenu view, file;
    private JCheckBoxMenuItem debug, grid;

    public Tetris() {
        initUI();
    }

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

    private void initUI() {

        setResizable(false);

        statusbar = new JLabel("Start!");
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusbar, BorderLayout.SOUTH);

        board = new Board(this);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initMenu();
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