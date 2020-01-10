package com.bdstar.taxi.dialog;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bdstar.taxi.R;
import com.bdstar.taxi.databinding.FragmentSelectDialogBinding;

/**
 * 是否同意拼车
 * A simple {@link Fragment} subclass.
 */
public class SelectDialogFragment extends DialogFragment implements View.OnClickListener {

    private FragmentSelectDialogBinding binding;
    private DialogListener listener;
    private String msg;

    public SelectDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_select_dialog, container, false);
        binding = DataBindingUtil.bind(view);

        binding.tvMsg.setText(msg);
        binding.btnOk.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(660, 365);
    }

    public void setMessage(String msg){
        this.msg = msg;

        if (binding != null)
            binding.tvMsg.setText(msg);
    }

    public void setListener(DialogListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        dismiss();
        if (listener != null){
            if (id == R.id.btnOk){
                listener.onOk();
            }else {
                listener.onCancel();
            }
        }
    }

    public interface DialogListener{
        void onOk();
        void onCancel();
    }
}
