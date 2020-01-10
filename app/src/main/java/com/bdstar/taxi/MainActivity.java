package com.bdstar.taxi;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.databinding.ActivityMainBinding;
import com.bdstar.taxi.dialog.AlertDialogFragment;
import com.bdstar.taxi.dialog.NetWrongDialogFragment;
import com.bdstar.taxi.events.DriverLoginEvent;
import com.bdstar.taxi.events.ErrorEvent;
import com.bdstar.taxi.events.MeterStatuEvent;
import com.bdstar.taxi.events.OrderCreateEvent;
import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.events.UploadRecordEvent;
import com.bdstar.taxi.net.bean.UpdateInfo;
import com.bdstar.taxi.serial.ByteUtil;
import com.bdstar.taxi.speech.SpeechTTS;
import com.bdstar.taxi.utils.QrcodeUtil;
import com.bdstar.taxi.utils.UUIDUtil;
import com.bdstar.taxi.viewmodels.DriverInfoViewModel;
import com.bdstar.taxi.viewmodels.MainViewModel;
import com.bdstar.updatelib.UpdateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取设备号，检查更新，初始化语音、端口协议、定位、连接端口
 */
public class MainActivity extends AppCompatActivity implements LocationListener {

    public static com.bdstar.taxi.net.bean.Location location = new com.bdstar.taxi.net.bean.Location();
    public static String deviceId = "";

    private String driverId;
    private String driverLoginId;
    //private String[] orderId = new String[2];
    private AlertDialogFragment alertDialogFragment;
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private LocationManager locationManager;
    private NavController navController;
    private DriverInfoViewModel driverInfoViewModel;
    private boolean isNetWrong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        alertDialogFragment = new AlertDialogFragment();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        navController = Navigation.findNavController(this, R.id.fragment);
        deviceId = UUIDUtil.getDeviceId(this);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //viewModel.setUUID("123456");
        SpeechTTS.INSTANCE.init(this);
        alertDialogFragment.setListener(new AlertDialogFragment.AlertListener() {
            @Override
            public void onClicked() {
                navController.popBackStack(R.id.driverInfoFragment, false);

                Constant.isPaying = false;
            }
        });

        viewModel.updateDate.observe(this, new Observer<UpdateInfo.UpdateData>() {
            @Override
            public void onChanged(UpdateInfo.UpdateData updateData) {
                UpdateFragment.showFragment(
                        MainActivity.this,
                        false,
                        updateData.getUrl(),
                        "taxi",
                        "有新的版本",
                        BuildConfig.APPLICATION_ID,
                        new UpdateFragment.CancelListener() {
                            @Override
                            public void onCancel() {
                                finish();
                            }
                        });
            }
        });


        /*String qrcode = ByteUtil.bytesToHexString(QrcodeUtil.createQRCodeBits("测试", 128, 128));
        Log.d("QRCode", qrcode);
        Log.d("QRCode", qrcode.length() + "");*/

        initLocationService();
        viewModel.setUUID(deviceId);
        viewModel.connectSerial();
        viewModel.getAds();
        viewModel.getUpdateInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        locationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event){
        if (event.getThrowable() != null){

            //Toast.makeText(this, event.getThrowable().getMessage(), Toast.LENGTH_LONG).show();
            if (!isNetWrong){
                isNetWrong = true;
                NetWrongDialogFragment.newInstance().show(getSupportFragmentManager(), "netwrong");
            }
            event.getThrowable().printStackTrace();
        } else
            Toast.makeText(this, event.getMsg(), Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordUpload(UploadRecordEvent event){
        alertDialogFragment.show(getSupportFragmentManager(), "alertDialog");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDriverLogin(DriverLoginEvent event){

        /*Observable.just("通知 " + binding.tvMessage.getText().toString())
                .observeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {

                        SpeechTTS.INSTANCE.speech(s);
                    }
                })
                .subscribe();*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPassengerOn(PassengerOnEvent event){
        if (event.getIndex() == 0){
            viewModel.showWelcome();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeterStatus(MeterStatuEvent event){
        viewModel.setDeviceInfo(event.getCarNo());
    }
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderCreated(OrderCreateEvent event){
        if (orderId.length > event.getIndex()){
            orderId[event.getIndex()] = event.getOrderId();
        }
    }*/

   private void initLocationService(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, false);

        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider, 5, 10, this);
        }
    }

    public com.bdstar.taxi.net.bean.Location getLocation(){
        return location;
    }

    public String getDriverId(){
        return driverId;
    }

    /*public String getOrderId(int index){
        String id = null;

        if (index <= orderId.length + 1){
            id = orderId[index];
        }

        return id;
    }*/

    @Override
    public void onLocationChanged(Location location) {
        MainActivity.location.setLatitude(location.getLatitude());
        MainActivity.location.setLongitude(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
