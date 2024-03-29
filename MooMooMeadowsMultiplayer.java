package src.src;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.*;

import javax.swing.border.Border;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.*;
import java.awt.*;

public class MooMooMeadowsMultiplayer {
    private static Speedometer speedometer; // Make it static
    private static int currentSegment = 5;


    public MooMooMeadowsMultiplayer() {




        src.src.MooMooMeadowsMultiplayer.setup();

    }

    public static void setup() {
        appFrame = new JFrame("Scuffed Rainbow Road");
        XOFFSET = 0;
        YOFFSET = 0;
        WINWIDTH = 1280; //500    2000
        WINHEIGHT = 720; //500   1000



        endgame = false;

        p1width = 20; //30
        p1height = 20; //30
        p1originalX = RESPAWN_X;
        p1originalY = RESPAWN_Y;


        p2width = 20; //30
        p2height = 20; //30
        p2originalX = RESPAWN_X2;
        p2originalY = RESPAWN_Y2;



        nitroFlamePNGWidth = 25;
        nitroFlamePNGHeight = 25;


        try { // IO
//            player1 = ImageIO.read( new File("C:\\Users\\theru\\OneDrive\\Desktop\\Courses\\EGR222\\2022.01 Spring\\MooMooMeadowsMultiplayer\\res\\bluecar1.png") );
            player1 = ImageIO.read( new File("res/RainbowRoad/marioplayersmall.png") );

            player2 = ImageIO.read( new File("res/RainbowRoad/luigiplayersmall.png"));





            OnTrack = ImageIO.read( new File("res/MooMooMeadowsMultiplayer/MooMooMeadows.png") );
//            OnTrack = ImageIO.read( new File("/Users/benjaminbrodwolf/IdeaProjects/MooMooMeadowsMultiplayer-Benjamin-Brodwolf/src/src/resources/CBUTrack.png") );

            OffTrack = ImageIO.read( new File("res/RainbowRoad/largerrainbowroadmultiplayerspace.png") );
//            OffTrack = ImageIO.read( new File("/Users/benjaminbrodwolf/IdeaProjects/MooMooMeadowsMultiplayer-Benjamin-Brodwolf/src/src/resources/CBUTrack.png") );

            finishline = ImageIO.read( new File("res/RainbowRoad/FINISHLINEMULTIPLAYER.png") );

            checkpoint = ImageIO.read( new File("res/RainbowRoad/CHECKPOINTMULTIPLAYER.png") );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static class BackgroundMusic implements Runnable {
        private String file = "res/RainbowRoad/Rainbow-Road-Mario-Kart-Wii.wav";


        public BackgroundMusic(String file) {
            this.file = file;
        }

        public void play() {
            Thread t = new Thread(this);
            t.start();
        }

        public void run() {
            playSound(file);
        }

        private void playSound(String file) {
            File soundFile = new File(file);
            AudioInputStream inputStream = null;

            try { // get input stream
                Clip clip = AudioSystem.getClip();
                inputStream = AudioSystem.getAudioInputStream(soundFile);
                clip.open(inputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }











    private static void setButtonAppearance(JButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(15, URANIAN),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        button.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(HIGHLIGHT);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(URANIAN);
            }

        });


        button.setBackground(URANIAN);
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
    }


    private static class RoundBorder implements Border { // Used for rounded buttons
        private int radius;
        private Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }














    private static class MyButton extends JButton {
        public MyButton(String text) {
            super(text);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));  // Adjust the radius here
            super.paintComponent(g);
            g2.dispose();
        }
    }








