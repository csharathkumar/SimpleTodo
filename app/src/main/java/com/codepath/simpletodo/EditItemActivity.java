package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codepath.simpletodo.model.TodoItem;

import java.io.Serializable;

public class EditItemActivity extends AppCompatActivity {
    public static final String EXTRA_TASK = "task";
    public static final String EXTRA_TASK_POSITION = "task_position";

    int mPosition;
    TodoItem mTodoItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mTodoItem = (TodoItem) getIntent().getSerializableExtra(EXTRA_TASK);
        mPosition = getIntent().getIntExtra(EXTRA_TASK_POSITION,0);

        EditText etEditTask = (EditText) findViewById(R.id.editTask);
        etEditTask.setText(mTodoItem.todoItemName);
        etEditTask.setSelection(etEditTask.getText().length());
    }

    public void saveEditedItem(View view) {
        EditText etEditTask = (EditText) findViewById(R.id.editTask);
        String editedTask = etEditTask.getText().toString();
        Intent returnData = new Intent();
        mTodoItem.todoItemName = editedTask;
        returnData.putExtra(EXTRA_TASK, (Serializable) mTodoItem);
        returnData.putExtra(EXTRA_TASK_POSITION,mPosition);
        setResult(RESULT_OK,returnData);
        finish();
    }
}
