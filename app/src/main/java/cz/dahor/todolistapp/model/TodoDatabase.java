package cz.dahor.todolistapp.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    abstract TodoDao todoDao();
    private static volatile TodoDatabase INSTANCE;  // zadefinoval jsem si singleton, abych předešel otevírání více instancí v DB najednou
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);  // zadefinovali jsme si executor service aby naše tasky na databázi běžely asynchronně na pozadí

    static TodoDatabase getDatabase(final Context context) { // vrací singleton
        if (INSTANCE == null) {
            synchronized (TodoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoDatabase.class, "todo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
