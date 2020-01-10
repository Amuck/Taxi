package com.bdstar.taxi.fragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentCarpoolBinding;
import com.bdstar.taxi.dialog.SelectDialogFragment;
import com.bdstar.taxi.viewmodels.CarpoolViewModel;
import com.bdstar.taxi.viewmodels.DriverInfoViewModel;

/**
 * 是否拼车，选择方向
 * A simple {@link Fragment} subclass.
 */
public class CarpoolFragment extends Fragment {

    private FragmentCarpoolBinding binding;
    private CarpoolViewModel viewModel;
    private DriverInfoViewModel driverInfoViewModel;
    private SelectDialogFragment dialogFragment;

    public CarpoolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_carpool, container, false);
        binding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(CarpoolViewModel.class);
        driverInfoViewModel = ViewModelProviders.of(getParentFragment()).get(DriverInfoViewModel.class);
        dialogFragment = new SelectDialogFragment();

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.btnCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });
        dialogFragment.setMessage("乘客是否同意拼车？");
        dialogFragment.setListener(new SelectDialogFragment.DialogListener() {
            @Override
            public void onOk() {
                viewModel.showDes();
            }

            @Override
            public void onCancel() {

            }
        });
        viewModel.setListener(new CarpoolViewModel.OnDestinationChangeListener() {
            @Override
            public void onDestinationSet(String desName) {
                viewModel.resetCarpool();
                driverInfoViewModel.doCarpool(desName);
            }
        });
        /*viewModel.desName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.resetCarpool();
                driverInfoViewModel.doCarpool(s);
            }
        });*/

        return view;
    }

    public void resetCarpool(){
        viewModel.resetCarpool();
    }
}
