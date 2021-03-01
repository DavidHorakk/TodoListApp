package cz.dahor.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditTitle, mEditDescription;
    private String title, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);

        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditTitle.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                 title = mEditTitle.getText().toString();
                 description = mEditDescription.getText().toString();
                Bundle extras = new Bundle();
                extras.putString("EXTRA_TITLE",title);

                 if(!description.isEmpty()){
                     extras.putString("EXTRA_DESCRIPTION",description);
                     replyIntent.putExtras(extras);
                     setResult(RESULT_OK, replyIntent);
                 }else{
                     replyIntent.putExtras(extras);
                     setResult(RESULT_OK, replyIntent);
                 }

            }
            finish();
        });
    }
}
