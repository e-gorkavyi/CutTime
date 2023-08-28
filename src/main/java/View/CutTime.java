package View;

import Controller.DataRefreshListener;
import Model.Model;
import com.formdev.flatlaf.FlatLightLaf;
import org.kabeja.parser.ParseException;

import javax.swing.*;
import java.io.IOException;

public class CutTime implements DataRefreshListener {
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
    private static Model model = new Model();

    @Override
    public void onDataChanged(int dataCount) {
        System.out.println("On Change!");
//        reading data from Model and refresh labels
        this.objectsNum.setText(model.getData().get("objectsNum"));
        this.objectsLen.setText(model.getData().get("totalLength"));
        this.stopsNum.setText(model.getData().get("stopsNum"));
        this.idleLen.setText(model.getData().get("idleRunLength"));
        this.headIdleTime.setText(model.getData().get("headUpTime"));
        this.idleTime.setText(model.getData().get("idleRunTime"));
        this.workTime.setText(model.getData().get("workRunTime"));
        this.totalTime.setText(model.getData().get("totalTime"));
    }

    public CutTime() {
        closeButton.addActionListener(actionEvent -> {
            exit();
        });
        model.addListener(this);
    }

    public static void main(String[] args) throws ParseException, IOException {
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Plotter cut time");
        frame.setContentPane(new CutTime().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        model.calculate("carton"); //in work args[0]
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
