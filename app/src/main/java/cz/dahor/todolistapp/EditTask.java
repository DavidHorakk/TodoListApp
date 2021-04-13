package cz.dahor.todolistapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTask extends AppCompatActivity implements SensorEventListener {

    public static final String EXTRA_ID = "cz.dahor.todolistapp.EXTRA_ID";
    public static final String EXTRA_TITLE ="cz.dahor.todolistapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION ="cz.dahor.todolistapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "cz.dahor.todolistapp.EXTRA_PRIORITY";
    public static final String EXTRA_LONGITUDE = "cz.dahor.todolistapp.EXTRA_LONGITUDE";
    public static final String EXTRA_LATITUDE = "cz.dahor.todolistapp.EXTRA_LATITUDE";
    public static final String EXTRA_CREATED = "cz.dahor.todolistapp.EXTRA_CREATED";
    public static final String EXTRA_FINISHED = "cz.dahor.todolistapp.EXTRA_FINISHED";
    private EditText editTextTitle, editTextDescription;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerAvailable = false;
    private boolean itIsNotFirstTime = false;
    private float threshold = 5f;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float diffX, diffY, diffZ;

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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager != null){
            if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                isAccelerometerAvailable=true;
            }else{
                Log.e("accelerometer", "Error, accelerometer unavailable");
                isAccelerometerAvailable=false;
            }
        }


        final Button button = findViewById(R.id.button_save2);

        button.setOnClickListener(view -> {
            saveNote();
        });

    }

    public void onSensorChanged(SensorEvent event) {
        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];

        if(itIsNotFirstTime){
            diffX = Math.abs(lastX - currentX);
            diffY = Math.abs(lastY - currentY);
            diffZ = Math.abs(lastZ - currentZ);

            if(diffX > threshold && diffY > threshold && diffZ > threshold){
                editTextTitle.setText("");
                editTextDescription.setText("");
            }

        }
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        itIsNotFirstTime = true;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(isAccelerometerAvailable){
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerAvailable){
            sensorManager.unregisterListener(this);
        }
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
        //TODO přidat vlastní atributy a dodělat to podle addTask
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id !=-1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}