import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;//pixels
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);//Here the frame of the window is set to the mentioned board width and height.
        frame.setLocationRelativeTo(null);//So that the window stays in the center of the screen and doesn't move, later we can change this too.
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//the frame is to be shut down once the close window button is clicked

        //A JPanel is used to draw on the frame that we have created, but rather than creating a JPanel directly, a class that inherits 'JPanel' is to be created.

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
