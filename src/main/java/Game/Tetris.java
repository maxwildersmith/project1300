package Game;

import AI2.Individual;
import Genetics.Evolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tetris extends JFrame {

    private JLabel statusbar;
    private JMenuBar menuBar;
    private Board board;
    private JMenu view, file;
    private JCheckBoxMenuItem debug, grid;

    private boolean comp, gameDone=false;
    private int timer;
    private ActionListener parent;


    public Tetris(boolean computer, int delay, double[] genome, int x, int y, ActionListener parent, long seed){
        initUI(computer, delay,genome, x,y, seed);
        comp = computer;
        timer=delay;
        this.parent = parent;
    }

    public void gameDone(){
        if(gameDone)
            System.err.println("GAME FINISHED BUT ALREADY DONE!");
        gameDone=true;
        parent.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,board.numLinesRemoved+""));
    }

    public boolean isGameDone(){
        return gameDone;
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
                board.repaint();
            }
        });

        grid = new JCheckBoxMenuItem("Grid");
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.GRID_VIEW = grid.getState();
                board.repaint();
            }
        });

        view.add(grid);
        view.addSeparator();
        view.add(debug);

        menuBar.add(file);
        menuBar.add(view);

        setJMenuBar(menuBar);
    }

    private void initUI(boolean computer, int delay, double[] genome, int x,int y, long seed) {
        setResizable(false);

        statusbar = new JLabel("Start!");
        statusbar.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusbar, BorderLayout.SOUTH);

        board = new Board(this, computer, delay, genome, seed);
        add(board);
        board.start();

        setAlwaysOnTop(true);
        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(x,y);
        initMenu();
        setVisible(true);
    }

    public void nextLine(){
        board.actionPerformed(new ActionEvent("sd",0,"thing"));
    }

    public int getScore(){
        return board.numLinesRemoved;
    }

    public double[] getGenome(){
        return board.genome;
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
        System.out.println("restart");
//        board = new Board(this, comp, timer, new double[]{-.5,.76,-.35,-.18});
        gameDone=false;
        board.start();
    }
    public void restart(double[] genome,long seed){
//        board = new Board(this, comp, timer, new double[]{-.5,.76,-.35,-.18});
        board.genome = genome;
        board.rand = new Random(seed);
        gameDone=false;
        board.start();

    }
}