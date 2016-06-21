package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_NAME = "task_name";
    public static final String EXTRA_TASK_POSITION = "task_position";

    int mPosition;
    String mTaskName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mTaskName = getIntent().getStringExtra(EXTRA_TASK_NAME);
        mPosition = getIntent().getIntExtra(EXTRA_TASK_POSITION,0);

        EditText etEditTask = (EditText) findViewById(R.id.editTask);
        etEditTask.setText(mTaskName);
        etEditTask.setSelection(etEditTask.getText().length());
    }

    public void saveEditedItem(View view) {
        EditText etEditTask = (EditText) findViewById(R.id.editTask);
        String editedTask = etEditTask.getText().toString();
        Intent returnData = new Intent();
        returnData.putExtra(EXTRA_TASK_NAME,editedTask);
        returnData.putExtra(EXTRA_TASK_POSITION,mPosition);
        setResult(RESULT_OK,returnData);
        finish();
    }
}
