package com.renatsayf.mobileclientsitec.ui.users;

import com.renatsayf.mobileclientsitec.model.auth.AuthCode;
import com.renatsayf.mobileclientsitec.model.users.User;
import com.renatsayf.mobileclientsitec.model.users.Users;
import com.renatsayf.mobileclientsitec.repository.db.DbRepository;
import com.renatsayf.mobileclientsitec.repository.net.NetRepository;
import java.util.List;
import javax.inject.Inject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.Disposable;



@SuppressWarnings("Convert2MethodRef")
@HiltViewModel
public class UsersViewModel extends ViewModel
{
    private final NetRepository net;
    private final DbRepository db;
    private Disposable subscribe;

    @Inject
    public UsersViewModel(NetRepository net, DbRepository db) {
        this.net = net;
        this.db = db;
    }

    public void auth(String imei, String uid, String pass) {
        subscribe = net.auth(imei, uid, pass, false, "")
                .subscribe(response -> {
                    int httpCode = response.code();
                    AuthCode authCode = response.body();
                    State state = new State();
                    if (httpCode >= 200 && httpCode <= 202 && authCode != null)
                    {
                        state.isAuth = true;
                        _state.setValue(state);
                    } else
                    {
                        state.error = "Failed authorization";
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    State state = new State();
                    state.error = throwable.getMessage();
                    _state.setValue(state);
                });
    }

    public void getUsers(String imei) {
        subscribe = net.getUsers(imei)
                .subscribe(response -> {
                    if (response.code() == 200)
                    {
                        Users body = response.body();
                        State state = new State();
                        state.isAuth = true;
                        if (body != null) {
                            state.users = body.users.listUsers;
                        } else
                        {
                            state.users = null;
                        }
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
                });
    }

    public void saveUsers(List<User> list) {
        subscribe = db.insertOrUpdate(list)
                .subscribe(unused -> {
                    State state = new State();
                    state.isAuth = true;
                    state.users = list;
                    state.saved = true;
                    state.error = "";
                    _state.setValue(state);
                }, throwable -> {
                    State state = new State();
                    state.isAuth = true;
                    state.users = null;
                    state.saved = false;
                    state.error = throwable.getMessage();
                    _state.setValue(state);
                });
    }

    @Override
    protected void onCleared()
    {
        subscribe.dispose();
        super.onCleared();
    }

    static class State {
        boolean isAuth = false;
        List<User> users = null;
        String error = "";
        boolean saved = false;
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