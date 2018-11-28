package AI2;

import Game.Tetris;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Tetris game = new Tetris(true);
        game.setVisible(true);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Right);
        game.nextLine();
        TimeUnit.SECONDS.sleep(10);
        game.move(Tetris.Move.Drop);
        game.nextLine();

    }
}
