package com.renatsayf.mobileclientsitec.repository.db;

import com.renatsayf.mobileclientsitec.model.users.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class DbRepository implements AppDao
{
    private final AppDao dao;
    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DbRepository(AppDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<List<Long>> insertOrUpdate(List<User> users)
    {
        return Single.create(emitter -> {
            Disposable subscribe = dao.insertOrUpdate(users)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ids -> {
                        emitter.onSuccess(ids);
                    }, throwable -> {
                        emitter.onError(throwable);
                    });
            compositeDisposable.add(subscribe);
        });
    }
}
