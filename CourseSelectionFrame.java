import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CourseSelectionFrame extends JFrame {

    public CourseSelectionFrame() {
        setTitle("Course Selection");
        setSize(400, 200); // Increased width to accommodate more buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton rainbowRoadButton = new JButton("Rainbow Road");
        JButton MooMooMeadowsButton = new JButton("Moo Moo Meadows");
        JButton MushroomGeorgeButton = new JButton("Mushroom George");
        JButton CBUTrackButton = new JButton("CBU Track!");

        CBUTrackButton.addActionListener(e -> openCBUTrackFrame("res/largerrainbowroad.png"));
        rainbowRoadButton.addActionListener(e -> openRacer2DFrame("res/largerrainbowroad.png"));
        MooMooMeadowsButton.addActionListener(e -> openMooMooMeadowsFrame("res/largerrainbowroad.png"));
        MushroomGeorgeButton.addActionListener(e -> openMushroomGorgeFrame("res/largerrainbowroad.png"));


        JPanel panel = new JPanel();
        panel.add(rainbowRoadButton);
        panel.add(MooMooMeadowsButton);
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
                Class<?> racer2DClass = Class.forName("src.src.Racer2D");
                racer2DClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose(); // Close the course selection frame
        });
    }

    private void openMooMooMeadowsFrame(String trackImagePath) {
        // Open the main game frame with the specified track image
        SwingUtilities.invokeLater(() -> {
            try {
                // Run the Moo Moo MeadowsFrame class using reflection
                Class<?> MooMooMeadowsClass = Class.forName("src.src.MooMooMeadows");
                MooMooMeadowsClass.getMethod("main", String[].class).invoke(null, (Object) null);
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(CourseSelectionFrame::new);
    }
}
