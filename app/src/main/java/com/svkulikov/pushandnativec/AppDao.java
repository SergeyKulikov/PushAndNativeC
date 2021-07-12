package com.svkulikov.pushandnativec;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface AppDao {
    /** Исключительно для создания продукта при первом запуске в MainActivity */

    @Query("SELECT * FROM MessageData ORDER BY eventTime DESC")
    Flowable<List<MessageData>> rx_loadMessageList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> rx_saveMessage(MessageData messageData);

}