package com.bdstar.taxi.viewmodels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.events.ErrorEvent;
import com.bdstar.taxi.events.UploadRecordEvent;
import com.bdstar.taxi.net.APIService;
import com.bdstar.taxi.net.apis.BaseApi;
import com.bdstar.taxi.net.bean.DefaultData;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.net.bean.UpdateRecordRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 上传评价
 */
public class EvaluateViewModel extends ViewModel {

    //public final MutableLiveData<Boolean> isRecordUpload = new MutableLiveData<>(false);

    private BaseApi api = APIService.INSTANCE.createAPI(BaseApi.class);
    private Disposable disposable;
    private Disposable delayDisposabel;
    private String sScore = "3";
    private String cScore = "3";
    private UpdateRecordRequest recordRequest;
    private Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRecordRequest(UpdateRecordRequest recordRequest) {
        this.recordRequest = recordRequest;
    }

    public void serviceScore(boolean isChecked, String score){
        if (delayDisposabel != null)
            delayDisposabel.dispose();
        if (isChecked) sScore = score;
    }

    public void carScore(boolean isChecked, String score){
        if (delayDisposabel != null)
            delayDisposabel.dispose();
        if (isChecked) cScore = score;
    }

    public void onSubmit(){
        if (delayDisposabel != null)
            delayDisposabel.dispose();

        disposable = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<Response<DefaultData>>>() {
                    @Override
                    public ObservableSource<Response<DefaultData>> apply(String s) {
                        recordRequest.setCarHealth(cScore);
                        recordRequest.setServiceEvaluation(sScore);
                        recordRequest.setOffAdress(MainActivity.location.getLatitude() + "," + MainActivity.location.getLongitude());
                        return api.updateRecord(recordRequest);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<DefaultData>>() {
                               @Override
                               public void accept(Response<DefaultData> defaultDataResponse) {
                                    if (defaultDataResponse.isSuccess()){
                                        EventBus.getDefault().post(new UploadRecordEvent());
                                    }else {
                                        EventBus.getDefault().post(new ErrorEvent(defaultDataResponse.getError()));
                                    }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                EventBus.getDefault().post(new ErrorEvent(throwable));
                            }
                        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (disposable != null)
            disposable.dispose();
    }

    public void delayFinish(){
        delayDisposabel = Observable.timer(10, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        onSubmit();
                    }
                });
    }
}
