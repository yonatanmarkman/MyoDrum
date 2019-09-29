package Yonatan;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import java.util.ArrayList;

/**
 * Created by צח on 26/10/2016.
 */
public class RunProgramThread extends Thread {
    private ArrayList<collectData> ReferenceArray1 = new ArrayList<collectData>();
    private ArrayList<collectData> ReferenceArray2 = new ArrayList<collectData>();
    private ArrayList<collectData> ReferenceArray3 = new ArrayList<collectData>();
    private double Threshold1;
    private double Threshold2;
    private double Threshold3;
    private Hub hub;
    private DeviceListener dataCollector;
    static boolean repeatSound = false;
    public RunProgramThread(ArrayList<collectData> referenceArray1,ArrayList<collectData> referenceArray2,ArrayList<collectData> referenceArray3,double threshold1,double threshold2,double threshold3){
        ReferenceArray1 = referenceArray1;
        Threshold1 = threshold1;
        ReferenceArray2 = referenceArray2;
        Threshold2 = threshold2;
        ReferenceArray3 = referenceArray3;
        Threshold3 = threshold3;

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
        ArrayList<collectData> MovementArray1 = new ArrayList<collectData>();
        ArrayList<collectData> MovementArray2 = new ArrayList<collectData>();
        ArrayList<collectData> MovementArray3 = new ArrayList<collectData>();
        int countRepeat = 0;
        while (!interrupted()) {
            hub.run(1000 / 20);
            collectData dc = new collectData((collectData)dataCollector);
            updateArray(MovementArray1,dc,ReferenceArray1.size());
            updateArray(MovementArray2,dc,ReferenceArray2.size());
            updateArray(MovementArray3,dc,ReferenceArray3.size());
            System.out.println(MovementArray1);
            CalculationThread calThread = new CalculationThread(MovementArray1, ReferenceArray1, Threshold1);
            calThread.start();
            SecondCalculationThread secondCalThread = new SecondCalculationThread(MovementArray2, ReferenceArray2, Threshold2);
            secondCalThread.start();
            ThirdCalculationThread thirdCalThread = new ThirdCalculationThread(MovementArray3, ReferenceArray3, Threshold3);
            thirdCalThread.start();
        }
    }



    @Override
    public void interrupt() {
        super.interrupt();
    }

}
