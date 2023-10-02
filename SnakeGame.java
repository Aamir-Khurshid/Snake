import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardheight;
    int boardwidth;
    int tilesize = 25;

    Tile snakehead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;

    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardwidth,int boardheight){
        this.boardwidth = boardwidth;
        this.boardheight = boardheight;
        setPreferredSize(new Dimension(this.boardwidth,this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakehead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();   

        velocityX = 0;
        velocityY = 0;

        gameloop = new Timer(100, this);
        gameloop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        
        g.setColor(Color.red);
        g.fill3DRect(food.x*tilesize,food.y*tilesize,tilesize,tilesize,true);
        g.setColor(Color.green);
        g.fill3DRect(snakehead.x*tilesize,snakehead.y*tilesize,tilesize,tilesize,true);

        for (int i =0;i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tilesize,snakePart.y*tilesize,tilesize,tilesize,true);
        }

        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: "+ String.valueOf(snakeBody.size()),tilesize -16,tilesize);
        }
        else{
            g.drawString("Score: "+ String.valueOf(snakeBody.size()),tilesize -16,tilesize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardwidth/tilesize);
        food.y = random.nextInt(boardheight/tilesize);
    }

    public boolean collision(Tile tile1,Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){

        if (collision(snakehead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }
        
        for(int i = snakeBody.size()-1;i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakehead.x;
                snakePart.y = snakehead.y;
            }
            else{
                Tile prevSnakepart = snakeBody.get(i-1);
                snakePart.x = prevSnakepart.x;
                snakePart.y = prevSnakepart.y;
            }
        }

        snakehead.x += velocityX;
        snakehead.y += velocityY;

        for(int i =0; i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            if (collision(snakehead,snakePart)){
                gameOver = true;
            }
        }
        if(snakehead.x*tilesize<0 || snakehead.x*tilesize>boardwidth||snakehead.y*tilesize<0 || snakehead.y*tilesize>boardheight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT  && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT  && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){}

}