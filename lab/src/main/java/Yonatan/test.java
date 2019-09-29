package Yonatan;

/**
 * Created by צח on 17/09/2016.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yoni on 07-Sep-16.
 */
public class test {
    //Graphic User Interface data
    private test2 firstMovement;
    private test2 secondMovement;
    private test2 thirdMovement;
    private RunProgramThread prog;

    private JFrame mainFrame;

    private JLabel textLabel;

    private BufferedImage drumImage;
    private JLabel drumImageLABEL;

    private JButton first;
    private JButton second;
    private JButton third;
    private JButton start;
    private JButton stop;

    private JPanel buttonPanel;

    private ArrayList<collectData> MovementArray = new ArrayList<collectData>(); /* movement array from user */


    public test() {
        //GUI and key interface
        mainFrame = new JFrame("Myo Drum Program");
        mainFrame.setSize(500, 500);

        mainFrame.setLayout(new FlowLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        buttonPanel = new JPanel();
        //buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new GridLayout(3,2));


        first = new JButton("Record First Movement");
        second = new JButton("Record Second Movement");
        third = new JButton("Record Third Movement");
        start = new JButton("Start Playing");
        stop = new JButton("Stop Playing");

        first.addActionListener(e -> {
            if (first.isEnabled()) {
                first.setEnabled(false);
                firstMovement = new test2(1);
            }
        });

        second.addActionListener(e -> {
            if (second.isEnabled()) {
                second.setEnabled(false);
                secondMovement = new test2(2);
            }
        });

        third.addActionListener(e -> {
            if (third.isEnabled()) {
                third.setEnabled(false);
                thirdMovement = new test2(3);
            }
        });

        start.addActionListener(e -> {
            if (start.isEnabled()) {
                prog = new RunProgramThread(
                        firstMovement.getReferenceArray(),
                        secondMovement.getReferenceArray(),
                        thirdMovement.getReferenceArray(),
                        firstMovement.getThreshold(),
                        secondMovement.getThreshold(),
                        thirdMovement.getThreshold());
                prog.start();
            }
        });

        stop.addActionListener(e -> {
            if (stop.isEnabled()) {
                prog.interrupt();
            }
        });

        try {
            drumImage = ImageIO.read(new File("src/main/resources/photos/drums.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        drumImageLABEL = new JLabel(new ImageIcon(drumImage));

        //textLabel = new JLabel("Myo Drum Program");

        mainFrame.add(drumImageLABEL);
        //mainFrame.add(textLabel);
        buttonPanel.add(first);
        buttonPanel.add(start);
        buttonPanel.add(second);
        buttonPanel.add(stop);
        buttonPanel.add(third);
        mainFrame.add(buttonPanel);
        mainFrame.setBackground(Color.WHITE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        test p = new test();
    }


}
