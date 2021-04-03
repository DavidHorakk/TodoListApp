package cz.dahor.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity implements SensorEventListener {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditTitle, mEditDescription;
    private String title, description;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerAvailable = false;
    private boolean itIsNotFirstTime = false;
    private float threshold = 5f;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float diffX, diffY, diffZ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        final Button button = findViewById(R.id.button_save);

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];

        if(itIsNotFirstTime){
            diffX = Math.abs(lastX - currentX);
            diffY = Math.abs(lastY - currentY);
            diffZ = Math.abs(lastZ - currentZ);

            if(diffX > threshold && diffY > threshold && diffZ > threshold){

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
}
