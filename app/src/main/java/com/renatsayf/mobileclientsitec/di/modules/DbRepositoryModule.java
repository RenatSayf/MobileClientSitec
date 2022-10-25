package com.renatsayf.mobileclientsitec.di.modules;


import com.renatsayf.mobileclientsitec.repository.db.AppDao;
import com.renatsayf.mobileclientsitec.repository.db.DbRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DbRepositoryModule
{
    @Inject
    public DbRepositoryModule(){}

    @Provides
    @Singleton
    public DbRepository provideDbRepository(AppDao dao) {
        return new DbRepository(dao);
    }
}
