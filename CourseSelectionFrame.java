import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CourseSelectionFrame extends JFrame {

    public CourseSelectionFrame() {
        setTitle("Course Selection");
        setSize(1280, 840); // Increased width to accommodate more buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton RainbowRoadButton = new JButton("Rainbow Road");
        JButton RainbowRoadMultiplayerButton = new JButton("Rainbow Road Multiplayer");
        JButton MooMooMeadowsMultiplayerMultiplayerButton = new JButton("Moo Moo Meadows Multiplayer");

        JButton MooMooMeadowsMultiplayerButton = new JButton("Moo Moo Meadows");
        JButton MushroomGeorgeButton = new JButton("Mushroom George");
        JButton CBUTrackButton = new JButton("CBU Track!");


        RainbowRoadMultiplayerButton.addActionListener(e -> openRainbowRoadMultiplayer("res/rainbowroad/largerrainbowroadmultiplayer.png"));

        CBUTrackButton.addActionListener(e -> openCBUTrackFrame("res/rainbowroad/largerrainbowroad.png"));

        RainbowRoadButton.addActionListener(e -> openRacer2DFrame("res/rainbowroad/largerrainbowroad.png"));

        MooMooMeadowsMultiplayerButton.addActionListener(e -> openMooMooMeadowsMultiplayerFrame("res/MooMooMeadows/MooMooMeadows.png"));

        MushroomGeorgeButton.addActionListener(e -> openMushroomGorgeFrame("res/rainbowroad/largerrainbowroad.png"));

        MushroomGeorgeButton.addActionListener(e -> openMushroomGorgeFrame("res/rainbowroad/largerrainbowroad.png"));



        JPanel panel = new JPanel();
        panel.add(RainbowRoadButton);

        panel.add(RainbowRoadMultiplayerButton);

        panel.add(MooMooMeadowsMultiplayerButton);
        panel.add(MooMooMeadowsMultiplayerMultiplayerButton);

        panel.add(MushroomGeorgeButton);
        panel.add(CBUTrackButton);




        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openRacer2DFrame(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the Racer2D class using reflection
                Class<?> racer2DClass = Class.forName("src.src.RainbowRoad");
                racer2DClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }

    private void openMooMooMeadowsMultiplayerFrame(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the Moo Moo MeadowsFrame class using reflection
                Class<?> MooMooMeadowsMultiplayerClass = Class.forName("src.src.MooMooMeadowsMultiplayer");
                MooMooMeadowsMultiplayerClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }


    private void openMushroomGorgeFrame(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the Racer2D class using reflection
                Class<?> MushroomGorgeClass = Class.forName("src.src.MushroomGorge");
                MushroomGorgeClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }

    private void openCBUTrackFrame(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the CBU Track class using reflection
                Class<?> CBUTrackClass = Class.forName("src.src.CBUTrack");
                CBUTrackClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }

    private void openRainbowRoadMultiplayer(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the CBU Track class using reflection
                Class<?> RainbowRoadMuliplayerClass = Class.forName("src.src.RainbowRoadMultiplayer");
                RainbowRoadMuliplayerClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(CourseSelectionFrame::new);
    }
}
