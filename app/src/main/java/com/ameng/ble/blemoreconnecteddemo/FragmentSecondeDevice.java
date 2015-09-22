package com.ameng.ble.blemoreconnecteddemo;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ameng.ble.custom.BodyDataParser;
import com.ameng.ble.custom.DeviceListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ameng on 15/7/31.
 */
public class FragmentSecondeDevice extends Fragment implements DeviceListener {
    private TextView emptyText_2;

    private TextView data_text_2, ecg_2, heart_rate_2, rr_2, hrv_2, breathing_rate_2,
            breathestreng_2, muscle_m_upper_2, muscle_m_lower_2, muscle_i_upper_2,
            muscle_i_lower_2, step_2, calories_2, state_2, gps_2, speed_2, altitude_2;

    private ScrollView device_view_2;

    private Handler handler = new Handler();

    private SimpleDateFormat sdFormat;

    private int currentIndexDevice = 1;

    private int ECG_NUM = 0;

    private int MUSCLE_M_UPPER = 0;

    private int MUSCLE_M_LOSER = 0;

    private long startTime;

    private long endTime;

    private boolean isHasStartTime = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        sdFormat = new SimpleDateFormat("yyyy-MM-dd,hh:mm:ss.SSS");
    }

    private void initView() {
        emptyText_2 = (TextView) getView().findViewById(R.id.no_device_txt_2);
        device_view_2 = (ScrollView) getView().findViewById(R.id.device_layout_2);

        data_text_2 = (TextView) getView().findViewById(R.id.data_txt_2);
        ecg_2 = (TextView) getView().findViewById(R.id.ecg_txt_2);
        heart_rate_2 = (TextView) getView().findViewById(R.id.heart_rate_txt_2);
        rr_2 = (TextView) getView().findViewById(R.id.rr_txt_2);
        hrv_2 = (TextView) getView().findViewById(R.id.hrv_txt_2);
        breathing_rate_2 = (TextView) getView().findViewById(R.id.breathing_rate_txt_2);
        breathestreng_2 = (TextView) getView().findViewById(R.id.breathe_streng_txt_2);
        muscle_m_upper_2 = (TextView) getView().findViewById(R.id.muscle_m_upper_txt_2);
        muscle_m_lower_2 = (TextView) getView().findViewById(R.id.muscle_m_lower_txt_2);
        muscle_i_upper_2 = (TextView) getView().findViewById(R.id.muscle_i_upper_txt_2);
        muscle_i_lower_2 = (TextView) getView().findViewById(R.id.muscle_i_lower_txt_2);
        step_2 = (TextView) getView().findViewById(R.id.step_counter_txt_2);
        calories_2 = (TextView) getView().findViewById(R.id.calories_txt_2);
        state_2 = (TextView) getView().findViewById(R.id.state_text_2);
        gps_2 = (TextView) getView().findViewById(R.id.gps_txt_2);
        speed_2 = (TextView) getView().findViewById(R.id.speed_txt_2);
        altitude_2 = (TextView) getView().findViewById(R.id.altitude_txt_2);
    }

    public void connectBle(int tag, BluetoothDevice device) {
        ECG_NUM = 0;
        MUSCLE_M_UPPER = 0;
        MUSCLE_M_LOSER = 0;
        startTime = 0;
        endTime = 0;

        currentIndexDevice = tag;
        data_text_2.setVisibility(View.GONE);
        MyApplication.getApplication().connectedDeviceList.get(tag).setDeviceListener(this);
        MyApplication.getApplication().connectedDeviceList.get(tag).setBluetoothDevice(device);
        MyApplication.getApplication().connectedDeviceList.get(tag).connect();
    }


    public boolean isConnected(int tag) {
        return MyApplication.getApplication().connectedDeviceList.get(tag).isConnected();
    }

    public void disconnected(int tag) {
        endTime = System.currentTimeMillis();
        //MyApplication.getApplication().connectedDeviceList.get(tag).setDeviceListener(null);
        MyApplication.getApplication().connectedDeviceList.get(tag).close();
       // MyApplication.getApplication().connectedDeviceList.get(tag).disconnect();
    }

    public void close(int tag) {
        if (isConnected(tag)) {
            MyApplication.getApplication().connectedDeviceList.get(tag).setDeviceListener(null);
            MyApplication.getApplication().connectedDeviceList.get(tag).close();
        }

        emptyText_2.setVisibility(View.VISIBLE);
        device_view_2.setVisibility(View.GONE);
    }

    @Override
    public void onConnected(final int tag, final BluetoothDevice device) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), device.getName() + "connected success" + "设备" + (tag + 1) + "连接成功~~~~", Toast.LENGTH_LONG).show();
                emptyText_2.setVisibility(View.GONE);
                device_view_2.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDisconnected(int tag, BluetoothDevice device) {
        //MyApplication.getApplication().connectedDeviceList.get(tag).connect();


    }

    @Override
    public void onCloseBleGatt(int tag) {
        final long l = (endTime - startTime) / 1000;
//        long day = l / (24 * 60 * 60 * 1000);
//        long hour = (l / (60 * 60 * 1000) - day * 24);
//        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        final long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        isHasStartTime = false;

        handler.post(new Runnable() {
            @Override
            public void run() {
                data_text_2.setVisibility(View.VISIBLE);
                data_text_2.setText("结束时间:" + endTime + ", 心电监护共接收到" + ECG_NUM + "次" + "需要" + l + "秒, " + "肌肉上身共接收到" + MUSCLE_M_UPPER + "次， " + "需要" + l + "秒, " + "肌肉下身共接收到" + MUSCLE_M_LOSER + "次， " + "需要" + l + "秒");
            }
        });
    }

    @Override
    public void onConn(int tag, BluetoothDevice device) {

    }

    @Override
    public void onGetDeviceName(int tag, String deviceName, BluetoothDevice device) {

    }

    @Override
    public void onGetBattery(int tag, int battery, BluetoothDevice device) {

    }

    @Override
    public void onGetRssi(final int tag, final int rssi, BluetoothDevice device) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "设备" + tag + 1 + "当前信号强度为：" + rssi, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onGetValue(int tag, int value, BluetoothDevice device) {

    }

    @Override
    public void onSendSuccess(int tag, int value, BluetoothDevice device) {

    }


    @Override
    public void onDataAvailable(int tag, byte[] data) {
        BodyDataParser.BodyDataMeta dm;
        List<BodyDataParser.BodyDataMeta> metaList = BodyDataParser.parseData(data);
        if (metaList == null) {
            //isTimeStamp
            return;
        }

        if (!isHasStartTime) {
            startTime = System.currentTimeMillis();
            isHasStartTime = true;
        }

        for (int i = 0; i < metaList.size(); i++) {
            dm = metaList.get(i);
            switch ((int) dm.item) {
                case BodyDataParser.CODE_ECG_MONITOR:
                    mECGMonitorCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_HEART_RATE:
                    mHeartRateCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_RR_INTERVALS:
                    mRRIntervalsCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_HRV_RATIO:
                    mHRVRatioCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_BREATHING_RATE:
                    mBreathingRateCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_BREATHE_STRENGTH:
                    mBreatheStrengthCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_MUSCLE_MONITOR_UPPER:
                    mMuscleMonitorUpperCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_MUSCLE_MONITOR_LOWER:
                    mMuscleMonitorLowerCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_MUSCLE_IMBALANCE_UPPER:
                    mMuscleImbalanceUpperCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_MUSCLE_IMBALANCE_LOWER:
                    mMuscleImbalanceLowerCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_STEP_COUNTER:
                    mStepCounterCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_CALORIES:
                    mCaloriesCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_STATE:
                    mStateCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_GPS:
                    mGpsCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_SPEED:
                    mSpeedCallback(tag, dm);
                    break;
                case BodyDataParser.CODE_ALTITUDE:
                    mAltitudeCallback(tag, dm);
                    break;
            }
        }
    }


    private void mECGMonitorCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentIndexDevice == tag) {
                    ECG_NUM++;
                    ecg_2.setVisibility(View.VISIBLE);
                    ecg_2.setText("开始计算的时间:" + startTime + ", 心电监护数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mHeartRateCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    heart_rate_2.setVisibility(View.VISIBLE);
                    heart_rate_2.setText("心率数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)) + ", 心率变化:" + (Integer) (dm.data[0] & 0xFF));
                }
            }
        });
    }

    private void mRRIntervalsCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    rr_2.setVisibility(View.VISIBLE);
                    rr_2.setText("RR数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });

    }

    private void mHRVRatioCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    hrv_2.setVisibility(View.VISIBLE);
                    hrv_2.setText("HRV数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mBreathingRateCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    breathing_rate_2.setVisibility(View.VISIBLE);
                    breathing_rate_2.setText("呼吸率数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)) + ", 呼吸率变化:" + (Integer) (dm.data[0] & 0xFF));
                }
            }
        });
    }

    //呼吸强度
    private void mBreatheStrengthCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    breathestreng_2.setVisibility(View.VISIBLE);
                    breathestreng_2.setText("呼吸强度数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mMuscleMonitorUpperCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    MUSCLE_M_UPPER++;
                    muscle_m_upper_2.setVisibility(View.VISIBLE);
                    muscle_m_upper_2.setText("上身肌肉监测数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mMuscleMonitorLowerCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    MUSCLE_M_LOSER++;
                    muscle_m_lower_2.setVisibility(View.VISIBLE);
                    muscle_m_lower_2.setText("下身肌肉监测数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mMuscleImbalanceUpperCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    muscle_i_upper_2.setVisibility(View.VISIBLE);
                    muscle_i_upper_2.setText("上身肌肉失调数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mMuscleImbalanceLowerCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    muscle_i_lower_2.setVisibility(View.VISIBLE);
                    muscle_i_lower_2.setText("下身肌肉失调数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mStepCounterCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    step_2.setVisibility(View.VISIBLE);
                    step_2.setText("计步数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mCaloriesCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    calories_2.setVisibility(View.VISIBLE);
                    calories_2.setText("热量数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mStateCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    state_2.setVisibility(View.VISIBLE);
                    state_2.setText("状态数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mGpsCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    gps_2.setVisibility(View.VISIBLE);
                    gps_2.setText("GPS数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mSpeedCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    speed_2.setVisibility(View.VISIBLE);
                    speed_2.setText("速度数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

    private void mAltitudeCallback(final int tag, final BodyDataParser.BodyDataMeta dm) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (tag == currentIndexDevice) {
                    altitude_2.setVisibility(View.VISIBLE);
                    altitude_2.setText("海拔数据字符串:" + Arrays.toString(dm.data) + ", 序号:" + dm.item + ", 时间:" + sdFormat.format(new Date(dm.time)));
                }
            }
        });
    }

}
