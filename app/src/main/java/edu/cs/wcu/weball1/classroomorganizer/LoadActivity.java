package edu.cs.wcu.weball1.classroomorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends AppCompatActivity implements MyAdapter.ItemWasClicked {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // Get all of the file names
        List<String> fileNames = new ArrayList<>();
        //File[] files = this.getFilesDir().listFiles();
        File dir = this.getExternalFilesDir(null);
        assert dir != null;
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                fileNames.add(file.toString());
            } // end for
        } // end if

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.file_name_view);
        RecyclerView.Adapter myAdapter = new MyAdapter(fileNames, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void itemClicked(String text) {
        Intent attendance = new Intent(this, TabbedAttendanceActivity.class);
        String fullPath = this.getExternalFilesDir(null).toString();
        fullPath += "/" + text;
        attendance.putExtra("loadedFile", fullPath);
        attendance.putExtra("fileName", text);
        this.setResult(1, attendance);
        finish();
    }
}