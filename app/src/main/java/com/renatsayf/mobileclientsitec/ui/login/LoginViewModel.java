package com.renatsayf.mobileclientsitec.ui.login;

import com.renatsayf.mobileclientsitec.repository.net.NetRepository;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel
{
    private final NetRepository net;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LoginViewModel(NetRepository net) {
        this.net = net;
    }

    public void auth(String imei, String uid, String pass) {
        compositeDisposable.add(
                net.auth(imei, uid, pass, false, "")
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe( authCode -> {

                        }, throwable -> {

                        })
        );
    }

    @Override
    protected void onCleared()
    {
        compositeDisposable.dispose();

        super.onCleared();
    }
}