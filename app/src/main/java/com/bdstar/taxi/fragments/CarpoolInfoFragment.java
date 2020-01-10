package com.bdstar.taxi.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentCarpoolInfoBinding;
import com.bdstar.taxi.events.MeterHeartbeatEvent;
import com.bdstar.taxi.viewmodels.CarpoolInfoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 拼车添加乘客和下车
 * A simple {@link Fragment} subclass.
 */
public class CarpoolInfoFragment extends Fragment {

    private FragmentCarpoolInfoBinding binding;
    private CarpoolInfoViewModel viewModel;

    public CarpoolInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carpool_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(CarpoolInfoViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeterHeartbeat(MeterHeartbeatEvent event){
        viewModel.updateInfo(event.getValuationInfos());
    }

    public void reset(){
        viewModel.reset();
    }
}
