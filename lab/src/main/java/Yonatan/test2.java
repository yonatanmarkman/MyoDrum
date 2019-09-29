package Yonatan;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;


import static Yonatan.Main.min;

/**
 * Created by צח on 17/09/2016.
 */
public class test2 {
    private ArrayList<collectData> ReferenceArray = new ArrayList<collectData>();
    private double Threshold;
    private Hub hub;
    private DeviceListener dataCollector;
    private boolean isRecording = false;
    private boolean startClicked = false;
    private int caseNum;
    private test3 p;

    private JTextArea log;

    public test2(int caseNumber){
        caseNum = caseNumber;

        log = new JTextArea(15, 20);
        log.setLineWrap(true);
        log.setForeground(Color.BLUE);
        log.setEditable(false);
        JScrollPane scroller = new JScrollPane(log);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //other gui stuff
        DefaultCaret caret = (DefaultCaret)log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        hub = new Hub("com.example.hello-myo");
        System.out.println("Attempting to find a Myo...");
        Myo myo = hub.waitForMyo(10000);

        if (myo == null) {
            throw new RuntimeException("Unable to find a Myo!");
        }

        System.out.println("Connected to a Myo armband!");

        dataCollector = new collectData();
        hub.addListener(dataCollector);

        //GUI and key interface
        JFrame mainFrame = new JFrame("Recording Window");
        mainFrame.setSize(600, 350);
        mainFrame.setLayout(new FlowLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        textPanel.setLayout(new FlowLayout());
        buttonPanel = new JPanel();
        //buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setLayout(new GridLayout(2,3));

        startRecordingButton = new JButton("Start Recording Movement");
        endRecordingButton = new JButton("Stop Recording Movement");
        clearMovement = new JButton("Clear");
        repeatMovement = new JButton("Repeat the movement");
        determineThreshold = new JButton("Determine ThresholdValue");
        sync = new JButton("Sync");

        endRecordingButton.setEnabled(false);
        repeatMovement.setEnabled(false);

        AssignFunctionsToButtons();

        RecordAction recordAction = new RecordAction();
        releasedRecordAction r = new releasedRecordAction();

        mainPanel.getInputMap(mainPanel.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( " ENTER " ),
                "doRecordAction" );

        mainPanel.getInputMap(mainPanel.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke( "released ENTER " ),
                "doreleasedRecordAction" );

        mainPanel.getActionMap().put("doRecordAction", recordAction );

        mainPanel.getActionMap().put("doreleasedRecordAction", r );

        buttonPanel.add(startRecordingButton);
        buttonPanel.add( endRecordingButton );
        buttonPanel.add( clearMovement );
        buttonPanel.add( repeatMovement );
        buttonPanel.add( determineThreshold );
        buttonPanel.add( sync );
        mainPanel.add(buttonPanel);
        textPanel.add(scroller);
        mainPanel.add(textPanel);

        mainFrame.add(mainPanel);
        mainFrame.setLocationRelativeTo(null);
       // mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public double getThreshold(){
        return Threshold;
    }

    public ArrayList<collectData> getReferenceArray(){
        return ReferenceArray;
    }

    public void updateDataArray(boolean isRecording) {
        try {
            if (isRecording) {
                hub.run(1000 / 20);
                collectData dc = new collectData((collectData)dataCollector);
                ReferenceArray.add(dc);
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    JPanel mainPanel;
    JPanel textPanel = new JPanel();

    JButton startRecordingButton;
    JButton endRecordingButton;
    JButton clearMovement;
    JButton repeatMovement;
    JButton determineThreshold;
    JButton sync;

    JPanel buttonPanel;




    private void AssignFunctionsToButtons(){
        startRecordingButton.addActionListener(e -> {
            if (startRecordingButton.isEnabled()) {
                endRecordingButton.setEnabled(true);
                startRecordingButton.setEnabled(false);
                startClicked = true;
                //update log
                logPrint("Press Enter to start recording.");
            }
        });

        endRecordingButton.addActionListener(e -> {
            if (endRecordingButton.isEnabled()) {
                endRecordingButton.setEnabled(false);
                repeatMovement.setEnabled(true);
                startClicked = false;
                //update log
                logPrint("Recording saved.");
            }
        });

        clearMovement.addActionListener(e -> {
            if (clearMovement.isEnabled()) {
                ReferenceArray.clear();
                startRecordingButton.setEnabled(true);
                endRecordingButton.setEnabled(false);
                repeatMovement.setEnabled(false);
                //update log
                logPrint("Recording saved.");
            }
        });

        repeatMovement.addActionListener(e -> {
            if (repeatMovement.isEnabled()) {
                repeatMovement.setEnabled(false);
                RepeatMovementFunc();
                //update log
                logPrint("Press Enter to repeat movement.");
            }
        });

        determineThreshold.addActionListener(e -> {
            if (determineThreshold.isEnabled()) {
                p = new test3(ReferenceArray,Threshold,caseNum);
                //update log
                logPrint("ThresholdValue window opening...");
            }
        });

        sync.addActionListener(e -> {
            if (sync.isEnabled()) {
                Threshold = p.getThresholdValue();
                //update log
                logPrint("ThresholdValue is : "+Threshold);
            }
        });
    }

    private void RepeatMovementFunc(){
        int count2 = 0;
        ArrayList<collectData> temp = new ArrayList<collectData>();
        while (count2 < 100) {
            CalculationThread calThread;
            hub.run(1000 / 20);
            collectData dc = new collectData((collectData)dataCollector);
            count2++;
            updateArray(temp,dc,ReferenceArray.size());
            System.out.println(temp);
            calThread = new CalculationThread(temp,ReferenceArray);
            calThread.start();
        }
        Threshold = min;
        System.out.println(Threshold);
    }

    private void updateArray(ArrayList<collectData> arr,collectData data,int size){
        if(arr.size() == size)
        {
            arr.remove(0);
            arr.add(data);
        }
        else {
            arr.add(data);
        }
    }

    boolean printed = false;

    class RecordAction extends AbstractAction {
        public void actionPerformed( ActionEvent e ) {
            if(startClicked){
                isRecording = true;
                if (!printed) {
                    logPrint("Recording...");
                    printed = true;
                }
                updateDataArray(isRecording);
            }
        }

    }

    class releasedRecordAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent e )
        {
            logPrint("Ended recording.\nPress 'End recording' to save result.");
            if(!startClicked)
            {
                isRecording = false;
                //logPrint("Ended recording.\nPress 'End recording' to save result.");
                //Data array print to console
                System.out.println("Data array: \n");
                for (collectData d :
                        ReferenceArray) {
                    System.out.println(d.getGyroscope() + ", ");
                    System.out.println(d.getRota() + ", ");
                    System.out.println(d.getCurrentPose() + ", ");
                    System.out.println(d.getAcc() + "'\n");
                }
            }
        }

    }

    public void logPrint(String str){
        log.setText(log.getText()+str+'\n');
    }

    //FOR TESTING ONLY
    public static void main(String[] args) {
        new test2(1);
    }
}
