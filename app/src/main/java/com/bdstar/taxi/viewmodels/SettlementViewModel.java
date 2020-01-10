package com.bdstar.taxi.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.events.ErrorEvent;
import com.bdstar.taxi.net.APIService;
import com.bdstar.taxi.net.apis.BaseApi;
import com.bdstar.taxi.net.bean.FPUrl;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.serial.MeterProtocol;
import com.bdstar.taxi.serial.SerialController;
import com.bdstar.taxi.utils.QrcodeUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 打印发票二维码
 */
public class SettlementViewModel extends ViewModel {
    public final MutableLiveData<String> price = new MutableLiveData<>();
    public final MutableLiveData<String> aliPay = new MutableLiveData<>(Constant.aliPay);
    public final MutableLiveData<String> wechatPay = new MutableLiveData<>(Constant.wechatPay);
    public final MutableLiveData<Boolean> isProgressShow = new MutableLiveData<>(false);
    
    private Disposable disposable;
    private String orderId;
    private String url;
    private MeterProtocol meterProtocol;
    private BaseApi api = APIService.INSTANCE.createAPI(BaseApi.class);
    
    public SettlementViewModel(){
        meterProtocol = (MeterProtocol) SerialController.INSTANCE.getProtocol(MeterProtocol.class);
    }
    
    public void print(){
        /*disposable = api.getFPUrl(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Response<FPUrl>>() {
                               @Override
                               public void accept(Response<FPUrl> fpUrlResponse) {
                                   // TODO: 2020/1/9 开始打印 
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                EventBus.getDefault().post(new ErrorEvent(throwable));
                            }
                        });*/
        String fullUrl = url + "/pjfw/index.html?id=" + orderId;

        isProgressShow.postValue(true);
        meterProtocol.print(QrcodeUtil.createQRCodeBits(fullUrl, 128, 128), "月城出租", new MeterProtocol.Callback() {
            @Override
            public void onCompleted(boolean isSuccessful) {
                isProgressShow.postValue(false);
            }
        });
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        
        if (disposable != null)
            disposable.dispose();
    }
}
