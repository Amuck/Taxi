package com.bdstar.taxi.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentTestBinding;
import com.bdstar.taxi.events.DriverLoginEvent;
import com.bdstar.taxi.events.PassengerOffEvent;
import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.serial.MeterProtocol;
import com.bdstar.taxi.serial.SerialController;
import com.bdstar.taxi.serial.bean.CarpoolPassengerOn;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {


    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTestBinding binding = DataBindingUtil.bind(view);

        /*binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DriverLoginEvent("2"));
            }
        });*/

        binding.cbPass1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    EventBus.getDefault().post(new PassengerOnEvent(0));
                }else {
                    EventBus.getDefault().post(new PassengerOffEvent(0, "18"));
                }
            }
        });

        binding.cbPass2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //EventBus.getDefault().post(new PassengerOnEvent(1));
                    ((MeterProtocol)SerialController.INSTANCE
                            .getProtocol(MeterProtocol.class))
                            .carpoolPassengerOn(new CarpoolPassengerOn(1, 0));
                }else {
                    //EventBus.getDefault().post(new PassengerOffEvent(1, "18"));
                }
            }
        });
    }
}
