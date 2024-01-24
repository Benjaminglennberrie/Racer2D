import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Vector;
import java.awt.image.BufferStrategy;

public class Racer2D extends JFrame implements ActionListener, KeyListener {
    private JButton startButton, quitButton;
    private Clip lobbyMusic;
    private JLabel roadImage, carImage;
    private boolean endgame;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private double p1width, p1height, p1originalX, p1originalY, p1velocity;
    private ImageObject p1;
    private BufferedImage OffTrack, player1;
    private BufferStrategy bs;
    private JLayeredPane layeredPane;

    public Racer2D() {
        setTitle("Racer2D");
        setSize(500, 590);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startButton = new JButton("Start Game");
        quitButton = new JButton("Quit");

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        add(startButton, BorderLayout.NORTH);
        add(quitButton, BorderLayout.SOUTH);

        loadMusic("C:\\Users\\theru\\OneDrive\\Desktop\\Courses\\EGR222\\2022.01 Spring\\Racer2D\\res\\lobbymusic.wav");

        layeredPane = new JLayeredPane();
        layeredPane.setFocusable(true);

        roadImage = new JLabel(new ImageIcon("C:\\Users\\theru\\OneDrive\\Desktop\\Courses\\EGR222\\2022.01 Spring\\Racer2D\\res\\rainbowroad.png"));
        roadImage.setBounds(0, 0, 500, 500);
        layeredPane.add(roadImage, JLayeredPane.DEFAULT_LAYER);

        carImage = new JLabel(new ImageIcon("C:\\Users\\theru\\OneDrive\\Desktop\\Courses\\EGR222\\2022.01 Spring\\Racer2D\\res\\bluecar1.png"));
        carImage.setBounds(150, 150, 560, 200);
        carImage.setVisible(false);
        layeredPane.add(carImage, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        layeredPane.addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();


    }


    private void loadMusic(String filename) {
        try {
            File file = new File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            lobbyMusic = AudioSystem.getClip();
            lobbyMusic.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }

    private void startGame() {
        System.out.println("Start Game button clicked");

        roadImage.setVisible(true);
        carImage.setVisible(true);

        endgame = false;
        p1 = new ImageObject(150, 150, 560, 200, 0.0);
        p1velocity = 0.0;

        playMusic();

        new Animate();
        new PlayerMover();

    }

    private void playMusic() {
        lobbyMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }



    public void keyTyped(KeyEvent e) {}



    private class Animate implements ActionListener {
        private Timer timer;

        public Animate() {
            timer = new Timer(32, this);
            timer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            p1.calculateMovement(p1velocity);
            p1.screenBounds(0, getWidth(), 0, getHeight());

            repaint();
        }
    }

    private class PlayerMover implements ActionListener {
        private double velocityStep, rotateStep, maxVelocity, brakingForce;
        private Timer timer;

        public PlayerMover() {
            velocityStep = 0.02; // Acceleration
            rotateStep = 0.03;
            maxVelocity = 2;
            brakingForce = 0.02;

            timer = new Timer(10, this);
            timer.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (upPressed) {
                if (p1velocity < maxVelocity) {
                    p1velocity += velocityStep;
                }
            }
            if (downPressed) {
                if (p1velocity > -1) {
                    p1velocity -= brakingForce;
                }
            }
            if (leftPressed) {
                p1.rotate(-rotateStep);
            }
            if (rightPressed) {
                p1.rotate(rotateStep);
            }

            // apply drag force
            if (!(upPressed || downPressed || leftPressed || rightPressed) && p1velocity != 0) {
                if ((p1velocity - 0.1) < 0) {
                    p1velocity = 0;
                } else {
                    p1velocity -= 0.04;
                }
            }

            p1.calculateMovement(p1velocity);
            p1.screenBounds(0, getWidth(), 0, getHeight());

            repaint();
        }
    }

    private class ImageObject {
        private double x, y, xwidth, yheight, angle;

        public ImageObject(double xinput, double yinput, double xwidthinput,
                           double yheightinput, double angleinput) {
            x = xinput;
            y = yinput;
            xwidth = xwidthinput;
            yheight = yheightinput;
            angle = angleinput;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return xwidth;
        }

        public double getHeight() {
            return yheight;
        }

        public double getAngle() {
            return angle;
        }

        public void move(double xinput, double yinput) {
            x += xinput;
            y += yinput;
        }

        public void calculateMovement(double velocity) {
            double deltaX = velocity * Math.cos(angle);
            double deltaY = velocity * Math.sin(angle);
            move(deltaX, deltaY);
        }

        public void screenBounds(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            if (x < leftEdge) {
                x = leftEdge;
            }
            if (x + getWidth() > rightEdge) {
                x = rightEdge - getWidth();
            }
            if (y < topEdge) {
                y = topEdge;
            }
            if (y + getHeight() > bottomEdge) {
                y = bottomEdge - getHeight();
            }
        }

        public void rotate(double input) {
            angle += input;
            while (angle > (Math.PI * 2)) {
                angle -= (Math.PI * 2);
            }
            while (angle < 0) {
                angle += (Math.PI * 2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Racer2D().setVisible(true);
            }
        });
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            upPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            upPressed = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
    }
}
