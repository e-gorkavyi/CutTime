import Controller.Controller;
import Model.Model;
import com.formdev.flatlaf.FlatLightLaf;
import org.kabeja.parser.ParseException;

import javax.swing.*;

public class CutTime {
    private JPanel mainPanel;
    private JButton closeButton;
    private JButton preferences;
    private JLabel objectsNum;
    private JLabel objectsLen;
    private JLabel stopsNum;
    private JLabel idleLen;
    private JLabel headIdleTime;
    private JLabel idleTime;
    private JLabel workTime;
    private JLabel totalTime;

//    private static ResourceBundle localeBundle = ResourceBundle.getBundle("localeBundle");

    public CutTime() {
        closeButton.addActionListener(actionEvent -> {
            Controller.exit();
        });
    }

    public static void main(String[] args) throws ParseException {
        Model model = new Model();
        model.readDXF("111.dxf"); //in work args[0]

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
