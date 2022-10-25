package com.renatsayf.mobileclientsitec.ui.login;

import com.renatsayf.mobileclientsitec.model.auth.AuthCode;
import com.renatsayf.mobileclientsitec.repository.net.NetRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
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
                        .subscribe(response -> {
                            int httpCode = response.code();
                            AuthCode authCode = response.body();
                            State state = new State();
                            if (httpCode >= 200 && httpCode <= 202 && authCode != null) {
                                state.code = authCode.code;
                                _state.setValue(state);
                            }
                            else {
                                state.error = "Failed authorization";
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            State state = new State();
                            state.error = throwable.getMessage();
                            _state.setValue(state);
                        })
        );
    }

    static class State {
        int code = -1;
        String error = "";
    }

    private final MutableLiveData<State> _state = new MutableLiveData<>(new State());
    public LiveData<State> getState()
    {
        return _state;
    }

    @Override
    protected void onCleared()
    {
        compositeDisposable.dispose();

        super.onCleared();
    }
}