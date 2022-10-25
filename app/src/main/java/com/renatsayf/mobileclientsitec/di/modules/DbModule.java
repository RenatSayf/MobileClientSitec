package com.renatsayf.mobileclientsitec.di.modules;


import android.content.Context;
import com.renatsayf.mobileclientsitec.repository.db.AppDao;
import com.renatsayf.mobileclientsitec.repository.db.AppDataBase;
import javax.inject.Inject;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@InstallIn(SingletonComponent.class)
@Module
public class DbModule
{
    @Inject
    public DbModule(){}

    @Provides
    @Singleton
    public AppDao provideDb(@ApplicationContext Context context) {
        return AppDataBase.buildDataBase(context).appDao();
    }
}
