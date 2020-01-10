package com.bdstar.taxi.viewmodels;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.events.PassengerOffEvent;
import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.net.APIService;
import com.bdstar.taxi.net.apis.BaseApi;
import com.bdstar.taxi.net.bean.DriverFullInfo;
import com.bdstar.taxi.net.bean.DriverInfo;
import com.bdstar.taxi.net.bean.DriverInfoResponse;
import com.bdstar.taxi.net.bean.InsertRecordRequest;
import com.bdstar.taxi.net.bean.OrderData;
import com.bdstar.taxi.net.bean.OrderSaveRequest;
import com.bdstar.taxi.net.bean.OrderSaveResponse;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.net.bean.UpdateRecordRequest;
import com.bdstar.taxi.serial.LEDProtocol;
import com.bdstar.taxi.serial.SerialController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 监听按表抬表，设置机信息
 */
public class DriverInfoViewModel extends ViewModel {
    public static final int STATUS_EMPTY = 0;
    public static final int STATUS_NO_CARPOOL = 1;
    public static final int STATUS_CARPOOLING = 2;

    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> gender = new MutableLiveData<>();
    public final MutableLiveData<String> age = new MutableLiveData<>();
    public final MutableLiveData<String> company = new MutableLiveData<>();
    public final MutableLiveData<String> phone = new MutableLiveData<>();
    public final MutableLiveData<String> avatar = new MutableLiveData<>();
    public final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    public final MutableLiveData<String> ledState = new MutableLiveData<>("空车");
    public final MutableLiveData<Integer> status = new MutableLiveData<>(STATUS_EMPTY);

    //public final MutableLiveData<UpdateRecordRequest> finishOrder = new MutableLiveData<>();

    private CarpoolInfoViewModel carpoolInfoViewModel;
    private DriverFullInfo driverInfo;
    private Disposable disposable;
    private String[] orderIds = new String[2];
    private int passenger = 0;
    private OnPassengerChangeListener listener;
    private boolean isCarpool = false;
    private static final String[] LED_STATUS = new String[]{"空车", "电召", "网约", "充电", "维修", "交班"};

    private BaseApi api = APIService.INSTANCE.createAPI(BaseApi.class);
    private LEDProtocol ledProtocol;

    public DriverInfoViewModel(){
        ledProtocol = (LEDProtocol) SerialController.INSTANCE.getProtocol(LEDProtocol.class);
    }

    public void setCarpoolInfoViewModel(CarpoolInfoViewModel carpoolInfoViewModel) {
        this.carpoolInfoViewModel = carpoolInfoViewModel;
    }

    public void setOnPassengerChangeListner(OnPassengerChangeListener listner){
        this.listener = listner;
    }

    /*public void queryDriverInfo(String cardNo){
        disposable = Observable.just(cardNo)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<DriverInfoResponse>>() {
                    @Override
                    public ObservableSource<DriverInfoResponse> apply(String s) {
                        return api.getDriverInfo(s);
                    }
                })
                .doOnNext(new Consumer<DriverInfoResponse>() {
                    @Override
                    public void accept(DriverInfoResponse driverInfoResponse) {
                        if (driverInfoResponse.isSuccess()){
                            driverInfo = driverInfoResponse.getData().getObject();

                            name.postValue(driverInfo.getName());
                            gender.postValue(driverInfo.getSex());
                            company.postValue(driverInfo.getCompanyName());
                            phone.postValue(driverInfo.getPhone());
                            avatar.postValue(driverInfo.getPhoto());
                        }else {
                            errorMsg.postValue(driverInfoResponse.getError());
                        }
                    }
                })
                .subscribe(new Consumer<DriverInfoResponse>() {
                               @Override
                               public void accept(DriverInfoResponse driverInfoResponse) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                errorMsg.postValue(throwable.getMessage());
                            }
                        });
    }*/

