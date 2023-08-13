package Controller;

import Model.Model;
import com.formdev.flatlaf.FlatLightLaf;
import org.kabeja.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class CutTimeController implements DataRefreshListener {
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

    @Override
    public void onDataChanged(int dataCount) {
//        reading data from Model and refresh labels
    }

    public CutTimeController() {
        closeButton.addActionListener(actionEvent -> {
            exit();
        });
    }

    public static void main(String[] args) throws ParseException, IOException {
        Model model = new Model();
        model.calculate("carton"); //in work args[0]

        FlatLightLaf.setup();

        JFrame frame = new JFrame("Plotter cut time");
        frame.setContentPane(new CutTimeController().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void listenerInit() {
        Model ctl = new Model();
        ctl.addListener(this);
    }

    public void setObjNum(int num) {
        objectsNum.setText(String.valueOf(num));
    }

    public static void exit() {
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
