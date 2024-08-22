import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int SIZE = 20;
    private final int NUM_CELLS_X = WIDTH / SIZE;
    private final int NUM_CELLS_Y = HEIGHT / SIZE;
    
    private ArrayList<Point> snake;
    private Point food;
    private int direction = KeyEvent.VK_RIGHT;
    private boolean running = true;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initializeGame();
    }

    private void initializeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(NUM_CELLS_X / 2, NUM_CELLS_Y / 2));
        placeFood();
        timer = new Timer(100, this);
        timer.start();
    }

    private void placeFood() {
        Random rand = new Random();
        int x = rand.nextInt(NUM_CELLS_X);
        int y = rand.nextInt(NUM_CELLS_Y);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * SIZE, p.y * SIZE, SIZE, SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * SIZE, food.y * SIZE, SIZE, SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) return;
        moveSnake();
        checkCollision();
        checkFood();
        repaint();
    }

    private void moveSnake() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case KeyEvent.VK_UP:
                head.translate(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                head.translate(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                head.translate(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                head.translate(1, 0);
                break;
        }
        
        if (head.x < 0 || head.x >= NUM_CELLS_X || head.y < 0 || head.y >= NUM_CELLS_Y || snake.contains(head)) {
            running = false;
            timer.stop();
            return;
        }
        
        snake.add(0, head);
        
        if (head.equals(food)) {
            placeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= NUM_CELLS_X || head.y < 0 || head.y >= NUM_CELLS_Y || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            timer.stop();
        }
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            placeFood();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int newDirection = e.getKeyCode();
        if ((newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
            (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) ||
            (newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
            (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT)) {
            direction = newDirection;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}