    public void setDriverInfo(DriverFullInfo driverInfo){
        this.driverInfo = driverInfo;

        name.postValue(driverInfo.getJt808DriverInfoEntity().getName());
        gender.postValue(driverInfo.getJt808DriverInfoEntity().getSex());
        company.postValue(driverInfo.getJt808CompanyInfoEntity().getCompanyName());
        phone.postValue(driverInfo.getJt808DriverInfoEntity().getContact());
        avatar.postValue(driverInfo.getJt808DriverInfoEntity().getFacephotourl());
        age.postValue(driverInfo.getJt808DriverInfoEntity().getAge());
    }

    public String getOrderId(int index){
        return orderIds[index];
    }

    public int getPassenger() {
        return passenger;
    }

    public void clearOrder(int index){
        orderIds[index] = null;
        passenger --;
        /*int passenger = orderIds.length;

        for (String id: orderIds){
            if (id == null)
                passenger --;
        }*/
    }

    public void createOrder(final int index, final String addr){
        /*String orderId =null;

        if (index != 0)
            orderId = orderIds[0];*/
        final String orderId = orderIds[0];

        disposable = Observable.just(" ")
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<Response<OrderData>>>() {
                    @Override
                    public ObservableSource<Response<OrderData>> apply(String s) throws Exception {
                        InsertRecordRequest request = new InsertRecordRequest(orderId, driverInfo.getJt808DriverInfoEntity().getName(), driverInfo.getDriverid(), addr, MainActivity.deviceId);

                        request.setLicensePlateNumber(driverInfo.getVehiid());
                        return api.createOrder(request);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<OrderData>>() {
                    @Override
                    public void accept(Response<OrderData> orderDataResponse) throws Exception {
                       if (orderDataResponse.isSuccess()){
                           orderIds[index] = orderDataResponse.getData().getObject().getId();
                       }else {
                           errorMsg.postValue(orderDataResponse.getError());
                       }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorMsg.postValue(throwable.getMessage());
                    }
                });
        passenger ++;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (disposable != null)
            disposable.dispose();
    }

    public void onLedStateSelected(boolean isChecked, int state){
        if (isChecked) setLed(LED_STATUS[state]);
    }

    public void setLed(String text){
        ledState.setValue(text);

        ledProtocol.sendFrontText(text);
    }

    public void doCarpool(String destination){
        setLed("在拼车中 " + destination);
        status.postValue(STATUS_CARPOOLING);
    }

    public void onPassengerOn(PassengerOnEvent event){
        if (event.getIndex() == 0){
            if (passenger > 0)
                return;

            isCarpool = false;
            status.postValue(STATUS_NO_CARPOOL);
            carpoolInfoViewModel.reset();
        }else {
            isCarpool = true;
        }

        setLed("有客");
        createOrder(event.getIndex(), MainActivity.location.getLatitude() + "," + MainActivity.location.getLongitude());
    }

    public void onPassengerOff(PassengerOffEvent event){
        UpdateRecordRequest order = new UpdateRecordRequest();

        if (passenger == 1){
            /*for (String orderId : orderIds){
                if (!TextUtils.isEmpty(orderId)){
                    order.setId(orderId);
                    break;
                }
            }*/
            if (Constant.isPaying) return;

            if (orderIds[0] != null){
                order.setId(orderIds[0]);
                clearOrder(0);
            }else if (orderIds[1] != null){
                order.setId(orderIds[1]);
                clearOrder(1);
            }

            status.postValue(STATUS_EMPTY);
            //setLed("空车");
        }else if (passenger == 2){
            if (event.getType() == PassengerOffEvent.OFFTYPE_METER) return;

            order.setId(orderIds[event.getIndex()]);
            clearOrder(event.getIndex());
        }

        order.setPincheState(isCarpool ? UpdateRecordRequest.PINCHE_STATE_YES : UpdateRecordRequest.PINCHE_STATE_NOT);
        order.setPayManey(event.getPay());
        order.setOffAdress(MainActivity.location.toString());
        Constant.isPaying = true;
        //finishOrder.postValue(order);
        if (listener != null)
            listener.onPassengerOff(order);
    }

    public boolean isPassengerClear(){
        return orderIds[0] == null && orderIds[1] == null;
    }

    public interface OnPassengerChangeListener{
        void onPassengerOff(UpdateRecordRequest request);
    }
}
