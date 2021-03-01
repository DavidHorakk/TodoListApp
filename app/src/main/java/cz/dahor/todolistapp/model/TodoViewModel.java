package cz.dahor.todolistapp.model;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cz.dahor.todolistapp.model.Todo;
import cz.dahor.todolistapp.model.TodoRepository;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;

    private final LiveData<List<Todo>> mAllTodos;

    public TodoViewModel (Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    LiveData<List<Todo>> getAllWords() { return mAllTodos; }

    public void insert(Todo todo) { mRepository.insert(todo); }
}
