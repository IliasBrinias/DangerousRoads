package com.unipi.msc.dangerousroads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.msc.dangerousroads.Constants.Constants;
import com.unipi.msc.dangerousroads.Databae.AppDatabase;
import com.unipi.msc.dangerousroads.Databae.Location;
import com.unipi.msc.dangerousroads.Databae.LocationDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, Constants.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
            db.locationDao().insertLocation(new Location(1561651L,1221345,12.5));
            StringBuilder stringBuilder = new StringBuilder();
            db.locationDao().getAll().forEach(location -> stringBuilder.append(location.toString()));
            ((TextView) findViewById(R.id.textView)).setText(stringBuilder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}