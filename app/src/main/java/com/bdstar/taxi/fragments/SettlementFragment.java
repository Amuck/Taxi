package com.bdstar.taxi.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdstar.taxi.R;
import com.bdstar.taxi.constants.Constant;
import com.bdstar.taxi.databinding.FragmentSettlementBinding;
import com.bdstar.taxi.net.bean.UpdateRecordRequest;
import com.bdstar.taxi.viewmodels.MainViewModel;
import com.bdstar.taxi.viewmodels.SettlementViewModel;

/**
 * 支付界面
 * A simple {@link Fragment} subclass.
 */
public class SettlementFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentSettlementBinding binding;
    private SettlementViewModel viewModel;
    private UpdateRecordRequest updateRecordRequest;

    public SettlementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_settlement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(SettlementViewModel.class);
        navController = Navigation.findNavController(view);
        updateRecordRequest = SettlementFragmentArgs.fromBundle(getArguments()).getOrder();
        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.btnCash.setOnClickListener(this);
        binding.btnAliPay.setOnClickListener(this);
        binding.btnWechatPay.setOnClickListener(this);
        viewModel.price.postValue(updateRecordRequest.getPayManey());
        viewModel.aliPay.postValue(Constant.aliPay);
        viewModel.wechatPay.postValue(Constant.wechatPay);
        viewModel.setOrderId(updateRecordRequest.getId());
        viewModel.setUrl(mainViewModel.getDriverFullInfo().getLocalhostIp());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCash){
            updateRecordRequest.setPayType(UpdateRecordRequest.PAY_TYPE_CASH);
        }else if (id == R.id.btnAliPay){
            updateRecordRequest.setPayType(UpdateRecordRequest.PAY_TYPE_ALIPAY);
        }else if (id == R.id.btnPrint){

        } else{
            updateRecordRequest.setPayType(UpdateRecordRequest.PAY_TYPE_WECHAT);
        }

        navController.navigate(SettlementFragmentDirections.actionSettlementFragmentToEvaluateFragment(updateRecordRequest));
    }
}
