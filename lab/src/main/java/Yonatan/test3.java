package Yonatan;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by צח on 22/10/2016.
 */
public class test3 {
    private ArrayList<collectData> ReferenceArray = new ArrayList<collectData>();
    private DetermineThersholdThread determineThersholdThread;
    static double ThresholdValue;
    private int caseNum;

    //GUI stuff
    JFrame mainFrame;
    JPanel buttonPanel;
    JPanel labelPanel;
    JButton plus;
    JButton minus;
    JButton Determine;
    JTextField thresholdField;

    public test3(ArrayList<collectData> referenceArray, double Thres, int caseNumber) {
        ThresholdValue = Thres;
        ReferenceArray = referenceArray;
        caseNum = caseNumber;

        //GUI and key interface
        mainFrame = new JFrame("Myo Drum Program");
        mainFrame.setSize(400, 120);
        mainFrame.setLayout(new GridLayout(2, 1));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());

        minus = new JButton("-");
        plus = new JButton("+");
        Determine = new JButton("Determine Thershold");
        thresholdField = new JTextField(20);
        thresholdField.setText(String.valueOf(ThresholdValue));

        plus.addActionListener(e -> {
            if (plus.isEnabled()) {
                ThresholdValue = ThresholdValue + 0.1;
                thresholdField.setText(String.valueOf(ThresholdValue));
            }
        });

        minus.addActionListener(e -> {
            if (minus.isEnabled()) {
                ThresholdValue = ThresholdValue - 0.1;
                thresholdField.setText(String.valueOf(ThresholdValue));
            }
        });

        Determine.addActionListener(e -> {
            if (Determine.isEnabled()) {
                ThresholdValue = Double.parseDouble(thresholdField.getText());
                if(caseNum != -1)
                    determineThersholdThread.interrupt();
                else //for testing gui.
                    System.out.println("thresh is: "+getThresholdValue());
            }
        });

        buttonPanel.add(minus);
        buttonPanel.add(plus);
        buttonPanel.add(Determine);
        labelPanel.add(thresholdField);

        mainFrame.add(buttonPanel);
        mainFrame.add(labelPanel,1);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        if (caseNum != -1) { //for testing gui
            ThresholdValue = Double.parseDouble(thresholdField.getText());
            determineThersholdThread = new DetermineThersholdThread(ReferenceArray, caseNum);
            determineThersholdThread.start();
        }
    }

    public double getThresholdValue() {
        return ThresholdValue;
    }

    //FOR TESTING ONLY
    public static void main(String[] args) {
        new test3(null, 0, -1);
    }
}
