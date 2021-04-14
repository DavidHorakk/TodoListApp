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
    private String title, description, created, finished;
    private String longitude, latitude;
    private int priority;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextTitle = findViewById(R.id.edit_title2);
        editTextDescription = findViewById(R.id.edit_description2);
        txtLocation= findViewById(R.id.txtLocation2);
        txtEditCreated = findViewById(R.id.edit_created2);
        txtEditFinished = findViewById(R.id.edit_finished2);
        numPickerPriority = findViewById(R.id.numPickerPriority2);
        btnAddLocation = findViewById(R.id.btn_add_location2);

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

                DatePickerDialog dateDialog = new DatePickerDialog( EditTask.this,
                        android.R.style.Theme_DeviceDefault,
                        mDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                dateDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                finished = dayOfMonth+"."+month+"."+year;
                txtEditFinished.setText(dayOfMonth+"."+month+"."+year);
            }
        };

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
            txtLocation.setText("Location: latitude="+intent.getStringExtra(EXTRA_LONGITUDE)+" longtitude="+intent.getStringExtra(EXTRA_LATITUDE));
            txtEditCreated.setText(intent.getStringExtra(EXTRA_CREATED));
            txtEditFinished.setText(intent.getStringExtra(EXTRA_FINISHED));


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

        final Button btnSave = findViewById(R.id.button_save2);

        btnSave.setOnClickListener(view -> {
            saveNote();
        });

        btnAddLocation.setOnClickListener(v -> {

            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
            Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
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


    public void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        priority = numPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_LONG).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        if(!description.isEmpty()){

        }
        if(longitude != null && latitude != null){
            data.putExtra(EXTRA_LONGITUDE, longitude);
            data.putExtra(EXTRA_LATITUDE, latitude);
        }
        if(!created.isEmpty()){
            data.putExtra(EXTRA_CREATED, created);
        }
        if(!finished.isEmpty()){
            data.putExtra(EXTRA_FINISHED, finished);
        }
        if(priority != 0){
            data.putExtra(EXTRA_PRIORITY, priority);
        }




        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id !=-1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}