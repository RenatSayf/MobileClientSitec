package com.renatsayf.mobileclientsitec.ui.users;

import com.renatsayf.mobileclientsitec.model.users.Users;
import com.renatsayf.mobileclientsitec.repository.net.NetRepository;

import javax.annotation.Nullable;
import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings("Convert2MethodRef")
@HiltViewModel
public class UsersViewModel extends ViewModel
{
    private final NetRepository net;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public UsersViewModel(NetRepository net) {
        this.net = net;
    }

    private final MutableLiveData<Users> _users = new MutableLiveData<>(null);
    public LiveData<Users> getUsers()
    {
        return _users;
    }

    public void getUsers(String imei) {
        compositeDisposable.add(
                net.getUsers(imei)
                        .subscribe(response -> {
                            if (response.code() == 200)
                            {
                                Users users = response.body();
                                State state = new State();
                                state.isAuth = true;
                                state.users = users;
                                _state.setValue(state);
                            }
                            else if (response.code() == 401) {
                                State state = new State();
                                state.isAuth = false;
                                state.users = null;
                                _state.setValue(state);
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            State state = new State();
                            state.isAuth = false;
                            state.users = null;
                            state.error = throwable.getMessage();
                            _state.setValue(state);
                        })
        );
    }

    @Override
    protected void onCleared()
    {
        compositeDisposable.dispose();
        net.compositeDisposable.dispose();
        super.onCleared();
    }



    static class State {
        boolean isAuth = false;
        Users users = null;
        String error = "";
    }

    private final MutableLiveData<State> _state = new MutableLiveData<>(new State());
    public LiveData<State> getState()
    {
        return _state;
    }
    public void setState(State state) {
        _state.setValue(state);
    }

}