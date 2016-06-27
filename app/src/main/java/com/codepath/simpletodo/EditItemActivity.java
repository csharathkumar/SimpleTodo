package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.simpletodo.model.Enums;
import com.codepath.simpletodo.model.TodoItem;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialogFragment.Callbacks {
    public static final String EXTRA_TASK = "task";
    public static final String EXTRA_TASK_POSITION = "task_position";
    public static final String EXTRA_IS_EDIT = "is_edit";
    private static final String TAG = EditItemActivity.class.getSimpleName();

    boolean isEdit;
    int mPosition;
    TodoItem mTodoItem;
    Spinner prioritySpinner;
    Spinner statusSpinner;
    EditText taskNameET;
    EditText taskDescriptionET;
    TextView setDueDateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT,false);
        mPosition = getIntent().getIntExtra(EXTRA_TASK_POSITION,0);
        if(isEdit){
            mTodoItem = (TodoItem) getIntent().getSerializableExtra(EXTRA_TASK);
        }else{
            mTodoItem = new TodoItem();
        }
        setDueDateTV = (TextView) findViewById(R.id.dueDateTV);
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_priority_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(this);
        if(mTodoItem.todoItemPriority == Enums.TodoItemPriority.LOW){
            prioritySpinner.setSelection(0);
        }else if(mTodoItem.todoItemPriority == Enums.TodoItemPriority.MEDIUM){
            prioritySpinner.setSelection(1);
        }else{
            prioritySpinner.setSelection(2);
        }
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.task_status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(this);
        if(mTodoItem.todoItemStatus == Enums.TodoItemStatus.TO_DO){
            statusSpinner.setSelection(0);
        }else{
            statusSpinner.setSelection(1);
        }

        taskNameET = (EditText) findViewById(R.id.editTask);
        taskNameET.setText(mTodoItem.todoItemName);
        taskNameET.setSelection(taskNameET.getText().length());

        taskDescriptionET = (EditText) findViewById(R.id.taskDescriptionET);
        taskDescriptionET.setText(mTodoItem.todoItemDescription);
        if(mTodoItem.todoItemDueDateLong > 0){
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(this);
            String formattedDate = dateFormat.format(new Date(mTodoItem.todoItemDueDateLong));
            setDueDateTV.setText("Due on "+formattedDate);
        }
        setDueDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.newInstance(mTodoItem.todoItemDueDateLong);
                datePickerDialogFragment.setCancelable(false);
                //datePickerDialogFragment.setTargetFragment(PublicShareDetailsFragment.this, DatePickerDialogFragment.SET_EXPIRY_REQUEST_CODE);
                datePickerDialogFragment.show(getSupportFragmentManager(),"Pick a date");
            }
        });

    }

    public void saveEditedItem(View view) {
        String editedTask = taskNameET.getText().toString();
        Intent returnData = new Intent();
        mTodoItem.todoItemName = editedTask;
        mTodoItem.todoItemDescription = taskDescriptionET.getText().toString();
        returnData.putExtra(EXTRA_TASK, (Serializable) mTodoItem);
        returnData.putExtra(EXTRA_TASK_POSITION,mPosition);
        setResult(RESULT_OK,returnData);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"Parent view id is - "+parent.getId());
        switch(parent.getId()){
            case R.id.statusSpinner:
                if(position == 0){
                    mTodoItem.todoItemStatus = Enums.TodoItemStatus.TO_DO;
                }else{
                    mTodoItem.todoItemStatus = Enums.TodoItemStatus.DONE;
                }
                break;
            case R.id.prioritySpinner:
                if(position == 0){
                    mTodoItem.todoItemPriority = Enums.TodoItemPriority.LOW;
                }else if(position == 1){
                    mTodoItem.todoItemPriority = Enums.TodoItemPriority.MEDIUM;
                }else{
                    mTodoItem.todoItemPriority = Enums.TodoItemPriority.HIGH;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(long dueDateTS, String dueDateString) {
        setDueDateTV.setText("Due on "+dueDateString);
        mTodoItem.todoItemDueDateLong = dueDateTS;
    }
}
