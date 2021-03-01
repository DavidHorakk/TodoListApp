package cz.dahor.todolistapp;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cz.dahor.todolistapp.adapter.TodoListAdapter;
import cz.dahor.todolistapp.model.Todo;
import cz.dahor.todolistapp.model.TodoViewModel;

public class MainActivity extends AppCompatActivity {


    public static final int ADD_WORD_ACTIVITY_REQUEST_CODE = 1;
    private TodoViewModel todoViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivityForResult(intent, ADD_WORD_ACTIVITY_REQUEST_CODE);
        });



        recyclerView = findViewById(R.id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter(new TodoListAdapter.WordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);  // Asociace View modelu a aktivity
        todoViewModel.getAllTodos().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String title, description;
            title = extras.getString("EXTRA_TITLE");
//            description = extras.getString("EXTRA_DESCRIPTION");
            Todo todo;
//            if(!description.isEmpty()){
//                todo = new Todo(title, description);
//            }else{
                todo = new Todo(title);
//            }
            todoViewModel.insert(todo);
        } else {
            Toast.makeText(
                    getApplicationContext(),
//                    R.string.empty_not_saved,
                    "Cannot insert",
                    Toast.LENGTH_LONG).show();
        }

    }
}