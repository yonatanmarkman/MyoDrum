package Yonatan;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.Arm;
import com.thalmic.myo.enums.WarmupState;
import com.thalmic.myo.enums.XDirection;

/**
 * Created by צח on 25/08/2016.
 */
public class collectData extends AbstractDeviceListener {
    private Vector3 gyroscope;
    private Quaternion rota;
    private Pose currentPose;
    private Vector3 acc;

    public collectData(collectData dataCollector) {
        gyroscope = dataCollector.getGyroscope();
        rota = dataCollector.getRota();
        currentPose = dataCollector.getCurrentPose();
        acc = dataCollector.getAcc();
    }

    public collectData(){

    }


    public Vector3 getGyroscope(){
        return gyroscope;
    }

    public Quaternion getRota(){
        return rota;
    }

    public Pose getCurrentPose(){
        return currentPose;
    }

    public Vector3 getAcc(){
        return acc;
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        rota = rotation;
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        currentPose = pose;
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
        acc = accel;
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
        gyroscope = gyro;
    }
}
