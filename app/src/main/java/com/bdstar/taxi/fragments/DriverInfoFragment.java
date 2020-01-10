package com.bdstar.taxi.fragments;


import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentDriverInfoBinding;
import com.bdstar.taxi.events.DriverLoginEvent;
import com.bdstar.taxi.events.PassengerOffEvent;
import com.bdstar.taxi.events.PassengerOnEvent;
import com.bdstar.taxi.net.bean.UpdateRecordRequest;
import com.bdstar.taxi.viewmodels.CarpoolInfoViewModel;
import com.bdstar.taxi.viewmodels.DriverInfoViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 司机信息，监听按表抬表，设置机信息
 * A simple {@link Fragment} subclass.
 */
public class DriverInfoFragment extends BaseFragment {

    private DriverInfoViewModel viewModel;
    //private CarpoolInfoViewModel carpoolInfoViewModel;
    private FragmentDriverInfoBinding binding;
    private CarpoolFragment carpoolFragment;
    private CarpoolInfoFragment carpoolInfoFragment;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_driver_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        binding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(DriverInfoViewModel.class);
        carpoolFragment = (CarpoolFragment) getChildFragmentManager().findFragmentById(R.id.fragmentCarpool);
        carpoolInfoFragment = (CarpoolInfoFragment) getChildFragmentManager().findFragmentById(R.id.fragmentCarpoolInfo);
        //carpoolInfoViewModel = ViewModelProviders.of(carpoolInfoFragment).get(CarpoolInfoViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setCarpoolInfoViewModel(ViewModelProviders.of(carpoolInfoFragment).get(CarpoolInfoViewModel.class));
        viewModel.setOnPassengerChangeListner(new DriverInfoViewModel.OnPassengerChangeListener() {
            @Override
            public void onPassengerOff(UpdateRecordRequest request) {
                NavDirections action = DriverInfoFragmentDirections.actionDriverInfoFragmentToSettlementFragment(request);

                navController.navigate(action);
            }
        });

        radioFix();


    }

    private void radioFix(){
        binding.rbCharge.setButtonDrawable(new StateListDrawable());
        binding.rbEmpty.setButtonDrawable(new StateListDrawable());
        binding.rbExchange.setButtonDrawable(new StateListDrawable());
        binding.rbFix.setButtonDrawable(new StateListDrawable());
        binding.rbNet.setButtonDrawable(new StateListDrawable());
        binding.rbPhone.setButtonDrawable(new StateListDrawable());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        if (viewModel.isPassengerClear()){
            binding.rbEmpty.setChecked(true);
            viewModel.setLed("空车");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDriverLogin(DriverLoginEvent event){
        //viewModel.queryDriverInfo(event.getDriverId());
        viewModel.setDriverInfo(event.getDriverInfo());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPassengerOn(PassengerOnEvent event){
        viewModel.onPassengerOn(event);

        /*if (event.getIndex() == 0)
            carpoolInfoFragment.reset();*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPassengerOff(PassengerOffEvent event){
        viewModel.onPassengerOff(event);
    }
}
