package com.renatsayf.mobileclientsitec.di.modules;


import com.renatsayf.mobileclientsitec.repository.net.IApi;
import com.renatsayf.mobileclientsitec.repository.net.NetRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class NetRepositoryModule
{
    @Inject
    public NetRepositoryModule(){}

    @Provides
    @Singleton
    public NetRepository provideNetRepository(IApi api) {
        return new NetRepository(api);
    }
}
