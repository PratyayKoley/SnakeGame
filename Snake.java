import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Random;

public class Snake
{
    public static void main(String[] args) {
       
        new GameFrame();
    }
}

class GameFrame extends JFrame{

    GameFrame(){
        this.add(new GamePanel());          //creating an instance of class GamePanel
        this.setTitle("Snake Game");        //setting the title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //Closes the game on hitting close
        this.setResizable(false);
        this.pack();                            //fits the JFrame snugly around the components of the frame
        this.setVisible(true);                  //sets the visibility to the user
        this.setLocationRelativeTo(null);       //sets the window to the center of the screen
    }
}

class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;        //how big the elements will be on the window    
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];    //includes all the x cooridnates of the Snake
    final int y[] = new int[GAME_UNITS];    //includes all the y coordinates of the Snake
    int bodyParts = 6;                      //initial amount of bodyParts
    int applesEaten;                        //initially apples eaten are 0
    int appleX;                             //contains the x positioning of the apple
    int appleY;                             //contains the y positioning of the apple
    char direction = 'R';                   //initially set the snake to move to right  
    boolean running = false;                //initially set the snake to not moving
    Timer timer;                            //sets the timer
    Random random;  

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));               //sets the size for the game panel
        this.setBackground(Color.blue);                                                 //sets the background of the window screen
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame()     //starts the game
    {
        newApple();                 //creates a new apple on the screen
        running = true;             //runs the snake
        timer = new Timer(DELAY,this);        //creates instance of Timer class and delays it
        timer.start();                  //starts the timer
    }

    public void paintComponent(Graphics g)     //paints the components on the window 
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)        //draws the grid , snake , apple and other components on the screen  
    {   
        if(running)
        {
            /*
            for(int i = 0 ; i < SCREEN_HEIGHT/UNIT_SIZE ; i++)          //draws a grid on the window 
            {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);     //draws the grid along x axis
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);      //draws the grid along y axis
            }
            */
            g.setColor(Color.green);            //sets the color of the apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);       //sets the shape and size of the random apple generated 

            for(int i = 0 ; i < bodyParts ; i++)        //draws the head and bodyparts of the snake
            {
                if(i==0)        //at the start of game head appears green
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE,  UNIT_SIZE);
                }
                else            //other bodyparts
                {
                    g.setColor(Color.CYAN);
                    g.fillRect(x[i], y[i], UNIT_SIZE,  UNIT_SIZE);
                }
            }
            //displays the score of the user
            g.setColor(Color.YELLOW);       //sets the color of the text
            g.setFont(new Font("Times New Roman" , Font.BOLD , 50));   //sets the font style and size of the text
            FontMetrics metrics = getFontMetrics(g.getFont());      //gets the font from setFont method
            g.drawString("Score : "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : "+applesEaten))/2, g.getFont().getSize());    //places the string at the top of the window
        }
        else
        {
            gameOver(g);
        }
    }

    public void newApple()          //generates the new coordinates for the apple using random class
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move()              //moving the snake
    {
        for(int i = bodyParts ; i > 0 ; i--)    //shifts the bodyparts of the snake
        {
            x[i] = x[i-1];          //shifting the x coordinates of snake
            y[i] = y[i-1];          //shifting the y coordinates of snake
        }

        switch(direction)
        {
            case 'U':       //moves the snake upwards
                y[0] = y[0] - UNIT_SIZE;      //up deals with the y coordinates
                break;
            case 'D':       //moves the snake downwards
                y[0] = y[0] + UNIT_SIZE;      //down deals with the y coordinates
                break;
            case 'L':       //moves the snake leftwards 
                x[0] = x[0] - UNIT_SIZE;      //left deals with the x coordinates
                break;
            case 'R':       //moves the snake rightwards
                x[0] = x[0] + UNIT_SIZE;      //right deals with the x coordinates
                break;             
        }       
    }

    public void checkApple()        //snake eats the apple
    {
        if((x[0] == appleX) && (y[0] == appleY))    //head of snake and apple collides that is snake eats the apple
        {
            bodyParts++;    //increase the size of snake
            applesEaten++;  //increase the number of applesEaten
            newApple();     //create a new apple at new position using random class
        }
    }

    public void checkCollisions()       //snake collides with body or walls
    {
        //checks if snake collides with its own body
        for(int i = 1 ; i < bodyParts ; i++)    //loops through all the bodyparts
        {
            if((x[0] == x[i]) && (y[0] == y[i]))     //if the x[0]/y[0](head) touches x[i]/y[i](any BodyPart) 
            {
                running = false;        //running stops 
            }
        }

        //checks if snake collides with the walls
        if(x[0] < 0)        //touches the left wall
        {
            running = false;    //stops running
        }
        if(x[0] > SCREEN_WIDTH)     //touches the right wall
        {
            running = false;    //stops running
        } 
        if(y[0] < 0)        //touches the top border
        {
            running = false;    //stops running
        }
        if(y[0] > SCREEN_HEIGHT)    //touches the bottom border
        {
            running = false;    //stops running 
        } 

        if(!running)        //if snake stops , stop the timer
        {
            timer.stop();  
        }
    }

    public void gameOver(Graphics g)        //game is over snake collides
    {
        //displays the score of the user
        g.setColor(Color.YELLOW);       //sets the color of the text
        g.setFont(new Font("Times New Roman" , Font.BOLD , 50));   //sets the font style and size of the text
        FontMetrics metrics1 = getFontMetrics(g.getFont());      //gets the font from setFont method
        g.drawString("Score : "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score : "+applesEaten))/2, g.getFont().getSize());    //places the string at the top of the window
        
        //displays the game over
        g.setColor(Color.YELLOW);       //sets the color of the text
        g.setFont(new Font("Times New Roman" , Font.BOLD , 100));   //sets the font style and size of the text
        FontMetrics metrics2 = getFontMetrics(g.getFont());      //gets the font from setFont method
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, (SCREEN_HEIGHT + g.getFont().getSize())/2);    //places the string "Game Over" at the center
    }
    
    public void actionPerformed(ActionEvent e)      //when game starts actions to be performed
    {
        if(running)  //events to be performed while snake is moving
        {
            move();     //moves the snake
            checkApple();   //eat the apple
            checkCollisions();  //checks if the snake collides with any of walls or its bodyparts
        }
        repaint();      //updates the display after moving the snake
    }
    
    public class MyKeyAdapter extends KeyAdapter        //uses the keyboard for input
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())      //gets the keycode pressed by user
            {
                case KeyEvent.VK_LEFT:      //Left arrow key of keyboard
                case KeyEvent.VK_A:         //'A' on keyboard
                    if(direction != 'R')
                    {
                        direction = 'L';    //move to left direction
                    }
                    break;
                case KeyEvent.VK_RIGHT:      //Right arrow key of keyboard
                case KeyEvent.VK_D:          //'D' on keyboard   
                    if(direction != 'L')
                    {
                        direction = 'R';    //move to right direction
                    }
                    break; 
                case KeyEvent.VK_DOWN:      //Down arrow key of keyboard
                case KeyEvent.VK_S:         //'S' on keyboard
                    if(direction != 'U')
                    {
                        direction = 'D';    //move to down direction
                    }
                    break;
                case KeyEvent.VK_UP:      //Up arrow key of keyboard
                case KeyEvent.VK_W:       //'W' on keyboard  
                    if(direction != 'D')
                    {
                        direction = 'U';    //move to up direction
                    }
                    break;           
            }
        }
    }
} 