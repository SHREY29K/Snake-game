import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;//This is going to be used for storing the segments of the snake's body
import java.util.Random; // Getting random values for x and y for the frame, to place the fruit's coordinates
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
  private class Tile{
    //This class is being created to track the coordinates of the frame window.
    int x, y;

    Tile(int x, int y){
      this.x = x;
      this.y = y;
    }
  }

  int boardWidth, boardHeight;
  int tileSize = 25;//This tile size is being defined as when the snake body is present on the window it moves a particular tile size-distance forward and not a pixel forward , so in reality the whole pixelated window is being defined into 25 rows and columns.

  //snake
  Tile snakeHead;//The snakehead would have a particularsize to it too, beside the coordinates that it would be provided with.
  ArrayList<Tile> snakeBody;

  //food
  Tile food;
  Random random; 

  //Game Logic
  Timer gameLoop;
  int velocityX;
  int velocityY;
  boolean gameOver = false;

  SnakeGame(int boardWidth, int boardHeight){
    this.boardHeight = boardHeight;
    this.boardWidth = boardWidth;
    setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
    setBackground(Color.black);
    addKeyListener(this);
    setFocusable(true);

    snakeHead = new Tile(5,5);
    snakeBody = new ArrayList<Tile>();

    food = new Tile(10, 10);
    random = new Random();
    placeFood();

    //postive velocity in x means that the snake is going towards the right, and positive velocity in Y means that the snake is going downwards.
    velocityX = 0;
    velocityY = 0;

    gameLoop = new Timer(100,this);//Every 100th millisecond the loop would go on to paint the frame again, for the snake to move around.
    //The ovverrid function actionListener repaints() the entire frame again and again, meaning it's just calling the draw() method again and again.
    gameLoop.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    //GRID
    for(int i=0; i<boardWidth/tileSize; i++) {
      //(x1,y1,x2,y2)
      g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);//drawing vertical lines so the y-coordinate changes
      g.drawLine(0, i*tileSize, boardWidth, i*tileSize);//drawing horizontal lines so the x-coordinate changes
    }

    //FOOD
    g.setColor(Color.red);
    g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

    //SNAKE HEAD
    g.setColor(Color.green);
    g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);

    //SNAKE BODY
    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);
      g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
    }

    //Score
    g.setFont(new Font("Arial",Font.BOLD,18));
    if (gameOver) {
      g.setColor(Color.red);
      g.drawString("Game Over !! Your Score was: "+String.valueOf(snakeBody.size()), tileSize-16, tileSize);
    }else{
      g.drawString("SCORE: "+String.valueOf(snakeBody.size()), tileSize-16, tileSize);
    }
  }

  public void placeFood(){
    //this will just set the food to a random spot on the grid that is not part of the snake
    food.x = random.nextInt(boardHeight/tileSize); //0-24
    food.y = random.nextInt(boardHeight/tileSize); //0-
  }

  public boolean collision(Tile tile1, Tile tile2){
    return tile1.x == tile2.x && tile1.y == tile2.y;
  }

  public void move(){
    //eat food
    if (collision(snakeHead, food)) {
      snakeBody.add(new Tile(food.x, food.y));
      placeFood();
    }

    //Going to move all the tiles bwfore i move the snake head, if the head is moved first then the next tile which is the first member of the snake's body will not know where to move as we're following the snake head, hence iteration needs to be done in a reverse direction such that each tile needs to catch up one before it, before the snakeHead can move.

    //SNAKE BODY
    for(int i=snakeBody.size()-1; i>=0; i--) {
      Tile snakePart = snakeBody.get(i);
      if(i==0){
        snakePart.x = snakeHead.x;
        snakePart.y = snakeHead.y;
      }else{
        Tile prevSnakePart = snakeBody.get(i-1);
        snakePart.x = prevSnakePart.x;
        snakePart.y = prevSnakePart.y;
      }
    }

    //SnakeHead
    snakeHead.x += velocityX; 
    snakeHead.y += velocityY;

    //game over conditons
    for (int i = 0; i < snakeBody.size(); i++) {
      Tile snakePart = snakeBody.get(i);
      //collide with the snake head
      if (collision(snakeHead, snakePart)) {
        gameOver = true;
      }
    }

    if (snakeHead.x*tileSize <0 || snakeHead.x*tileSize>boardWidth || snakeHead.y*tileSize <0 || snakeHead.y*tileSize > boardHeight) {    
      gameOver = true;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if(gameOver) {
      gameLoop.stop();
      JOptionPane.showMessageDialog(null,"Game Over!!","Thanks For Playing",JOptionPane.INFORMATION_MESSAGE);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){
      velocityX = 0;
      velocityY = -1;
    }
    else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1){
      velocityX = 0;
      velocityY = 1;
    }
    else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
      velocityX = -1;
      velocityY = 0;
    }
    else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1) {
      velocityX = 1;
      velocityY = 0;
    }

  }

  //We only need to define these two methods there is no need to use them
  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
