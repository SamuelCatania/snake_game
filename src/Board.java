import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class Board extends JPanel implements ActionListener {

    private final int BOARD_WIDTH = Snake.screen_width;
    private final int BOARD_HEIGHT = Snake.screen_height;
    private final int SQUARE_SIZE = 20;
    private final int MAX_DOTS = 625;
    private final int DELAY = 100;

    private final int[] x = new int[MAX_DOTS];
    private final int[] y = new int[MAX_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean rightDirection = true;
    private boolean leftDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean newHighScore = false;

    private Timer timer;
    private final File file = new File("high_scores.txt");
    private final Color DARK_GREEN = new Color(7, 79, 23);

    private final Font small = new Font("Comic Sans", Font.BOLD, 14);
    private final FontMetrics metrics = getFontMetrics(small);

    private final Scanner s = new Scanner(System.in);


    public Board() {

        initBoard();
    }

    private void initBoard() {

        addKeyListener(new keyListener());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        initGame();

    }

    private void initGame() {

        dots = 3;

        x[0] = 700;
        y[0] = 440;

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawSnake(g);
    }


    private void drawSnake(Graphics g) {

        if (inGame) {

            g.setColor(Color.RED);

            g.fillOval(apple_x, apple_y, SQUARE_SIZE, SQUARE_SIZE);

            for (int a = 0; a < dots; a++) {

                if (a == 0) {

                    g.setColor(Color.WHITE);

                } else {

                    g.setColor(Color.WHITE);
                }

                g.fillRect(x[a], y[a], SQUARE_SIZE, SQUARE_SIZE);

                g.setColor(Color.BLACK);

                g.drawRect(x[a], y[a], SQUARE_SIZE, SQUARE_SIZE);
            }

        } else {

            gameOver(g);
        }
    }

    private void restart() {

        rightDirection = true;
        leftDirection = false;
        upDirection = false;
        downDirection = false;

        timer.stop();

        inGame = true;

        initGame();

    }

    private void gameOver(Graphics g) {

        String highScore = getHighScore();
        writeScore();

        String yourScore = ("Your score:" + " ( " + dots + " ) ");
        String newHighScoreNotice = ("A New High Score! Congratulations!");
        String options = ("Press R to restart. Press Space to quit");
        String gameOver = "Game Over";

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(gameOver, (BOARD_WIDTH - metrics.stringWidth(gameOver)) / 2, BOARD_HEIGHT / 2);
        g.drawString(yourScore, (BOARD_WIDTH - metrics.stringWidth(yourScore)) / 2, BOARD_HEIGHT / 2 + 50);
        g.drawString(options, (BOARD_WIDTH - metrics.stringWidth(options)) / 2, BOARD_HEIGHT / 2 + 400);

        if (highScore != null && !newHighScore) {

            g.drawString(highScore, (BOARD_WIDTH - metrics.stringWidth(highScore)) / 2, BOARD_HEIGHT / 2 + 100);
        } else {

            g.drawString(newHighScoreNotice, (BOARD_WIDTH - metrics.stringWidth(newHighScoreNotice)) / 2, BOARD_HEIGHT / 2 + 100);

            newHighScore = false;
        }
    }

    private void writeScore() {

        try {

            FileWriter fileWriter = new FileWriter(file, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.newLine();

            bufferedWriter.write(dots + "");

            bufferedWriter.close();


        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    private String getHighScore() {

        try {

            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);


            String firstLine = bufferedReader.readLine();

            int max = Integer.parseInt(firstLine);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                int number = Integer.parseInt(line);

                if (number > max) {

                    max = number;

                }
            }

            if (dots > max) {

                newHighScore = true;
            }

            bufferedReader.close();

            return ("High score: " + max);


        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * ((BOARD_WIDTH / SQUARE_SIZE) - 1));
        apple_x = r * SQUARE_SIZE;

        r = (int) (Math.random() * ((BOARD_HEIGHT / SQUARE_SIZE) - 1));
        apple_y = r * SQUARE_SIZE;
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int a = dots; a > 0; a--) {
            x[a] = x[(a - 1)];
            y[a] = y[(a - 1)];
        }

        if (rightDirection) {

            x[0] += SQUARE_SIZE;
        }

        if (leftDirection) {

            x[0] -= SQUARE_SIZE;
        }

        if (upDirection) {

            y[0] -= SQUARE_SIZE;
        }

        if (downDirection) {

            y[0] += SQUARE_SIZE;
        }

    }

    private void checkCollision() {

        for (int a = dots; a > 0; a--) {

            if ((x[0] == x[a]) && (y[0] == y[a])) {

                inGame = false;
                break;
            }
        }

        if (x[0] >= BOARD_WIDTH) {

            inGame = false;
        }

        if (x[0] < 0) {

            inGame = false;
        }

        if (y[0] >= BOARD_HEIGHT) {

            inGame = false;
        }

        if (y[0] < 0) {

            inGame = false;
        }

    }

    private class keyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {

                rightDirection = true;
                upDirection = false;
                downDirection = false;

            }

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {

                leftDirection = true;
                upDirection = false;
                downDirection = false;

            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {

                upDirection = true;
                rightDirection = false;
                leftDirection = false;

            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {

                downDirection = true;
                rightDirection = false;
                leftDirection = false;

            }

            if (key == KeyEvent.VK_SPACE) {

                System.exit(0);
            }

            if (key == KeyEvent.VK_R) {

                restart();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            move();
            checkApple();
            checkCollision();

            repaint();
        }
    }
}
