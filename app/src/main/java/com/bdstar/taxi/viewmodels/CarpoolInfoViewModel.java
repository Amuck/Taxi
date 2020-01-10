package com.bdstar.taxi.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.serial.MeterProtocol;
import com.bdstar.taxi.serial.SerialController;
import com.bdstar.taxi.serial.bean.CarpoolPassengerOff;
import com.bdstar.taxi.serial.bean.CarpoolPassengerOn;
import com.bdstar.taxi.serial.bean.ValuationInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼车上车和下车
 */
public class CarpoolInfoViewModel extends ViewModel {
    //public final MutableLiveData<List<ValuationInfo>> infos = new MutableLiveData<>();
    public final MutableLiveData<ValuationInfo> p1 = new MutableLiveData<>();
    public final MutableLiveData<ValuationInfo> p2 = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isAddable = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isOffAble = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isP1On = new MutableLiveData<>(true);
    public final MutableLiveData<Boolean> isP2On = new MutableLiveData<>(false);

    private MeterProtocol protocol;

    public CarpoolInfoViewModel(){
        protocol = (MeterProtocol) SerialController.INSTANCE.getProtocol(MeterProtocol.class);
    }

    public void reset(){
        //infos.postValue(new ArrayList<ValuationInfo>());
        isAddable.postValue(true);
        isOffAble.postValue(false);
        isP1On.postValue(true);
        isP2On.postValue(false);
    }

    public void updateInfo(List<ValuationInfo> newInfos){
        //List<ValuationInfo> oldInfos = infos.getValue();
        if (newInfos.size() == 2){
            //infos.postValue(newInfos);
            p1.postValue(newInfos.get(0));
            p2.postValue(newInfos.get(1));
        }else if (newInfos.size() == 1){
            if (isP1On.getValue()){
                //infos.postValue(newInfos);
                p1.postValue(newInfos.get(0));
            }else {
                //newInfos.add(0, null);
                //infos.postValue(newInfos);
                p2.postValue(newInfos.get(0));
            }
            /*if (oldInfos != null && !oldInfos.isEmpty()){
                if (isP1On.getValue()){
                    this.infos.set(0, infos.get(0));
                }else {
                    this.infos.set(1, infos.get(0));
                }
            }else {
                infos.addAll(newInfos);
            }*/
        }
    }

    public void onP2On(){
        isP2On.postValue(true);
        isAddable.postValue(false);
        isOffAble.postValue(true);

        protocol.carpoolPassengerOn(new CarpoolPassengerOn(1, 1));
    }

    public void onP1Off(){
        isOffAble.postValue(false);
        isP1On.postValue(false);

        protocol.carpoolPassengerOff(new CarpoolPassengerOff(0));
    }

    public void onP2Off(){
        isOffAble.postValue(false);
        isP2On.postValue(false);

        protocol.carpoolPassengerOff(new CarpoolPassengerOff(1));
    }
}
