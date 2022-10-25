package com.renatsayf.mobileclientsitec.repository.db;

import com.renatsayf.mobileclientsitec.model.users.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import io.reactivex.Single;

@Dao
public interface AppDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = User.class)
    Single<List<Long>> insertOrUpdate(List<User> users);
}
