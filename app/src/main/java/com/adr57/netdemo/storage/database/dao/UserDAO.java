package com.adr57.netdemo.storage.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adr57.netdemo.storage.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

//    @Query("SELECT COUNT(*) FROM users WHERE uid = :uid")
//    int isUidExists(String uid);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE user_name LIKE :username LIMIT 1")
    User findByName(String username);

    @Query("SELECT * FROM users WHERE email LIKE :email LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users WHERE display_name LIKE :displayName")
    User findByDisplayName(String displayName);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Delete
    void delete(User user);
}
