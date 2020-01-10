package com.bdstar.taxi.viewmodels;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.BuildConfig;
import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.events.DriverLoginEvent;
import com.bdstar.taxi.events.ErrorEvent;
import com.bdstar.taxi.net.APIService;
import com.bdstar.taxi.net.apis.BaseApi;
import com.bdstar.taxi.net.bean.Ads;
import com.bdstar.taxi.net.bean.DefaultData;
import com.bdstar.taxi.net.bean.DriverFullInfo;
import com.bdstar.taxi.net.bean.DriverInfo;
import com.bdstar.taxi.net.bean.DriverInfoResponse;
import com.bdstar.taxi.net.bean.Notification;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.net.bean.UpdateInfo;
import com.bdstar.taxi.serial.LEDProtocol;
import com.bdstar.taxi.serial.MeterProtocol;
import com.bdstar.taxi.serial.SerialController;
import com.bdstar.taxi.speech.SpeechTTS;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    public final MutableLiveData<Boolean> isDriverLoginning = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isWelcomeShow = new MutableLiveData<>(false);
    public final MutableLiveData<String> notiStr = new MutableLiveData<>("");
    public final MutableLiveData<String> uuid = new MutableLiveData<>();
    public final MutableLiveData<UpdateInfo.UpdateData> updateDate = new MutableLiveData<>();
    public final MutableLiveData<String> version = new MutableLiveData<>(BuildConfig.VERSION_CODE + "");

    private Disposable disposable;
    private Disposable adsDisposable;
    private Disposable noticeDisposable;
    private Disposable updateDisposable;
    private Disposable uploadUUIDDisp;
    private BaseApi api = APIService.INSTANCE.createAPI(BaseApi.class);
    private Disposable loginDisposable;
    private LEDProtocol ledProtocol;
    private MeterProtocol meterProtocol;
    private DriverFullInfo driverFullInfo;
    private Ads.AdsData ads;
    private Notification.NotificationData notificationData;
    private boolean isNotificationEabled = false;

    public DriverFullInfo getDriverFullInfo() {
        return driverFullInfo;
    }

    /**
     * 连接端口
     */
    public void connectSerial(){
        SerialController.INSTANCE.connect(MeterProtocol.class);
        SerialController.INSTANCE.connect(LEDProtocol.class);
        SerialController.INSTANCE.start();

        ledProtocol = (LEDProtocol) SerialController.INSTANCE.getProtocol(LEDProtocol.class);
        meterProtocol = (MeterProtocol) SerialController.INSTANCE.getProtocol(MeterProtocol.class);

        if (ledProtocol != null)
            ledProtocol.reset();

        if (meterProtocol != null)
            meterProtocol.queryMeterStatus();
    }

    /*public void setVersion(){
        version
    }*/

    public void setUUID(String uuid){
        this.uuid.postValue(uuid);
    }

    /**
     * @param carNo 上传设备编号
     */
    public void setDeviceInfo(String carNo){

        uploadUUIDDisp = api.uploadDeviceUUID(uuid.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Response<DefaultData>>() {
                    @Override
                    public void accept(Response<DefaultData> defaultDataResponse) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 每五秒获取司机信息
     * 司机打卡uuid
     */
    public void driverLogin(){

        loginDisposable = Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                .flatMap(new Function<Long, ObservableSource<DriverInfoResponse>>() {
                    @Override
                    public ObservableSource<DriverInfoResponse> apply(Long aLong) {
                        return api.getDriverInfo(uuid.getValue());
                    }
                })
                .takeWhile(new Predicate<DriverInfoResponse>() {
                    @Override
                    public boolean test(DriverInfoResponse driverInfoResponse) {
                        return driverInfoResponse.isSuccess();
                    }
                })
                .subscribe(new Consumer<DriverInfoResponse>() {
                               @Override
                               public void accept(DriverInfoResponse driverInfoResponse) {
                                   if (driverInfoResponse.getData().getObject() == null) return;

                                   if (driverFullInfo == null || !driverFullInfo.getDriverid().equals(driverInfoResponse.getData().getObject().getDriverid())){
                                       driverFullInfo = driverInfoResponse.getData().getObject();
                                       // 支付二维码
                                       Constant.aliPay = driverInfoResponse.getData().getObject().getScDriverInfoReplenishEntity().getAlipayPay();
                                       Constant.wechatPay = driverInfoResponse.getData().getObject().getScDriverInfoReplenishEntity().getWechatPay();

                                        EventBus.getDefault().post(new DriverLoginEvent(driverInfoResponse.getData().getObject()));
                                       isDriverLoginning.postValue(true);

                                       // 获取通知
                                       getNotification();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                EventBus.getDefault().post(new ErrorEvent(throwable));

                                //EventBus.getDefault().post(new DriverLoginEvent(new DriverFullInfo()));
                                //isDriverLoginning.postValue(true);
                            }
                        });

    }

    /**
     * 获取广告
     */
    public void getAds(){
        adsDisposable = Observable.interval(10, TimeUnit.SECONDS, Schedulers.io())
                .flatMap(new Function<Long, ObservableSource<Response<Ads>>>() {
                    @Override
                    public ObservableSource<Response<Ads>> apply(Long aLong) throws Exception {
                        return api.getAds();
                    }
                })
                .takeWhile(new Predicate<Response<Ads>>() {
                    @Override
                    public boolean test(Response<Ads> adsResponse) throws Exception {
                        return adsResponse.isSuccess();
                    }
                })
                .doOnNext(new Consumer<Response<Ads>>() {
                    @Override
                    public void accept(Response<Ads> adsResponse) throws Exception {
                        if (ads == null || !ads.getId().equals(adsResponse.getData().getObjcet().getId())){
                            ads = adsResponse.getData().getObjcet();
                            ledProtocol.sendBackText(ads.getAds());
                        }
                    }
                })
                .subscribe(new Consumer<Response<Ads>>() {
                    @Override
                    public void accept(Response<Ads> adsResponse) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 每15秒获取通知,语音播报
     */
    public void getNotification(){
        if (isNotificationEabled) return;

        isNotificationEabled = true;
        noticeDisposable = Observable.interval(15, TimeUnit.SECONDS, Schedulers.io())
                .flatMap(new Function<Long, ObservableSource<Response<Notification>>>() {
                    @Override
                    public ObservableSource<Response<Notification>> apply(Long aLong) throws Exception {
                        return api.getNotice();
                    }
                })
                .takeWhile(new Predicate<Response<Notification>>() {
                    @Override
                    public boolean test(Response<Notification> notificationResponse) throws Exception {
                        return notificationResponse.isSuccess();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Notification>>() {
                               @Override
                               public void accept(Response<Notification> notificationResponse) throws Exception {
                                   if (notificationData == null || !notificationData.getId().equals(notificationResponse.getData().getObject().getId())){
                                       notificationData = notificationResponse.getData().getObject();

                                       notiStr.postValue(notificationData.getContent());
                                       SpeechTTS.INSTANCE.speech(notificationData.getContent());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
    }

    public void showWelcome(){
        disposable = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        isWelcomeShow.postValue(true);
                    }
                })
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   isWelcomeShow.postValue(false);
                               }
                           });
    }

    /**
     * 获取更新信息
     */
    public void getUpdateInfo(){
        updateDisposable = api.getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Response<UpdateInfo>>() {
                    @Override
                    public void accept(Response<UpdateInfo> updateInfoResponse) throws Exception {
                        if (updateInfoResponse.isSuccess()){
                            if (updateInfoResponse.getData().getObject().getVersion() > BuildConfig.VERSION_CODE){
                                Log.d("版本检测", "当前版本: " + BuildConfig.VERSION_CODE + " 更新版本: " + updateInfoResponse.getData().getObject().getVersion());
                                updateDate.postValue(updateInfoResponse.getData().getObject());
                            }else {
                                driverLogin();
                            }
                        }else {
                            driverLogin();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        driverLogin();
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (disposable != null)
            disposable.dispose();

        if (loginDisposable != null)
            loginDisposable.dispose();

        if (adsDisposable != null)
            adsDisposable.dispose();

        if (noticeDisposable != null)
            noticeDisposable.dispose();

        if (updateDisposable != null)
            updateDisposable.dispose();

        if (uploadUUIDDisp != null)
            uploadUUIDDisp.dispose();

        SerialController.INSTANCE.stop();
    }
}
