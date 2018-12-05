package Game;

import Game.Shape.Tetrominoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board extends JPanel implements ActionListener, KeyListener {

    double[] genome;//for equation a*total height + b*lines cleared + c*holes + d*wells

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private int DELAY = 400;

    public boolean DATA_VIEW = false;
    public boolean GRID_VIEW = false;
    public boolean COMPUTER = false;

    private Timer timer;
    private boolean isFallingFinished = false;
    public boolean isStarted = false;
    private boolean isPaused = false;
    public int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Tetris parent;
    public Shape curPiece;
    private Tetrominoe[] board;

    public Board(Tetris parent, boolean computer, int delay, double[] genome) {
        //GRID_VIEW = computer;
        //DATA_VIEW = computer;
        this.genome = genome;
        DELAY = delay;
        COMPUTER = computer;
        initBoard(parent);
    }

    private void initBoard(Tetris parent) {

        setFocusable(true);
        curPiece = new Shape();

            timer = new Timer(DELAY, this);
            timer.start();

        this.parent = parent;
        statusbar =  this.parent.getStatusBar();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];

        addKeyListener(this);
        clearBoard();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if(keycode=='R')
            restart();
        if (!isStarted || curPiece.getShape() == Tetrominoe.NoShape)
            return;

        if (keycode == 'P') {
            pause();
            return;
        }
        if (isPaused)
            return;
        switch (keycode) {
            case KeyEvent.VK_LEFT:
                tryMove(curPiece, curX - 1, curY);
                break;
            case KeyEvent.VK_RIGHT:
                tryMove(curPiece, curX + 1, curY);
                break;
            case KeyEvent.VK_DOWN:
                tryMove(curPiece.rotateRight(), curX, curY);
                break;
            case KeyEvent.VK_UP:
                tryMove(curPiece.rotateLeft(), curX, curY);
                break;
            case KeyEvent.VK_SPACE:
                dropDown();
                break;
            case KeyEvent.VK_M:
                makeBestMove();
                break;


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(COMPUTER)
            makeBestMove();
        else {
            if (isFallingFinished) {
                isFallingFinished = false;
                newPiece();
            } else
                oneLineDown();
        }
    }

    private int squareWidth() { return (int) getSize().getWidth() / BOARD_WIDTH; }
    private int squareHeight() { return (int) getSize().getHeight() / BOARD_HEIGHT; }
    private Tetrominoe shapeAt(int x, int y) { return board[(y * BOARD_WIDTH) + x]; }

    public void start()  {

        if (isPaused)
            return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    private void pause()  {

        if (!isStarted)
            return;

        isPaused = !isPaused;

        if (isPaused) {
                timer.stop();
            statusbar.setText("paused");
            System.out.println(genomeToString()+"\n"+boardToString()+"\n"+swapToString());
        } else {
                timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }

        repaint();
    }

    private class Move implements Comparable<Move>{
        public double score;
        public int xPos;
        public int rot;

        public Move(double score, int xPos, int rot) {
            this.score = score;
            this.xPos = xPos;
            this.rot = rot;
        }

        @Override
        public String toString() {
            return score + " at " + xPos;
        }

        @Override
        public int compareTo(Move o) {
            if(score==o.score)
                return 0;
            return (score-o.score<0)?1:-1;
        }
    }

    public void makeBestMove(){

        ArrayList<Move> scores = new ArrayList<>();

        curY--;
        for(int i=0;i<4;i++) {
            while (tryMove(curPiece, curX - 1, curY)) ;
            scores.add(new Move(fitness(simDropped(curPiece, curX)), curX, i));
            while (tryMove(curPiece, curX + 1, curY)) {
                scores.add(new Move(fitness(simDropped(curPiece, curX)), curX, i));
            }
            tryMove(curPiece, BOARD_WIDTH / 2, curY);
            curPiece=curPiece.rotateRight();
        }

        Collections.sort(scores);
        for(int i=0;i<scores.get(0).rot;i++)
            curPiece=curPiece.rotateRight();

        tryMove(curPiece,scores.get(0).xPos,curY);
        dropDown();
    }

    public double fitness(int[][] data){
        return (genome[0]* getTotalBlocks(data))+(genome[1]*getLinesCleared(data))+(genome[2]*getHoleCount(data))+(genome[3]*getWells(data));
    }

    public void doDrawing(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; ++i) {

            for (int j = 0; j < BOARD_WIDTH; ++j) {

                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Tetrominoe.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                else if(DATA_VIEW)
                    g.drawString("0",0 + j * squareWidth()+6,boardTop + i * squareHeight()-2);
            }
        }

        if (curPiece.getShape() != Tetrominoe.NoShape) {

            for (int i = 0; i < 4; ++i) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
        if(GRID_VIEW) {
            for (int col = 0; col < getSize().getWidth(); col += squareWidth()) {
                g.drawLine(col, squareHeight(), col, (1+BOARD_HEIGHT)*squareHeight());
            }
            for (int row = -1; row <= getSize().getHeight(); row += squareHeight())
                g.drawLine(0, row+squareHeight(), BOARD_WIDTH*squareWidth(), row+squareHeight());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void dropDown() {
        int newY = curY;

        while (newY > 0)
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            else
                --newY;

        pieceDropped();
    }

    private void oneLineDown()  {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i)
            board[i] = Tetrominoe.NoShape;
    }

    private int[][] simDropped(Shape shape, int xPos){
        int[][] newData = getBoardData();


        int x=xPos, yPos=curY;
        while(yPos>0)
            if(!canPlace(shape,x,yPos)) {
                break;
            }
            else
                --yPos;
            yPos++;
        for (int i = 0; i < 4; ++i) {
            x = xPos + curPiece.x(i);
            int y = yPos - curPiece.y(i);
            newData[y][x]=1;
        }

        return newData;
    }

    private boolean canPlace(Shape shape, int xPos, int yPos){
        for (int i = 0; i < 4; ++i) {
            int x = xPos + shape.x(i);
            int y = yPos - shape.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;
            if (shapeAt(x, y) != Tetrominoe.NoShape)
                return false;
        }
        return true;
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished)
            newPiece();
    }

    private void newPiece()  {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH/2;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        statusbar.setText(numLinesRemoved+" lines, "+fitness(getBoardData())+" fitness");


        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Tetrominoe.NoShape);
            timer.stop();
            isStarted = false;
            statusbar.setText("Score: "+numLinesRemoved);
        }

    }

    public boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;

            if (shapeAt(x, y) != Tetrominoe.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();
        return true;
    }

    /**
     * Starts at the bottom of the board and checks if there are any empty boxes in each row. If there aren't then it adds
     * one to the number of lines full
     */
    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; ++j)
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; ++k)
                    for (int j = 0; j < BOARD_WIDTH; ++j)
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText(numLinesRemoved + " lines cleared");
            if(!COMPUTER)
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
            repaint();
        }
    }

    public int[][] getBoardData(){
        int[][] data = new int[BOARD_HEIGHT][BOARD_WIDTH];

        for(int i=0;i<data.length;i++)
            for(int j=0;j<data[i].length;j++)
                if(board[(BOARD_WIDTH*i)+j].equals(Tetrominoe.NoShape))
                    data[i][j]=0;
                else
                    data[i][j]=1;
            return data;
    }

    public String boardToString(){
        String out="";
        for(int[] row:getBoardData()) {
            for (int box : row)
                out += "|" + box + "|";
            out += "\n";
        }
        return out;
    }

    public String dataToString(int[][] data){
        String out="";
        for(int[] row:data) {
            for (int box : row)
                out += "|" + box + "|";
            out += "\n";
        }
        return out;
    }

    public String swapToString(){
        String out="";
        for(int[] row:swapXY(getBoardData())) {
            for (int box : row)
                out += "|" + box + "|";
            out += "\n";
        }
        return out;
    }

    public String genomeToString(){
        return "Blocks: "+ getTotalBlocks(getBoardData())+"\tLines: "+numLinesRemoved+"\tHoles: "+getHoleCount(getBoardData())+"\tWell score: "+getWells(getBoardData());
    }

    public static boolean arrayHas(int[] arr, int i){
        for(int x: arr)
            if(x==i)
                return true;
            return false;
    }

    private int getWells(int[][] boardData) {
        int firstEmpty=0;
        int[][] data = boardData;
        for(int i=0;i<data.length;i++)
            if(!arrayHas(data[i],1)){
                firstEmpty = i;
                break;
            }
        return (firstEmpty*BOARD_WIDTH)-getHoleCount(boardData)-getTotalBlocks(boardData);
    }

    public static int arrSum(int[] arr){
        int total=0;
        for(int i:arr)
            total+=i;
        return total;
    }

    private int getHoleCount(int[][] boardData) {
        int[][] data = swapXY(boardData);
        int[] colHoleTotal = new int[BOARD_WIDTH];
        row:
        for(int j=0;j<data.length;j++)
            for(int i=data[j].length-1;i>=0;i--)
                if(data[j][i]==1){
                    colHoleTotal[j]=i;
                    continue row;
                }
                for(int i=0;i<colHoleTotal.length;i++)
                    colHoleTotal[i]+=1-arrSum(data[i]);
        return arrSum(colHoleTotal);
    }

    private int getLinesCleared(int[][] boardData){
        int lines=0;
        for(int[] row: boardData)
            if(arrSum(row)==BOARD_WIDTH)
                lines++;
            return lines+numLinesRemoved;
    }

    private int getTotalBlocks(int[][] boardData) {
        int total=0;
        for(int[] row:boardData)
                total+=arrSum(row);
        return total;
    }

    public static int[][] swapXY(int[][] array){
        int[][] swapped = new int[array[0].length][array.length];
        for(int i=0;i<array.length;i++)
            for(int j=0;j<array[0].length;j++)
                swapped[j][i]=array[i][j];
            return swapped;
    }

    private void restart(){
        parent.restart();
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape)  {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        if(!DATA_VIEW) {
            Color color = colors[shape.ordinal()];

            g.setColor(color);
            g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

            g.setColor(color.brighter());
            g.drawLine(x, y + squareHeight() - 1, x, y);
            g.drawLine(x, y, x + squareWidth() - 1, y);

            g.setColor(color.darker());
            g.drawLine(x + 1, y + squareHeight() - 1,
                    x + squareWidth() - 1, y + squareHeight() - 1);
            g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                    x + squareWidth() - 1, y + 1);
        } else {
            ((Graphics2D) g).drawString("1", x+6, y-2);
        }
    }

}