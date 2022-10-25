package com.renatsayf.mobileclientsitec.repository.net;

import com.renatsayf.mobileclientsitec.model.auth.AuthCode;
import com.renatsayf.mobileclientsitec.model.users.Users;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


@SuppressWarnings({"CodeBlock2Expr", "Convert2MethodRef"})
public class NetRepository
{
    private final IApi api;
    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public NetRepository(IApi api) {
        this.api = api;
    }

    public Single<Response<AuthCode>> auth(
            String imei,
            String uid,
            String pass,
            Boolean copyFromDevice,
            String nfc
    ) {
        return Single.create( emitter -> {
            Disposable subscribe = api.auth(imei, uid, pass, copyFromDevice, nfc)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(authCode -> {
                        emitter.onSuccess(authCode);
                    }, throwable -> {
                        emitter.onError(throwable);
                    });
            compositeDisposable.add(subscribe);
        });
    }

    public Single<Response<Users>> getUsers(String imei) {
        return Single.create( emitter -> {
            Disposable subscribe = api.getUsers(imei)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(users -> {
                        emitter.onSuccess(users);
                    }, throwable -> {
                        emitter.onError(throwable);
                    });
            compositeDisposable.add(subscribe);
        });

    }
}
