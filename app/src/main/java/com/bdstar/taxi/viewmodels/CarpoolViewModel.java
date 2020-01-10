package com.bdstar.taxi.viewmodels;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.events.ErrorEvent;
import com.bdstar.taxi.events.LedEvent;
import com.bdstar.taxi.net.APIService;
import com.bdstar.taxi.net.apis.BaseApi;
import com.bdstar.taxi.net.bean.Destinations;
import com.bdstar.taxi.net.bean.InsertRecordRequest;
import com.bdstar.taxi.net.bean.Response;
import com.bdstar.taxi.serial.LEDProtocol;
import com.bdstar.taxi.serial.SerialController;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CarpoolViewModel extends ViewModel {
    public static final int STATE_NOT_CARPOOL = 0;
    public static final int STATE_SELECT_DIRECTION = 1;
    public static final int STATE_ON_CARPOOL = 2;

    public final MutableLiveData<Integer> state = new MutableLiveData<>();
    public final MutableLiveData<List<Destinations.Destination>> dirs = new MutableLiveData<>();
    public final MutableLiveData<String> desName = new MutableLiveData<>();

    private BaseApi api = APIService.INSTANCE.createAPI(BaseApi.class);
    private Disposable disposable;
    private Destinations.Destination destination;
    private OnDestinationChangeListener listener;
    //private LEDProtocol ledProtocol;

    public CarpoolViewModel(){
        //ledProtocol = (LEDProtocol) SerialController.INSTANCE.getProtocol(LEDProtocol.getTag());
        List<Destinations.Destination> destinations = new ArrayList<>();

        destinations.add(new Destinations.Destination("方向一"));
        destinations.add(new Destinations.Destination("方向二"));
        destinations.add(new Destinations.Destination("方向三"));
        destinations.add(new Destinations.Destination("方向四"));
        destinations.add(new Destinations.Destination("方向五"));
        destinations.add(new Destinations.Destination("方向六"));

        dirs.postValue(destinations);
    }

    public void setListener(OnDestinationChangeListener listener) {
        this.listener = listener;
    }

    public void onDirSelected(View view){
        String dName = ((TextView)view).getText().toString();

        for (Destinations.Destination des : dirs.getValue()){
            if (dName.equals(des.getDestination())){
                destination = des;

                if (listener != null)
                    listener.onDestinationSet(dName);
                //state.setValue(STATE_ON_CARPOOL);
                //desName.setValue(dName);
                //ledProtocol.sendFrontText("拼车 " + des.getDestination());
                //EventBus.getDefault().post(new LedEvent(des.getDestination()));
                break;
            }
        }
    }

    public void onCarpoolClicked(){

    }

    public void showDes(){
        disposable = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<Response<Destinations>>>() {
                    @Override
                    public ObservableSource<Response<Destinations>> apply(String s) throws Exception {
                        return api.getDestinations();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Response<Destinations>>() {
                    @Override
                    public void accept(Response<Destinations> destinationsResponse) throws Exception {
                        if (destinationsResponse.isSuccess()){
                            state.setValue(STATE_SELECT_DIRECTION);
                            //dirs.setValue((Destinations.Destination[]) destinationsResponse.getData().getObject().toArray());
                            dirs.setValue(destinationsResponse.getData().getObject());
                        }else {
                            EventBus.getDefault().post(new ErrorEvent(destinationsResponse.getError()));
                        }
                    }
                })
                .subscribe(new Consumer<Response<Destinations>>() {
                               @Override
                               public void accept(Response<Destinations> driverInfoResponse) throws Exception {

                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                EventBus.getDefault().post(new ErrorEvent(throwable));
                                state.postValue(STATE_SELECT_DIRECTION);
                            }
                        });
    }

    public void resetCarpool(){
        state.setValue(STATE_NOT_CARPOOL);
    }

    public Destinations.Destination getDestination() {
        return destination;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (disposable != null)
            disposable.dispose();
    }

    public interface OnDestinationChangeListener{
        void onDestinationSet(String desName);
    }
}
