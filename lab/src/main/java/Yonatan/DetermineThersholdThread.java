package Yonatan;

import com.thalmic.myo.*;

import java.util.ArrayList;

import static Yonatan.test3.ThresholdValue;

/**
 * Created by צח on 23/10/2016.
 */
public class DetermineThersholdThread extends Thread {
    private ArrayList<collectData> ReferenceArray = new ArrayList<collectData>();
    private Hub hub;
    private DeviceListener dataCollector;
    private int caseNum;
    static boolean repeatSound = false;

    public DetermineThersholdThread(ArrayList<collectData> referenceArray,int caseNumber){
        ReferenceArray = referenceArray;
        caseNum = caseNumber;

        hub = new Hub("com.example.hello-myo");
        System.out.println("Attempting to find a Myo...");
        Myo myo = hub.waitForMyo(10000);

        if (myo == null) {
            throw new RuntimeException("Unable to find a Myo!");
        }

        System.out.println("Connected to a Myo armband!");

        dataCollector = new collectData();
        hub.addListener(dataCollector);
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

    @Override
    public void run() {
        ArrayList<collectData> MovementArray = new ArrayList<collectData>();
        int countRepeat = 0;
        while (!interrupted()) {
            System.out.println(caseNum);
            hub.run(1000 / 20);
            collectData dc = new collectData((collectData)dataCollector);
            updateArray(MovementArray,dc,ReferenceArray.size());
            System.out.println(MovementArray);
            if (caseNum == 1) {
                CalculationThread calThread = new CalculationThread(MovementArray, ReferenceArray, ThresholdValue);
                calThread.start();
            }
            if (caseNum == 2) {
                SecondCalculationThread secondCalThread = new SecondCalculationThread(MovementArray, ReferenceArray, ThresholdValue);
                secondCalThread.start();
            }
            if (caseNum == 3) {
                ThirdCalculationThread thirdCalThread = new ThirdCalculationThread(MovementArray, ReferenceArray, ThresholdValue);
                thirdCalThread.start();
            }
        }
    }



    @Override
    public void interrupt() {
        super.interrupt();
    }
}
