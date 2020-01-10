package com.bdstar.taxi.dialog;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.NetWrongDialogFragmentBinding;
import com.bdstar.taxi.viewmodels.NetWrongDialogViewModel;

/**
 * 网络错误，退出软件
 */
public class NetWrongDialogFragment extends DialogFragment {

    private NetWrongDialogViewModel mViewModel;
    private NetWrongDialogFragmentBinding binding;

    public static NetWrongDialogFragment newInstance() {
        return new NetWrongDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.net_wrong_dialog_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding = DataBindingUtil.bind(view);
        mViewModel = ViewModelProviders.of(this).get(NetWrongDialogViewModel.class);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        /*Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );

        WindowManager.LayoutParams params = win.getAttributes();
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);*/
        setCancelable(false);
        getDialog().getWindow().setLayout(660, 365);
    }
}
