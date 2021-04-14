package cz.dahor.todolistapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import cz.dahor.todolistapp.util.Utilities;

public class AddTask extends AppCompatActivity implements SensorEventListener {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditTitle, mEditDescription;
    private String title, description, created, finished;
    private String longitude, latitude;
    private int priority;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    //accelerator attributes
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerAvailable = false;
    private boolean itIsNotFirstTime = false;
    private float threshold = 5f;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float diffX, diffY, diffZ;
    //location attributes
    private Button btnAddLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0;
    private TextView txtLocation, txtEditCreated, txtEditFinished;
    private NumberPicker numPickerPriority;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        final Button button = findViewById(R.id.button_save);
        btnAddLocation = findViewById(R.id.btn_add_location);
        txtLocation = findViewById(R.id.txtLocation);
        txtEditCreated = findViewById(R.id.edit_created);
        txtEditFinished = findViewById(R.id.edit_finished);
        numPickerPriority = findViewById(R.id.numPickerPriority);

        numPickerPriority.setMinValue(1);
        numPickerPriority.setMaxValue(10);
        numPickerPriority.setValue(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        created = Utilities.getToday();
        txtEditCreated.setText(created);

        txtEditFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog( AddTask.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                dateDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                finished = dayOfMonth+"."+month+"."+year;
                txtEditFinished.setText(dayOfMonth+"."+month+"."+year);
            }
        };

        btnAddLocation.setOnClickListener(v -> {

            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
            Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
        });

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
                 priority = numPickerPriority.getValue();
                Bundle extras = new Bundle();
                extras.putString("EXTRA_TITLE",title);

                 if(!description.isEmpty()){
                     extras.putString("EXTRA_DESCRIPTION",description);
                     replyIntent.putExtras(extras);
                 }
                 if(longitude != null && latitude != null){
                     extras.putString("EXTRA_LONGITUDE",longitude);
                     extras.putString("EXTRA_LATITUDE",latitude);
                 }
                 if(!created.isEmpty()){
                     extras.putString("EXTRA_CREATED", created);
                 }
                if(!finished.isEmpty()){
                    extras.putString("EXTRA_FINISHED", finished);
                }
                if(priority != 0){
                    extras.putInt("EXTRA_PRIORITY", priority);
                }

                 replyIntent.putExtras(extras);
                 setResult(RESULT_OK, replyIntent);



            }
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case ACCESS_COARSE_LOCATION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    break;
        }
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
                mEditTitle.setText("");
                mEditDescription.setText("");
                txtEditFinished.setText("Date");
                numPickerPriority.setValue(1);
                txtLocation.setText("Location:");
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

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        longitude = String.valueOf(location.getLongitude());
                        latitude = String.valueOf(location.getLatitude());
                        txtLocation.setText("Location: latitude="+latitude+" longtitude="+longitude);
                        Log.e("location", "location ok " +longitude+ "," +latitude);
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("location", "location nok" + e.getMessage());
            }
        });
    }
}