    private static class Animate implements Runnable, ImageObserver {
        int i = 0;
        public void run() {
            bs = appFrame.getBufferStrategy();
            if (bs == null) {
                return;
            }

            while (!endgame) {


                Graphics g = bs.getDrawGraphics();
                Graphics2D g2D = (Graphics2D) g;




                // Draw the track
                g2D.drawImage(OffTrack, XOFFSET, YOFFSET, null);
                g2D.drawImage(OnTrack, XOFFSET, YOFFSET, null);


            //    g2D.drawImage(finishline, XOFFSET, YOFFSET, null);

            //    g2D.drawImage(checkpoint, XOFFSET, YOFFSET, null);




                g2D.setColor(Color.RED);

                g2D.setFont(new Font("Arial", Font.PLAIN, 200));

                //Check if a player has won.
                if (lapCount1 == numLapsToWin) {
                    g2D.drawString("Mario won!", 75, 420 );
                    BackgroundMusic menu_theme = new BackgroundMusic("res/finishlapyay.wav");
                    menu_theme.play();
                    endgame = true;
                }
                //Check if a player has won.
                if (lapCount2 == numLapsToWin) {
                    g2D.drawString("Luigi won!", 75, 420 );
                    BackgroundMusic menu_theme = new BackgroundMusic("res/finishlapyay.wav");
                    menu_theme.play();
                    endgame = true;
                }





                g2D.setColor(Color.LIGHT_GRAY);

                g2D.setFont(new Font("Arial", Font.PLAIN, 10));

                g2D.drawString("Player 1:  Velocity: " + Math.round(p1velocity * 10), 50, 50);
                g2D.drawString("Lap Counter " + lapCount1, 200, 50);
                g2D.drawString("Best Lap Time " + String.format("%.2f", bestLapP1) + " seconds", 300, 50);

                g2D.drawString("Player 2:  Velocity: " + Math.round(p2velocity * 10), 600, 50);
                g2D.drawString("Lap Counter " + lapCount2, 750, 50);
                g2D.drawString("Best Lap Time " + String.format("%.2f", bestLapP2) + " seconds", 850, 50);


                if (bestLapP1 == 0) {
                    bestLapP1 = LaptimeP1;
                }
                if (bestLapP2 == 0) {
                    bestLapP2 = LaptimeP2;
                }

                if (isCollidingWithGrass(p1.getX(), p1.getY(), checkpoint)) {
                    hasCrossedCheckPointp1 = true;
                }
                if (isCollidingWithGrass(p2.getX2(), p2.getY2(), checkpoint)) {
                    hasCrossedCheckPointp2 = true;
                }






                if (isCollidingWithGrass(p1.getX(), p1.getY(), finishline)) {
                    if (!hasCrossedFinishLinep1 && hasCrossedCheckPointp1 == true) {
                        lapCount1 += 1;
                        hasCrossedFinishLinep1 = true;
                        hasCrossedCheckPointp1 = false;

                        // Stop timing the lap
                        lapEndTimeP1 = System.currentTimeMillis();
                        LaptimeP1 = (lapEndTimeP1 - lapStartTimeP1) / 1000.0;

                        // Display lap time for player 1
                        System.out.println("Player 1 Lap Time: " + LaptimeP1 + " seconds");

                        // Check if it's a new best lap
                        if (LaptimeP1 < bestLapP1) {
                            bestLapP1 = LaptimeP1;
                            System.out.println("Player 1 New Best Lap: " + bestLapP1 + " seconds");
                        }

                        // Start timing the new lap
                        lapStartTimeP1 = System.currentTimeMillis();
                    }
                    lapStartTimeP1 = System.currentTimeMillis();

                } else {
                    hasCrossedFinishLinep1 = false;
                }


                if (isCollidingWithGrass(p2.getX2(), p2.getY2(), finishline)) {
                    if (!hasCrossedFinishLinep2 && hasCrossedCheckPointp2 == true) {
                        lapCount2 += 1;
                        hasCrossedFinishLinep2 = true;
                        hasCrossedCheckPointp2 = false;

                        // Stop timing the lap
                        lapEndTimeP2 = System.currentTimeMillis();
                        LaptimeP2 = (lapEndTimeP2 - lapStartTimeP2) / 1000.0;

                        // Display lap time for player 2
                        System.out.println("Player 2 Lap Time: " + LaptimeP2 + " seconds");

                        // Check if it's a new best lap
                        if (LaptimeP2 < bestLapP2) {
                            bestLapP2 = LaptimeP2;
                            System.out.println("Player 1 New Best Lap: " + bestLapP2 + " seconds");
                        }

                        // Start timing the new lap
                        lapStartTimeP2 = System.currentTimeMillis();
                    }
                    lapStartTimeP2 = System.currentTimeMillis();

                } else {
                    hasCrossedFinishLinep2 = false;
                }




                // Draw the player
                g2D.drawImage(rotateImageObject(p1).filter(player1, null), (int) (p1.getX() + 0.5),
                        (int) (p1.getY() + 0.5), null);



                if (spacePressed && !isCollidingWithGrass(p1.getX(), p1.getY(), OffTrack)) {
                    try {
                        player1 = ImageIO.read( new File("res/RainbowRoad/marioplayerboostingsmall.png") );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
//                System.out.println("Coords:" + p1.x + "  " + p1.y);


// RESPAWN PROGRAMMING
                i += 1;
                if (i==10 && !isCollidingWithGrass(p1.getX(), p1.getY(), OffTrack)) {
                    RESPAWN_X = p1.x;
                    RESPAWN_Y = p1.y;
                    System.out.println("   \nSpawn Reset\n   ");
                }



                if (!spacePressed) {
                    try {
                        player1 = ImageIO.read(new File("res/RainbowRoad/marioplayersmall.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }





                g2D.drawImage(rotateImageObject2(p2).filter(player2, null), (int) (p2.getX2() + 0.5),
                        (int) (p2.getY2() + 0.5), null);

                if (qPressed && !isCollidingWithGrass(p2.getX2(), p2.getY2(), OffTrack)) {
                    try {
                        player2 = ImageIO.read( new File("res/RainbowRoad/luigiplayerboostingsmall.png") );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

//                System.out.println("Player 2 Coords:" + p2.x2 + "  " + p2.y2);

                if (i==10 && !isCollidingWithGrass(p2.getX2(), p2.getY2(), OffTrack)) {
                    RESPAWN_X2 = p2.x2;
                    RESPAWN_Y2 = p2.y2;
                    System.out.println("   \nSpawn Reset 2\n   ");
                }
                if (i==10) {
                    i=0;
                }
                if (!qPressed) {
                    try {
                        player2 = ImageIO.read(new File("res/RainbowRoad/luigiplayersmall.png"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }








                g.dispose();
                g2D.dispose();
                bs.show();

                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true;
        }


    }









    // thread responsible for updating player movement
    private static class PlayerMoverplayer1 implements Runnable {
        public PlayerMoverplayer1() {
            p1velocitystep = 0.01; // aka accel
            p1rotatestep = 0.03; //0.03
            p1maxvelocity = 2;
            p1brakingforce = 0.04;
            p1nitroBoost = 2;

        }

        public void run() {
            while (!endgame) {
                try {
                    Thread.sleep(9);
                } catch (InterruptedException e) { }


                if (isCollidingWithGrass(p1.getX(), p1.getY(), OffTrack)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }



//                    try {
//                        OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroad.png"));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace.png"));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }


                    p1.moveto(RESPAWN_X,RESPAWN_Y);


//                    p1.setAngle(4.8);

//                    currentSegment = 5;
                    p1velocity = 0.0;



                } else {
                    p1maxvelocity = 2;
                    p1velocitystep = 0.01; // aka accel

                }


                if (spacePressed == true && isCollidingWithGrass(p1.getX(), p1.getY(), OffTrack) == false) {
                    //CHEAT
                    // NITRO
                    if (upPressed == false) {
                        if (p1velocity < p1maxvelocity) {
                            p1velocity = (p1velocity) + p1velocitystep;
                        } else if (p1velocity >= p1maxvelocity) { // ensure max vel not exceeded
                            p1velocity = p1maxvelocity;
                        }
                    }



                    p1maxvelocity += p1nitroBoost;
                    p1velocitystep = 0.03;

                    double flameX = p1.getX() + (p1.getWidth() / 2.0) - nitroFlamePNGWidth / 2.0;
                    double flameY = p1.getY() + (p1.getHeight() / 2.0) - nitroFlamePNGHeight / 2.0;
                    nitroFlameX = flameX;
                    nitroFlameY = flameY;


                    System.out.println("BOOOOOOOST");
                }


                if (upPressed == true) {
                    if (p1velocity < p1maxvelocity) {
                        p1velocity = (p1velocity) + p1velocitystep;
                    } else if (p1velocity >= p1maxvelocity) { // ensure max vel not exceeded
                        p1velocity = p1maxvelocity;
                    }
                }
                if (downPressed == true) {

                    if (p1velocity < -1) { // ensure max rev speed
                        p1velocity = -1;
                    } else {
                        p1velocity = p1velocity - p1brakingforce;
                    }
                }
                if (leftPressed == true) {
                    if (p1velocity < 0) {
                        p1.rotate(-p1rotatestep);
                    } else {
                        p1.rotate(p1rotatestep);
                    }
                }
                if (rightPressed == true) {
                    if (p1velocity < 0) {
                        p1.rotate(p1rotatestep);
                    } else {
                        p1.rotate(-p1rotatestep);
                    }
                }

                // apply drag force
                if (!upPressed && !downPressed && !leftPressed && !rightPressed && !spacePressed
                        && p1velocity != 0) {
                    if ((p1velocity - 0.1) < 0) {
                        p1velocity = 0;
                    } else {
                        p1velocity = p1velocity - 0.04;
                    }
                }

                p1.move(-p1velocity * Math.cos(p1.getAngle() - Math.PI / 2.0),
                        p1velocity * Math.sin(p1.getAngle() - Math.PI / 2.0));
                try {
                    p1.screenBounds(XOFFSET, WINWIDTH, YOFFSET, WINHEIGHT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private double p1velocitystep, p1rotatestep, p1maxvelocity, p1brakingforce, p1nitroBoost;
    }


    // thread responsible for updating player movement
    private static class PlayerMoverplayer2 implements Runnable {
        public PlayerMoverplayer2() {
            p2velocitystep = 0.01; // aka accel
            p2rotatestep = 0.03; //0.03
            p2maxvelocity = 2;
            p2brakingforce = 0.04;
            p2nitroBoost = 2;

        }

        public void run() {
            while (!endgame) {
                try {
                    Thread.sleep(9);
                } catch (InterruptedException e) { }


                if (isCollidingWithGrass(p2.getX2(), p2.getY2(), OffTrack)) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

//                    try {
//                        OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroad.png"));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace.png"));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                    p2.moveto2(RESPAWN_X2,RESPAWN_Y2);


//                    currentSegment = 5;
                    p2velocity = 0.0;



                } else {
                    p2maxvelocity = 2;
                    p2velocitystep = 0.01; // aka accel

                }


                if (qPressed == true && isCollidingWithGrass(p2.getX2(), p2.getY2(), OffTrack) == false) {
                    //CHEAT
                    // NITRO
                    if (wPressed == false) {
                        if (p2velocity < p2maxvelocity) {
                            p2velocity = (p2velocity) + p2velocitystep;
                        } else if (p2velocity >= p2maxvelocity) { // ensure max vel not exceeded
                            p2velocity = p2maxvelocity;
                        }
                    }
                    p2maxvelocity += p2nitroBoost;
                    p2velocitystep = 0.02;
                    System.out.println("BOOOOOOOST");
                }


                if (wPressed == true) {
                    if (p2velocity < p2maxvelocity) {
                        p2velocity = (p2velocity) + p2velocitystep;
                    } else if (p2velocity >= p2maxvelocity) { // ensure max vel not exceeded
                        p2velocity = p2maxvelocity;
                    }
                }
                if (sPressed == true) {
                    System.out.println("down IS BEING PRESSED");

                    if (p2velocity < -1) { // ensure max rev speed
                        p2velocity = -1;
                    } else {
                        p2velocity = p2velocity - p2brakingforce;
                    }
                }




                if (dPressed == true) {
                    if (p2velocity < 0) {
                        p2.rotate2(p2rotatestep);
                    } else {
                        p2.rotate2(-p2rotatestep);
                    }
                }
                if (aPressed == true) {
                    if (p2velocity < 0) {
                        p2.rotate2(-p2rotatestep);
                    } else {
                        System.out.println("TURN LEFTTTT!!!!!!!!!");
                        p2.rotate2(p2rotatestep);
                    }
                }
                // apply drag force
                if (!wPressed && !sPressed && !aPressed && !dPressed && !qPressed
                        && p2velocity != 0) {
                    if ((p2velocity - 0.1) < 0) {
                        p2velocity = 0;
                    } else {
                        p2velocity = p2velocity - 0.04;
                    }
                }

                p2.move2(-p2velocity * Math.cos(p2.getAngle2() - Math.PI / 2.0),
                        p2velocity * Math.sin(p2.getAngle2() - Math.PI / 2.0));
                try {
                    p2.screenBounds(XOFFSET, WINWIDTH, YOFFSET, WINHEIGHT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private double p2velocitystep, p2rotatestep, p2maxvelocity, p2brakingforce, p2nitroBoost;
    }











    private static synchronized boolean isCollidingWithGrass(double bluecarX, double bluecarY, BufferedImage grass) {
        int x = (int) bluecarX;
        int y = (int) bluecarY;
        int x2 = (int) bluecarX;
        int y2 = (int) bluecarY;

        // Check if the coordinates are within bounds
        if (x >= 0 && x < grass.getWidth() && y >= 0 && y < grass.getHeight()) {
            int pixelColor = grass.getRGB(x, y);
            return (pixelColor & 0xFF000000) != 0;
        }

        // Check if the coordinates are within bounds
        if (x2 >= 0 && x2 < grass.getWidth() && y2 >= 0 && y2 < grass.getHeight()) {
            int pixelColor = grass.getRGB(x, y);
            return (pixelColor & 0xFF000000) != 0;
        }



        // If coordinates are out of bounds, consider it as colliding with grass
        return true;
    }









    // moveable image objects
    private static class ImageObject {
        private double x, y, xwidth, yheight, angle, internalangle, comX, comY;
        private Vector<Double> coords, triangles;

        public ImageObject() {
        }

        public ImageObject(double xinput, double yinput, double xwidthinput,
                           double yheightinput, double angleinput) {
            x = xinput;
            y = yinput;
            xwidth = xwidthinput;
            yheight = yheightinput;
            angle = angleinput;
            internalangle = 0.0;
            coords = new Vector<Double>();
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

        public double getInternalAngle() {
            return internalangle;
        }

        public void setAngle(double angleinput) {
            angle = angleinput;
        }

        public void setInternalAngle(double input) {
            internalangle = input;
        }

        public Vector<Double> getCoords() {
            return coords;
        }

        public void setCoords(Vector<Double> input) {
            coords = input;
            generateTriangles();
        }

        public void generateTriangles() {
            triangles = new Vector<Double>();
            // format: (0, 1), (2, 3), (4, 5) is x,y coords of triangle

            // get center point of all coords
            comX = getComX();
            comY = getComY();

            for (int i = 0; i < coords.size(); i = i + 2) {
                triangles.addElement(coords.elementAt(i));
                triangles.addElement(coords.elementAt(i + 1));

                triangles.addElement(coords.elementAt((i + 2) % coords.size()));
                triangles.addElement(coords.elementAt((i + 3) % coords.size()));

                triangles.addElement(comX);
                triangles.addElement(comY);
            }
        }

        public void printTriangles() {
            for (int i = 0; i < triangles.size(); i = i + 6) {
                System.out.print("p0x: " + triangles.elementAt(i) + ", p0y " + triangles.elementAt(i + 1));
                System.out.print(" p1x: " + triangles.elementAt(i + 2) + ", p1y: " + triangles.elementAt(i + 3));
                System.out.println(" p2x: " + triangles.elementAt(i + 4) + ", p2y: " + triangles.elementAt(i + 5));
            }
        }

        public double getComX() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 0; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public double getComY() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 1; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public void move(double xinput, double yinput) {
            x = x + xinput;
            y = y + yinput;
        }

        public void moveto(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }



        int currentSegment = 5;
        public void screenBounds(double leftEdge, double rightEdge, double topEdge, double bottomEdge) throws IOException {

//            if (isCollidingWithGrass(p1.getX(), p1.getY(), OffTrack)) {
//                currentSegment = 5;
//            }

//            if (currentSegment == 5 && x + getWidth() > rightEdge) {
//                moveto((leftEdge+50) - getWidth(), getY());
//                p1velocity = p1velocity * 0.9;
//                System.out.println("Mario is touching right");
//                currentSegment = 10;
//
//                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment2.png"));
//                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace2.png"));
//                finishline = ImageIO.read(new File("res/RainbowRoad/NOFINISHLINE.png"));
//
//
//            }
//
//
//            if (currentSegment == 10 && y + getHeight() > bottomEdge) {
//                moveto(getX(), topEdge+50);
//                p1velocity = p1velocity * 0.9;
//                System.out.println("Mario is touching bottom");
//                currentSegment = 15;  // Reset to segment 1
//                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment3.png"));
//                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace3.png"));
//                finishline = ImageIO.read(new File("res/RainbowRoad/NOFINISHLINE.png"));
//                System.out.println(currentSegment);
//            }
//
//
//            if (currentSegment == 15 && x < leftEdge+20) {
//                moveto(rightEdge-50, getY());
//                p1velocity = p1velocity * 0.9;
//                System.out.println("Mario is touching left");
//                currentSegment = 20;
//
//                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment4.png"));
//                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace4.png"));
//            }
//
//
//            if (currentSegment == 20 && y < topEdge+20) {
//                moveto(getX(), (bottomEdge-10) - getHeight());
//                p1velocity = p1velocity * 0.9;
//                System.out.println("Mario is touching top");
//                currentSegment = 5;
//                finishline = ImageIO.read(new File("res/RainbowRoad/FINISHLINEMULTIPLAYER.png"));
//
//                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadsegment1.png"));
//                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace1.png"));
//            }

//            // Reset currentSegment to 5 when respawned
//            if (p1.getX() == RESPAWN_X && p1.getY() == RESPAWN_Y) {
//                currentSegment = 5;
//            }

        }



        public void rotate ( double input){
            angle = angle + input;
            while (angle > (Math.PI * 2)) {
                angle = angle - (Math.PI * 2);
            }
            while (angle < 0) {
                angle = angle + (Math.PI * 2);
            }
        }

        public void spin ( double input){
            internalangle = internalangle + input;
            while (internalangle > (Math.PI * 2)) {
                internalangle = internalangle - (Math.PI * 2);
            }
            while (internalangle < 0) {
                internalangle = internalangle + (Math.PI * 2);
            }
        }
    }

    // moveable image objects
    private static class ImageObject2 {
        private double x2, y2, x2width, y2height, angle2, internalangle2, comX2, comY2;
        private Vector<Double> coords2, triangles2;

        public ImageObject2() {
        }

        public ImageObject2(double x2input, double y2input, double x2widthinput,
                            double y2heightinput, double angleinput2) {
            x2 = x2input;
            y2 = y2input;
            x2width = x2widthinput;
            y2height = y2heightinput;
            angle2 = angleinput2;
            internalangle2 = 0.0;
            coords2 = new Vector<Double>();
        }

        public double getX2() {
            return x2;
        }

        public double getY2() {
            return y2;
        }

        public double getWidth2() {
            return x2width;
        }

        public double getHeight2() {
            return y2height;
        }

        public double getAngle2() {
            return angle2;
        }

        public double getInternalAngle2() {
            return internalangle2;
        }

        public void setAngle2(double angleinput2) {
            angle2 = angleinput2;
        }

        public void setInternalAngle2(double input2) {
            internalangle2 = input2;
        }

        public Vector<Double> getCoords2() {
            return coords2;
        }

        public void setCoords2(Vector<Double> input2) {
            coords2 = input2;
            generateTriangles2();
        }

        public void generateTriangles2() {
            triangles2 = new Vector<Double>();
            // format: (0, 1), (2, 3), (4, 5) is x,y coords of triangle

            // get center point of all coords
            comX2 = getComX2();
            comY2 = getComY2();

            for (int i = 0; i < coords2.size(); i = i + 2) {
                triangles2.addElement(coords2.elementAt(i));
                triangles2.addElement(coords2.elementAt(i + 1));

                triangles2.addElement(coords2.elementAt((i + 2) % coords2.size()));
                triangles2.addElement(coords2.elementAt((i + 3) % coords2.size()));

                triangles2.addElement(comX2);
                triangles2.addElement(comY2);
            }
        }

        public void printTriangles2() {
            for (int i = 0; i < triangles2.size(); i = i + 6) {
                System.out.print("p0x: " + triangles2.elementAt(i) + ", p0y " + triangles2.elementAt(i + 1));
                System.out.print(" p1x: " + triangles2.elementAt(i + 2) + ", p1y: " + triangles2.elementAt(i + 3));
                System.out.println(" p2x: " + triangles2.elementAt(i + 4) + ", p2y: " + triangles2.elementAt(i + 5));
            }
        }

        public double getComX2() {
            double ret = 0;
            if (coords2.size() > 0) {
                for (int i = 0; i < coords2.size(); i = i + 2) {
                    ret = ret + coords2.elementAt(i);
                }
                ret = ret / (coords2.size() / 2.0);
            }
            return ret;
        }

        public double getComY2() {
            double ret = 0;
            if (coords2.size() > 0) {
                for (int i = 1; i < coords2.size(); i = i + 2) {
                    ret = ret + coords2.elementAt(i);
                }
                ret = ret / (coords2.size() / 2.0);
            }
            return ret;
        }

        public void move2(double x2input, double y2input) {
            x2 = x2 + x2input;
            y2 = y2 + y2input;
        }

        public void moveto2(double x2input, double y2input) {
            x2 = x2input;
            y2= y2input;
        }



        int currentSegment = 5;
        public void screenBounds(double leftEdge, double rightEdge, double topEdge, double bottomEdge) throws IOException {

            if (isCollidingWithGrass(p2.getX2(), p2.getY2(), OffTrack)) {
                currentSegment = 5;
            }


            if (currentSegment == 5 && x2 + getWidth2() > rightEdge) {
                moveto2((leftEdge+50) - getWidth2(), getY2());
                p2velocity = p2velocity * 0.9;
                System.out.println("luigi is touching right");
                currentSegment = 10;

                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment2.png"));
                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace2.png"));


            }


            if (currentSegment == 10 && y2 + getHeight2() > bottomEdge) {
                moveto2(getX2(), topEdge+50);
                p2velocity = p2velocity * 0.9;
                System.out.println("luigi is touching bottom");
                currentSegment = 15;  // Reset to segment 1
                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment3.png"));
                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace3.png"));
                System.out.println(currentSegment);
            }


            if (currentSegment == 15 && x2 < leftEdge+20) {
                moveto2(rightEdge-50, getY2());
                p2velocity = p2velocity * 0.9;
                System.out.println("luigi is touching left");
                currentSegment = 20;

                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadSegment4.png"));
                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace4.png"));
            }


            if (currentSegment == 20 && y2 < topEdge+20) {
                moveto2(getX2(), (bottomEdge-10) - getHeight2());
                p2velocity = p2velocity * 0.9;
                System.out.println("Mario is touching top");
                currentSegment = 5;

                OnTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroad.png"));
                OffTrack = ImageIO.read(new File("res/RainbowRoad/largerrainbowroadspace1.png"));
            }

            // Reset currentSegment to 5 when respawned
            if (p2.getX2() == RESPAWN_X2 && p2.getY2() == RESPAWN_Y2) {
                currentSegment = 5;
            }

        }



        public void rotate2 ( double input2){
            angle2 = angle2 + input2;
            while (angle2 > (Math.PI * 2)) {
                angle2 = angle2 - (Math.PI * 2);
            }
            while (angle2 < 0) {
                angle2 = angle2 + (Math.PI * 2);
            }
        }

        public void spin2 ( double input2){
            internalangle2 = internalangle2 + input2;
            while (internalangle2 > (Math.PI * 2)) {
                internalangle2 = internalangle2 - (Math.PI * 2);
            }
            while (internalangle2 < 0) {
                internalangle2 = internalangle2 + (Math.PI * 2);
            }
        }
    }


    // rotates ImageObject
    private static AffineTransformOp rotateImageObject(ImageObject obj) {
        AffineTransform at = new AffineTransform();
        at.translate(obj.getWidth() / 2.0, obj.getHeight() / 2.0);
        at.rotate(-obj.getAngle());
        at.translate(-obj.getWidth() / 2.0, -obj.getHeight() / 2.0);
        return new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    }

    // rotates ImageObject
    private static AffineTransformOp rotateImageObject2(ImageObject2 obj2) {
        AffineTransform at = new AffineTransform();
        at.translate(obj2.getWidth2() / 2.0, obj2.getHeight2() / 2.0);
        at.rotate(-obj2.getAngle2());
        at.translate(-obj2.getWidth2() / 2.0, -obj2.getHeight2() / 2.0);
        return new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    }




    // initiates key actions from panel key responses
    private static void bindKey(JPanel panel, String input) {
        panel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " + input), input + " pressed");
        panel.getActionMap().put(input + " pressed", new KeyPressed(input));

        panel.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + input), input + " released");
        panel.getActionMap().put(input + " released", new KeyReleased(input));
    }





    // monitors keypresses
    private static class KeyPressed extends AbstractAction {
        public KeyPressed() { action = ""; }

        public KeyPressed(String input) { action = input; }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) { upPressed = true; }
            if (action.equals("DOWN")) { downPressed = true; }
            if (action.equals("LEFT")) { leftPressed = true; }
            if (action.equals("RIGHT")) { rightPressed = true; }
            if (action.equals("SPACE")) { spacePressed = true; }

            if (action.equals("W")) { wPressed = true; }
            if (action.equals("S")) { sPressed = true; }
            if (action.equals("A")) { aPressed = true; }
            if (action.equals("D")) { dPressed = true; }
            if (action.equals("Q")) { qPressed = true; }

        }

        private String action;
    }






    // monitors keyreleases
    private static class KeyReleased extends AbstractAction {
        public KeyReleased() { action = ""; }

        public KeyReleased(String input) { action = input; }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) { upPressed = false; }
            if (action.equals("DOWN")) { downPressed = false; }
            if (action.equals("LEFT")) { leftPressed = false; }
            if (action.equals("RIGHT")) { rightPressed = false; }
            if (action.equals("SPACE")) { spacePressed = false; }

            if (action.equals("W")) { wPressed = false; }
            if (action.equals("S")) { sPressed = false; }
            if (action.equals("A")) { aPressed = false; }
            if (action.equals("D")) { dPressed = false; }
            if (action.equals("Q")) { qPressed = false; }



        }

        private String action;
    }






    private static class StartGame implements ActionListener {
        private final JPanel panel;

        public StartGame(JPanel panel) {
            this.panel = panel;
        }

        public void actionPerformed(ActionEvent ae) {
            lapMenu.setVisible(false);
            startButton.setVisible(false);
            quitButton.setVisible(false);
            endgame = true;

            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            spacePressed = false;

            wPressed = false;
            sPressed = false;
            aPressed = false;
            dPressed = false;
            qPressed = false;

            p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0);
            p1velocity = 0.0;

            p2 = new ImageObject2(p2originalX, p2originalY, p2width, p2height, 0);
            p2velocity = 0.0;

            try { Thread.sleep(32); } catch (InterruptedException ie) { }

            endgame = false;
            Thread t1 = new Thread( new Animate() );
            Thread t2 = new Thread( new PlayerMoverplayer1() );
            Thread t3 = new Thread( new PlayerMoverplayer2() );
            t1.start();
            t2.start();
            t3.start();
        }
    }






    private static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }




    private static class GameLevel implements ActionListener {
        public int decodeLevel(String input) {
            if (input.equals("One")) {
                numLapsToWin = 1;
            } else if (input.equals("Two")) {
                numLapsToWin = 2;
            } else if (input.equals("Three")) {
                numLapsToWin = 3;
            } else if (input.equals("Four")) {
                numLapsToWin = 4;
            } else if (input.equals("Five")) {
                numLapsToWin = 5;
            } else if (input.equals("Six")) {
                numLapsToWin = 6;
            } else if (input.equals("Seven")) {
                numLapsToWin = 7;
            } else if (input.equals("Eight")) {
                numLapsToWin = 8;
            } else if (input.equals("Nine")) {
                numLapsToWin = 9;
            } else if (input.equals("Ten")) {
                numLapsToWin = 10;
            }
            System.out.println(numLapsToWin);

            return numLapsToWin;

        }

        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            String textLevel = (String) cb.getSelectedItem();
            level = decodeLevel(textLevel);
        }
    }




    public static void main(String[] args){
        setup();

        src.src.MooMooMeadowsMultiplayer MooMooMeadowsMultiplayer = new src.src.MooMooMeadowsMultiplayer();
        MooMooMeadowsMultiplayer.setup();
        MooMooMeadowsMultiplayer.speedometer = new Speedometer(20, 20);

        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(WINWIDTH, WINHEIGHT);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.ipady = 15;
        gbc.ipadx = 50;


        startButton = new MyButton("START RACE");
        startButton.addActionListener(new StartGame(myPanel));
        setButtonAppearance(startButton);
        myPanel.add(startButton, gbc);


        gbc.insets = new Insets(10, 0, 0, 0);

        quitButton = new MyButton("QUIT");
        quitButton.addActionListener(new QuitGame());
        setButtonAppearance(quitButton);
        myPanel.add(quitButton, gbc);


        String[] laps = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"};
        lapMenu = new JComboBox<String>(laps);
        lapMenu.setSelectedIndex(2);
        lapMenu.addActionListener(new GameLevel());
        myPanel.add(lapMenu, gbc);

        System.out.println(lapMenu);
        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
// Disable focus traversal keys for the JComboBox

        bindKey(myPanel, "W");
        bindKey(myPanel, "S");
        bindKey(myPanel, "A");
        bindKey(myPanel, "D");
        bindKey(myPanel, "Q");


        bindKey(myPanel, "UP");
        bindKey(myPanel, "DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");
        bindKey(myPanel, "SPACE");






        myPanel.setBackground(CELESTIAL);
        appFrame.getContentPane().add(myPanel, "Center");
        appFrame.setVisible(true);






        Random rand  = new Random();
        int rand_int1 = rand.nextInt(3);
        if (rand_int1 == 2) {
            BackgroundMusic menu_theme = new BackgroundMusic("res/RainbowRoad/MarioKart64.wav");
            menu_theme.play();
        }
        else {
            BackgroundMusic menu_theme = new BackgroundMusic("res/RainbowRoad/Rainbow-Road-Mario-Kart-Wii.wav");
            menu_theme.play();



        }

        appFrame.createBufferStrategy(2);//2

    }

    public static class Speedometer {
        private int x, y;
        private Font font;

        public Speedometer(int x, int y) {
            this.x = x;
            this.y = y;
            this.font = new Font("Arial", Font.PLAIN, 18);
        }

        public void draw(Graphics g, double speed) {
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Speed: " + String.format("%.2f", speed), x, y);
        }
    }

    private static long startTime;
    private static long lapStartTime;
    private static long bestLapTime = Long.MAX_VALUE;
    private static long currentLapTime;
    private static int lapCount1 = 0;
    private static int lapCount2 = 0;


    private static Boolean endgame;
    private static Boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, wPressed, sPressed, aPressed, dPressed, qPressed;

    private static JButton startButton, quitButton;

    private static JComboBox<String> lapMenu;

    private static Color CELESTIAL = new Color(64, 224, 208);
    private static Color HIGHLIGHT = new Color(199, 199, 199);
    private static Color URANIAN = new Color(164, 210, 232);

    private static int XOFFSET, YOFFSET, WINWIDTH, WINHEIGHT;


    private static ImageObject p1; // player1 and player2 racecar object

    private static ImageObject2 p2; // player1 and player2 racecar object


//    private static ImageObject2 p2; // player1 and player2 racecar object

    private static double p1width, p1height, p1originalX, p1originalY, p1velocity, p2width, p2height, p2originalX, p2originalY, p2velocity, nitroFlamePNGWidth, nitroFlamePNGHeight, nitroFlameX, nitroFlameY;

    private static JFrame appFrame;

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static double RESPAWN_X = 1081 ; // Set the appropriate x-coordinate
    private static double RESPAWN_Y = 220; // Set the appropriate y-coordinate

    private static double RESPAWN_X2 = 1104 ; // Set the appropriate x-coordinate
    private static double RESPAWN_Y2 = 220; // Set the appropriate y-coordinate
    private static double LaptimeP1=0;
    private static double LaptimeP2=0;
    private static double bestLapP1=0;
    private static double bestLapP2=0;

    private static long lapStartTimeP1 = 0;
    private static long lapEndTimeP1 = 0;
    private static long lapEndTimeP2 = 0;

    private static long lapStartTimeP2 = 0;


    static boolean hasCrossedFinishLinep1 = false;
    static boolean hasCrossedFinishLinep2 = false;
    static boolean hasCrossedCheckPointp1 = false;
    static boolean hasCrossedCheckPointp2 = false;

    private static int level;

    private static int numLapsToWin = 3;

    private static BufferStrategy bs;
    private static BufferedImage OnTrack, OffTrack, finishline, checkpoint, player1, player2, nitroFlamePNG; // TODO: add player2

}