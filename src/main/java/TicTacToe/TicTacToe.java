package TicTacToe;

public class TicTacToe {

    char[][] board;
    public TicTacToe(){
        board = new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    }

    public boolean play(int x, int y, char c){
        if(board[y][x] == ' ')
            board[y][x] = c;
        else
            return false;
        return true;
    }

    public boolean checkWin(){
        return true;
    }

    public void print(){
        for(char[] row : board){
            System.out.printf(" %c | %c | %c \n-----------\n",row[0],row[1],row[2]);

        }

    }
}
