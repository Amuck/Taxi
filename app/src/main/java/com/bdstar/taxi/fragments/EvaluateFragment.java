package com.bdstar.taxi.fragments;


import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdstar.taxi.MainActivity;
import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentEvaluateBinding;
import com.bdstar.taxi.viewmodels.EvaluateViewModel;

/**
 * 评价
 * A simple {@link Fragment} subclass.
 */
public class EvaluateFragment extends Fragment {

    private FragmentEvaluateBinding binding;
    private EvaluateViewModel viewModel;

    public EvaluateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*viewModel.isRecordUpload.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                }
            }
        });*/

        return inflater.inflate(R.layout.fragment_evaluate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(EvaluateViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setRecordRequest(EvaluateFragmentArgs.fromBundle(getArguments()).getOrder());

        radioFix();
        //viewModel.setLocation(MainActivity.location);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.delayFinish();
    }

    private void radioFix(){
        binding.rbWSGood.setButtonDrawable(new StateListDrawable());
        binding.rbWSLow.setButtonDrawable(new StateListDrawable());
        binding.rbWSNormal.setButtonDrawable(new StateListDrawable());
        binding.rbZLGood.setButtonDrawable(new StateListDrawable());
        binding.rbZLLow.setButtonDrawable(new StateListDrawable());
        binding.rbZLNormal.setButtonDrawable(new StateListDrawable());
    }
}
