package com.pintu.neostore.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.pintu.neostore.view.forgot.Forgot;

public class ForgotVMFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;

    public ForgotVMFactory(Forgot forgot) {
        this.context = forgot;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForgotVM(context);
    }


}
