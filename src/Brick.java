import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// import Brick.DrawArea;

public class Brick extends JFrame implements KeyListener {
    private Timer timer;
    private DrawArea drawArea;
    private JButton restart;
    private JPanel canvas;
    private int speed;
    private boolean dead, won;
    private int BarPos = 390;
    private boolean movedCheck = false;
    private boolean hasStarted = false;
    private double Xr = 0.0;// 7
    private double Yr = 0.0;// 15
    private int ArrowAngle = 0;
    private boolean SLOW = false;
    private int numBroken = 0;
    private int numBrokenTemp = -1;
    private double XrTemp = 0.0;
    private double YrTemp = 0.0;
    private int score = 0;
    private boolean Begin = false;
    private int DiffPos = 0; // easy=0; medium=1; hard=2;
    private double DiffMod = 1.0;
    private double MAXr = 14;


    public Brick() {
        addKeyListener(this);
        drawArea = new DrawArea();
        final TimerAction ta = new TimerAction();
        timer = new Timer(40, ta);
        timer.start();
        canvas = new JPanel();

        restart = new JButton("Restart");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                BarPos = 390;
                won = false;
                dead = false;
                speed = 3;
                timer.start();
            }
        });

        canvas.add(drawArea);
        // canvas.add(restart);
        drawArea.requestFocus();
        this.setContentPane(canvas);

    }


    public void keyPressed(KeyEvent e) {

        if (timer.isRunning()) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (hasStarted) {
                    drawArea.addBar(-50);
                    drawArea.mover();
                    movedCheck = true;
                }
                else
                    ArrowAngle -= 1;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (hasStarted) {
                    drawArea.addBar(50);
                    drawArea.mover();
                    movedCheck = true;
                }
                else
                    ArrowAngle += 1;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                hasStarted = true;
                if (ArrowAngle == 0) {
                    Xr = 0;
                    Yr = -12;
                }
                if (ArrowAngle == 1) {
                    Xr = 6.3;
                    Yr = -13.5;
                }
                if (ArrowAngle == -1) {
                    Xr = -6.3;
                    Yr = -13.5;
                }
                if (ArrowAngle == 2) {
                    Xr = 13.5;
                    Yr = -5;
                }
                if (ArrowAngle == -2) {
                    Xr = -13.5;
                    Yr = -5;
                }
                Xr = Xr * (1 * DiffMod);
                Yr = Yr * (1 * DiffMod);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                Begin = true;
                if (DiffPos == 0)
                    DiffMod = .84;
                if (DiffPos == 1)
                    DiffMod = 1;
                if (DiffPos == 2)
                    DiffMod = 1.1;
                MAXr = MAXr * DiffMod;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (DiffPos == 2)
                    DiffPos = 2;
                else {
                    DiffPos++;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (DiffPos == 0)
                    DiffPos = 0;
                else {
                    DiffPos--;
                }
            }

        }
    }


    public void keyReleased(KeyEvent e) {
    }


    public void keyTyped(KeyEvent e) {
    }


    public class TimerAction implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            drawArea.addLogX(speed);
            drawArea.repaint();
        }
    }


    public class DrawArea extends JPanel {
        private int logX = 490;
        private int logY = 485;
        private ImageIcon background, log, death, win, BarPic, BrickPic,
            CrackedBrick, UpArrow, SlightRight, SlightLeft, WayRight, WayLeft,
            SlowPow, BlackSquare, Difficulty, Arrow;

        private int[][] BrickBroken = new int[6][8];
        private boolean GoRight = false, GoLeft = false;
        private int xRand = (int)(Math.random() * 6);
        private int yRand = (int)(Math.random() * 8);
        private int[][] PowerUps = new int[6][8];


        public DrawArea() {
            this.setPreferredSize(new Dimension(1024, 578)); // 600,337
            this.setMaximumSize(new Dimension(1024, 578)); // 1024, 768
            background = new ImageIcon("BrickWall.jpg");
            BarPic = new ImageIcon("Paddle.gif");
            BrickPic = new ImageIcon("Brick.png");
            log = new ImageIcon("Ball.gif");
            death = new ImageIcon("Death.png");
            for (int i = 0; i < BrickBroken.length; i++)
                for (int j = 0; j < BrickBroken[i].length; j++)
                    BrickBroken[i][j] = 2;
            for (int i = 0; i < PowerUps.length; i++)
                for (int j = 0; j < PowerUps[i].length; j++)
                    PowerUps[i][j] = 0;
            BrickBroken[xRand][yRand] = 1;
            PowerUps[xRand][yRand] = 1;
            win = new ImageIcon("Winner.jpg");
            CrackedBrick = new ImageIcon("GoodCrack.png");
            UpArrow = new ImageIcon("UpArrow.png");
            SlightRight = new ImageIcon("GoodRight.png");
            SlightLeft = new ImageIcon("GoodLeft.png");
            WayRight = new ImageIcon("WayRight.png");
            WayLeft = new ImageIcon("WayLeft.png");
            SlowPow = new ImageIcon("Green.png");
            BlackSquare = new ImageIcon("Black-square.png");
            Difficulty = new ImageIcon("Difficulty.png");
            Arrow = new ImageIcon("arrow.png");

        }


        public void paintComponent(Graphics g) {

            super.paintComponents(g);
            Graphics2D g0 = (Graphics2D)g;
            Graphics2D g2 = (Graphics2D)g;

            if (Begin == false) {
                BlackSquare.paintIcon(this, g0, 0, 0);
                Difficulty.paintIcon(this, g0, 393, 180);
                Arrow.paintIcon(this, g0, 320, 270 + (DiffPos * 84));

                g0.setColor(Color.cyan);
                g0.setFont(new Font("Gulim", Font.BOLD, 100)); // Gulim
                g0.drawString("BRICK BREAKA ", 155, 120);

            }
            else {

                background.paintIcon(this, g2, 0, 0);

                int SuperTEMP = 0;
                if (DiffPos == 2)
                    SuperTEMP = 6;
                if (DiffPos == 0)
                    SuperTEMP = -1;
                if (BarPos < 10)
                    BarPos = 10;
                if (BarPos > 780)
                    BarPos = 780;

                if (movedCheck == false)
                    BarPic.paintIcon(this, g2, BarPos, 520);
                if (GoRight)
                    BarPic.paintIcon(this, g2, BarPos += 13 + SuperTEMP, 520);
                if (GoLeft)
                    BarPic.paintIcon(this, g2, BarPos -= 13 + SuperTEMP, 520);

                for (int i = 0; i < 6; i++)
                    for (int j = 0; j < 8; j++) {
                        if (BrickBroken[i][j] == 1 || BrickBroken[i][j] == 2)
                            BrickPic.paintIcon(this, g2, 155 + 88 * j, 40 + 40
                                * i); // 350, 100
                        if (PowerUps[i][j] == 1)
                            SlowPow.paintIcon(this, g2, 178 + 88 * j, 40 + 40
                                * i);
                        else if (BrickBroken[i][j] == 1)
                            CrackedBrick.paintIcon(this, g2, 155 + 88 * j, 40
                                + 40 * i);

                    }
                log.paintIcon(this, g2, logX, logY);

                // <<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<
                // Arrow

                if (!hasStarted) {
                    if (ArrowAngle > 2)
                        ArrowAngle = 2;
                    if (ArrowAngle < -2)
                        ArrowAngle = -2;

                    if (ArrowAngle == 0)
                        UpArrow.paintIcon(this, g2, 492, 370);
                    if (ArrowAngle == 1)
                        SlightRight.paintIcon(this, g2, 510, 385);
                    if (ArrowAngle == -1)
                        SlightLeft.paintIcon(this, g2, 450, 385);
                    if (ArrowAngle == 2)
                        WayRight.paintIcon(this, g2, 517, 440);
                    if (ArrowAngle == -2)
                        WayLeft.paintIcon(this, g2, 390, 440);
                }

                // <<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<

                if (dead)
                    death.paintIcon(this, g2, 0, 0);

                if (won)
                    win.paintIcon(this, g2, 0, 0);

                int tallyScore = 0;
                for (int i = 0; i < 6; i++)
                    for (int j = 0; j < 8; j++)
                        if (BrickBroken[i][j] < 1)
                            tallyScore++;
                score = tallyScore * 100;
                if (score == 4800)
                    score = 5000;

                Graphics2D g1 = (Graphics2D)g;
                g1.setColor(Color.cyan);
                g1.setFont(new Font("SansSeriff", Font.BOLD, 26));
                g1.drawString("Score: ", 0, 20);
                g1.drawString(score + "", 85, 20);

            }
        }


        public void mover() {
            // moved = true;
        }


        public void addBar(int x) {

            if (x > 0) {
                GoRight = true;
                GoLeft = false;
            }
            else {
                GoLeft = true;
                GoRight = false;
            }

        }


        public void addLogX(int x) {

            if (numBroken == numBrokenTemp + 3 && SLOW) {
                Xr = XrTemp;
                Yr = YrTemp;
                SLOW = false;
            }

            if (logX < 5)
                Xr *= -1;
            if (logX > 980)
                Xr *= -1;
            if (logY < 0)
                Yr *= -1;
            if (logY > 485 && (logX > BarPos && logX < (BarPos + 231))) {
                Xr += (((int)(Math.random() * 3) - 1) * Math.PI) / 2; // *2
                Yr *= -1;
            }
            // r = -r;
            if (logY > 570)
                die();
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 8; j++) {
                    if (((logX > (155 + 88 * j) && logX < (243 + 88 * j))
                        && (Math.abs(logY - (40 + 40 * i)) < 10 || Math.abs(logY
                            - (80 + 40 * i)) < 10)) && (BrickBroken[i][j] == 1
                                || BrickBroken[i][j] == 2)) {
                        if (PowerUps[i][j] == 1) {
                            SLOW = true;
                            numBrokenTemp = numBroken;
                            PowerUps[i][j] = 0;
                            XrTemp = Xr;
                            YrTemp = Yr;
                            Xr = Xr / 3;
                            Yr = Yr / 3;
                        }

                        BrickBroken[i][j]--;
                        // Xr+=((int)(Math.random()*4)-2)*Math.PI;
                        Yr *= -1;
                        numBroken++;
                    }

                    else if (((logY > (40 + 40 * i) && logY < (80 + 40 * i))
                        && (BrickBroken[i][j] == 1 || BrickBroken[i][j] == 2))
                        && (Math.abs(logX - (155 + 88 * j)) < 10 || Math.abs(
                            logX - (243 + 88 * j)) < 10)) {
                        if (PowerUps[i][j] == 1) {
                            SLOW = true;
                            numBrokenTemp = numBroken;
                            PowerUps[i][j] = 0;
                            XrTemp = Xr;
                            YrTemp = Yr;
                            Xr = Xr / 3;
                            Yr = Yr / 3;
                        }

                        Xr *= -1;
                        BrickBroken[i][j]--;
                        numBroken++;
                    }

                }
            win();

            if (Xr > MAXr)
                Xr = (int)MAXr;
            if (Xr < (-1 * MAXr))
                Xr = (int)(-1 * MAXr);

            if (logX + Xr < 0 && logX > 0)
                logX = 1;
            else if (logX + Xr > 0 && logX < 0)
                logX = -1;
            else
                logX += Xr;

            logY += Yr;

        }


        public void win() {
            int tally = 0;
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 8; j++)
                    if (BrickBroken[i][j] < 1)
                        tally++;
            if (tally == 48)
                won = true;
        }


        public void die() {

            gameOver();
            return;

        }


        public void gameOver() {
            dead = true;
            timer.stop();

            repaint();
        }


        public boolean isDead() {
            return dead;
        }

    }

}
