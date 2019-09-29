package Yonatan;


import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;

import java.io.File;
import java.util.ArrayList;

import static Yonatan.DetermineThersholdThread.repeatSound;
import static Yonatan.Main.flag;
import static Yonatan.Main.flag2;
import static Yonatan.Main.min;

/**
 * Created by צח on 29/08/2016.
 */
public class CalculationThread extends Thread {
    public static final String DUM_SOUND = "src/main/resources/audio/loflngtm.wav";
    private ArrayList<collectData> MovementArray = new ArrayList<collectData>();
    private ArrayList<collectData> ReferenceArray = new ArrayList<collectData>();
    private double Threshold;
    static double res;

    public CalculationThread(ArrayList<collectData> movement, ArrayList<collectData> reference, double thres){
        MovementArray = movement;
        ReferenceArray = reference;
        Threshold = thres;
    }

    public CalculationThread(ArrayList<collectData> movement, ArrayList<collectData> reference){
        MovementArray = movement;
        ReferenceArray = reference;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            res = calcDistance();
            if (res == -1)
                interrupt();
            System.out.println(res);
            if(flag2) {
                if (res != -1 && flag) {
                    min = res;
                    flag = false;
                } else if (res < min && res != -1) {
                    min = res;
                }
            }
            if(res < Threshold)
            {
                if(!repeatSound) {
                    SoundPlayer.playSound(new File(DUM_SOUND));
                    repeatSound = true;
                }
            }
            else
                repeatSound = false;
            interrupt();
        }
    }

    private double calcDistance(){
        if(MovementArray.size() != ReferenceArray.size())
            return -1;
        double diff = 0;
        for (int i=0;i<ReferenceArray.size();i++){
            diff += calcDif(ReferenceArray.get(i).getRota(),MovementArray.get(i).getRota());
        }
        return diff;
    }

    private double calcDif(Vector3 vec1, Vector3 vec2) {
        double sum;
        sum = Math.abs(vec1.getX()-vec2.getX()) + Math.abs(vec1.getY()-vec2.getY()) + Math.abs(vec1.getZ()-vec2.getZ());
        return sum;
    }

    private double calcDif(Quaternion vec1, Quaternion vec2) {
        double sum;
        sum = Math.abs(vec1.getX()-vec2.getX()) + Math.abs(vec1.getY()-vec2.getY()) + Math.abs(vec1.getZ()-vec2.getZ()) + Math.abs(vec1.getW()-vec2.getW());
        return sum;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }


}
