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
        for(char[] row: board)
            if(row[0]==row[1]&&row[1]==row[2])
                return true;
            for(int i=0;i<3;i++)
                if(board[0][i]==board[1][i]&&board[1][i]==board[2][i])
                    return true;
                return (board [0][0]==board[1][1]&& board [1][1]==board[2][2])||(board [0][2]==board[1][1]&& board [1][1]==board[2][0]);
    }

    public boolean checkRowWin(){
        for(char[] c: board)
            if(c[0]==c[1]&&c[1]==c[2])
                return true;
        return false;
    }

    public void print(){
        for(char[] row : board){
            System.out.printf(" %c | %c | %c \n-----------\n",row[0],row[1],row[2]);

        }

    }
}
