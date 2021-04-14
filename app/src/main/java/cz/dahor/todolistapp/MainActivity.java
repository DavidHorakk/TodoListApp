package cz.dahor.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cz.dahor.todolistapp.adapter.TodoListAdapter;
import cz.dahor.todolistapp.model.Todo;
import cz.dahor.todolistapp.model.TodoViewModel;

public class MainActivity extends AppCompatActivity {


    public static final int ADD_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TODO_ACTIVITY_REQUEST_CODE = 2;
    private TodoViewModel todoViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivityForResult(intent, ADD_WORD_ACTIVITY_REQUEST_CODE);
        });

        recyclerView = findViewById(R.id.recyclerview);
        final TodoListAdapter adapter = new TodoListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);  // Asociace View modelu a aktivity
//        todoViewModel.getAllTodos().observe(this, todos -> {
//            // Update the cached copy of the words in the adapter.
//            adapter.setTodos(todos);
//        });
        todoViewModel.getAllTodos().observe(this, adapter::setTodos);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                todoViewModel.delete(adapter.getTodoAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickedListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(MainActivity.this, EditTask.class);
                intent.putExtra(EditTask.EXTRA_ID, todo.getId());
                intent.putExtra(EditTask.EXTRA_TITLE, todo.getTitle());
                intent.putExtra(EditTask.EXTRA_DESCRIPTION, todo.getDescription());
                intent.putExtra(EditTask.EXTRA_PRIORITY, todo.getPriority());
                intent.putExtra(EditTask.EXTRA_LONGITUDE, todo.getLongitude());
                intent.putExtra(EditTask.EXTRA_LATITUDE, todo.getLatitude());
                intent.putExtra(EditTask.EXTRA_CREATED, todo.getCreated());
                intent.putExtra(EditTask.EXTRA_FINISHED, todo.getFinished());
                startActivityForResult(intent, EDIT_TODO_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String title, description, longitude, latitude, created, finished;
            int priority;
            title = extras.getString("EXTRA_TITLE");
            description = extras.getString("EXTRA_DESCRIPTION");
            priority = extras.getInt("EXTRA_PRIORITY");
            longitude = extras.getString("EXTRA_LONGITUDE");
            latitude = extras.getString("EXTRA_LATITUDE");
            created = extras.getString("EXTRA_CREATED");
            finished = extras.getString("EXTRA_FINISHED");

            Todo todo;
            if(description != null || priority != 0 || longitude != null || latitude != null || created != null || finished != null){
                todo = new Todo(title, description, created, finished, longitude, latitude, priority);
            }else{
            todo = new Todo(title);
            }
            todoViewModel.insert(todo);
        } else if (requestCode == EDIT_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditTask.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Note cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title, description, longitude, latitude, created, finished;
            int priority;
            title = data.getStringExtra(EditTask.EXTRA_TITLE);
            description = data.getStringExtra(EditTask.EXTRA_DESCRIPTION);
            priority = data.getIntExtra(EditTask.EXTRA_PRIORITY, 1);
            longitude = data.getStringExtra(EditTask.EXTRA_LONGITUDE);
            latitude = data.getStringExtra(EditTask.EXTRA_LATITUDE);
            created = data.getStringExtra(EditTask.EXTRA_CREATED);
            finished = data.getStringExtra(EditTask.EXTRA_FINISHED);
            Todo todo = new Todo(title, description, created, finished, longitude, latitude, priority);
            todo.setId(id);
            todoViewModel.update(todo);

        } else {
            Toast.makeText(
                    getApplicationContext(),
//                    R.string.empty_not_saved,
                    "Cannot insert",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_deleteAll:
                todoViewModel.deleteAllData();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}