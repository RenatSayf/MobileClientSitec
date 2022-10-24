package com.renatsayf.mobileclientsitec.ui.users;

import com.renatsayf.mobileclientsitec.model.users.Users;
import com.renatsayf.mobileclientsitec.repository.net.NetRepository;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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

    public void getUsers(String imei) {
        compositeDisposable.add(
                net.getUsers(imei)
                        .subscribe(users -> {
                            Users users1 = users;
                            return;
                        }, throwable -> {
                            throwable.printStackTrace();
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
}