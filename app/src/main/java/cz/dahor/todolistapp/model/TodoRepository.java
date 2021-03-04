package cz.dahor.todolistapp.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepository {

private TodoDao mTodoDao;
private LiveData<List<Todo>> mAllTodos;

        // Note that in order to unit test the WordRepository, you have to remove the Application
        // dependency. This adds complexity and much more code, and this sample is not about testing.
        // See the BasicSample in the android-architecture-components repository at
        // https://github.com/googlesamples
        TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
            mTodoDao = db.todoDao();
            mAllTodos = mTodoDao.readAllData();
        }

        // Room executes all queries on a separate thread.
        // Observed LiveData will notify the observer when the data has changed.
        LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
        }

        // You must call this on a non-UI thread or your app will throw an exception. Room ensures
        // that you're not doing any long running operations on the main thread, blocking the UI.
        void insert(Todo todo) {
                TodoDatabase.databaseWriteExecutor.execute(() -> {
                    mTodoDao.insert(todo);
                });
        }

        void delete(Todo todo) {
                TodoDatabase.databaseWriteExecutor.execute(() -> {
                        mTodoDao.delete(todo);
                });
        }
        void update(Todo todo){
                TodoDatabase.databaseWriteExecutor.execute(() -> {
                        mTodoDao.update(todo);
                });
        }
}
