package cz.dahor.todolistapp.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE) // When identic user added, conflict is ignored
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    LiveData<List<Todo>> readAllData();
    
    @Query("DELETE FROM todo_table")
    void deleteAllData();
}
