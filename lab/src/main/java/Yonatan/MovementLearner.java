package Yonatan;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by yoni on 12-Aug-16.
 */
public class MovementLearner implements KeyListener {
    public static final String DRUM_SOUND = "src/main/resources/audio/Bass_Drum.wav";
    public static final String DRUM = "src/main/resources/audio/Crash_Drum.wav";
    public static final String SOUND = "src/main/resources/audio/Snare_Drum.wav";
    private ArrayList<collectData> MovementArray = new ArrayList<collectData>();
    private boolean startClicked = false;
    private JFrame mainFrame;

    private Hub hub;
    private DeviceListener dataCollector;

    public boolean isRecording = false;
    public boolean test = true;

    private Main main;

    public ArrayList<collectData> getMovementArray(){
        return MovementArray;
    }

    public MovementLearner() {
        this.main = new Main(this);
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
        mainFrame = new JFrame("Myo MovementLearner");
        mainFrame.setSize(300, 300);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                test = false;
                System.exit(0);
            }
        });

        JButton startRecordingButton;
        JButton endRecordingButton;

        startRecordingButton = new JButton("Start Recording Movement");
        endRecordingButton = new JButton("Stop Recording Movement");

        startRecordingButton.addActionListener(e -> {
            if (startRecordingButton.isEnabled()) {
                endRecordingButton.setEnabled(true);
                startRecordingButton.setEnabled(false);
                startClicked = true;
            }
        });

        endRecordingButton.addActionListener(e -> {
            if (endRecordingButton.isEnabled()) {
                startRecordingButton.setEnabled(true);
                endRecordingButton.setEnabled(false);
                startClicked = false;
            }
        });



        mainFrame.addKeyListener(this);

        mainFrame.setBackground(Color.GREEN);
        mainFrame.setVisible(true);
    }

    public void updateDataArray(boolean isRecording) {
        try {
            if (isRecording) {
                hub.run(1000 / 20);
                collectData dc = new collectData((collectData)dataCollector);
                MovementArray.add(dc);
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean b = false;

    public static void main(String[] args) {
        MovementLearner m = new MovementLearner();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    boolean printed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'q') {
            isRecording = true;
            if (!printed) {
                System.out.println("started recording");
                printed = true;
            }
            updateDataArray(isRecording);
        }

        if (e.getKeyChar() == 'a') {
            SoundPlayer.playSound(new File(DRUM_SOUND));
        }

        if (e.getKeyChar() == 'z') {
            SoundPlayer.playSound(new File(SOUND));
        }

        if (e.getKeyChar() == 's') {
            SoundPlayer.playSound(new File(DRUM));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'q') {
            isRecording = false;
            test = false;
            System.out.println("ended recording");
            System.out.println("Data array: \n");
            for (collectData d :
                    MovementArray) {
                System.out.println(d.getGyroscope() + ", ");
                System.out.println(d.getRota() + ", ");
                System.out.println(d.getCurrentPose() + ", ");
                System.out.println(d.getAcc() + "'\n");
            }
            main.cont();
        }
    }
}