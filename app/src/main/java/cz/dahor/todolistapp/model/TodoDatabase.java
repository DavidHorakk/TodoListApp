package cz.dahor.todolistapp.model;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TodoDao dao = INSTANCE.todoDao();
//                dao.deleteAll();

                Todo todo = new Todo("úkol","dodělat úkol");
                dao.insert(todo);
                todo = new Todo("úkol2","dodělat úkol2");
                dao.insert(todo);
            });
        }
    };





}
