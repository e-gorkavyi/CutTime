import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class CutTime {
    private JPanel mainPanel;
    private JButton closeButton;
    private JButton preferences;

    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Plotter cut time");
        frame.setContentPane(new CutTime().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
