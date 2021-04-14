package cz.dahor.todolistapp.model;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;

    private final LiveData<List<Todo>> mAllTodos;

    public TodoViewModel (Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() { return mAllTodos; }

    public void insert(Todo todo) { mRepository.insert(todo); }

    public void delete (Todo todo){
        mRepository.delete(todo);
    }

    public void update(Todo todo) {
        mRepository.update(todo);
    }

    public void deleteAllData(){
        mRepository.deleteAllData();
    }
}
