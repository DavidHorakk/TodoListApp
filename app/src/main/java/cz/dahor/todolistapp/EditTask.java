package cz.dahor.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTask extends AppCompatActivity {

    public static final String EXTRA_ID = "cz.dahor.todolistapp.EXTRA_ID";
    public static final String EXTRA_TITLE ="cz.dahor.todolistapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION ="cz.dahor.todolistapp.EXTRA_DESCRIPTION";

    private EditText editTextTitle, editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = findViewById(R.id.edit_title2);
        editTextDescription = findViewById(R.id.edit_description2);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }else{
            setTitle("Add note");
        }

        final Button button = findViewById(R.id.button_save2);

        button.setOnClickListener(view -> {
            saveNote();
        });

    }

    public void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_LONG).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id !=-1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}