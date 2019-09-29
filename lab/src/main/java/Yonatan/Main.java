package Yonatan;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.example.DataCollector;

import java.util.ArrayList;

/**
 * Created by צח on 29/08/2016.
 */
public class Main {
    private MovementLearner m;
    private ArrayList<collectData> ReferenceArray = new ArrayList<collectData>();
    static double min;
    static boolean flag = true;
    static boolean flag2 = true;

    public Main(MovementLearner m){
        this.m = m;
    }

    public void updateArray(collectData data,int size){
        if(ReferenceArray.size() == size)
        {
            ReferenceArray.remove(0);
            ReferenceArray.add(data);
        }
        else {
            ReferenceArray.add(data);
        }
    }

    public static void updatesArray(ArrayList<collectData> arr,collectData data,int size){
        if(arr.size() == size)
        {
            arr.remove(0);
            arr.add(data);
        }
        else {
            arr.add(data);
        }
    }

    public void cont(){
        try {
            Hub hub = new Hub("com.example.hello-myo");

            System.out.println("Attempting to find a Myo...");
            Myo myo = hub.waitForMyo(10000);

            if (myo == null) {
                throw new RuntimeException("Unable to find a Myo!");
            }

            System.out.println("Connected to a Myo armband!");
            DeviceListener dataCollector = new collectData();
            hub.addListener(dataCollector);

            int count = 0;
            int count2 = 0;

            ArrayList<collectData> temp = new ArrayList<collectData>();
            double avg = 0;
            for (int i=0;i<5;i++){ // 5 is the number we repeat on the move to for syncing the thresholdField
                count2 = 0;
                while (count2 < 100) {
                    CalculationThread calThread;
                    hub.run(1000 / 20);
                    collectData dc = new collectData((collectData)dataCollector);
                    count2++;
                    updatesArray(temp,dc,m.getMovementArray().size());
                    System.out.println(temp);
                    calThread = new CalculationThread(temp,m.getMovementArray());
                    calThread.start();
                }
                flag = true;
                System.out.println("i = " + i+1);
                System.out.println("i = " + i+1);
                System.out.println("i = " + i+1);
                System.out.println("i = " + i+1);
                System.out.println("min = " + min);
                avg = avg + min;
            }

            avg = avg/5;
            System.out.println("avg= " + avg);


            flag2 = false;
            System.out.println("begin");

            while (count < 3000) {
                CalculationThread calThread;
                hub.run(1000 / 20);
                collectData dc = new collectData((collectData)dataCollector);
                count++;
                this.updateArray(dc,m.getMovementArray().size());
                System.out.println(this.ReferenceArray);
                calThread = new CalculationThread(this.ReferenceArray,m.getMovementArray(),avg);
                calThread.start();
            }
        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        MovementLearner f = new MovementLearner();
    }
}
