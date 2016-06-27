package com.codepath.simpletodo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.codepath.simpletodo.data.TodoItemsAdapter;
import com.codepath.simpletodo.data.TodoItemsDatabaseHelper;
import com.codepath.simpletodo.model.TodoItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int EDIT_REQUEST_CODE = 21;
    public static final int ADD_REQUEST_CODE = 20;
    List<TodoItem> items;
    TodoItemsAdapter itemsAdapter;
    ListView lvItems;
    TodoItemsDatabaseHelper todoItemsDatabaseHelper;
    FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoItemsDatabaseHelper = TodoItemsDatabaseHelper.getsInstance(this);
        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new TodoItemsAdapter(this,items);
        lvItems.setAdapter(itemsAdapter);

        mFab = (FloatingActionButton) findViewById(R.id.addTask);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this,EditItemActivity.class);
                addIntent.putExtra(EditItemActivity.EXTRA_TASK_POSITION,items.size());
                addIntent.putExtra(EditItemActivity.EXTRA_IS_EDIT,false);
                startActivityForResult(addIntent,ADD_REQUEST_CODE);
            }
        });
        setUpListViewListerner();
        setUpListViewClickListener();
    }

    private void setUpListViewListerner() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem = items.get(position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                todoItemsDatabaseHelper.deleteTodoItem(todoItem);
                return true;
            }
        });
    }

    private void setUpListViewClickListener(){
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this,EditItemActivity.class);
                editIntent.putExtra(EditItemActivity.EXTRA_TASK, (Serializable) items.get(position));
                editIntent.putExtra(EditItemActivity.EXTRA_TASK_POSITION,position);
                editIntent.putExtra(EditItemActivity.EXTRA_IS_EDIT,true);
                startActivityForResult(editIntent,EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_REQUEST_CODE || requestCode == ADD_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                if(data != null){
                    TodoItem edited = (TodoItem) data.getSerializableExtra(EditItemActivity.EXTRA_TASK);
                    int mPosition = data.getIntExtra(EditItemActivity.EXTRA_TASK_POSITION,0);
                    if(requestCode == EDIT_REQUEST_CODE){
                        items.set(mPosition,edited);
                        todoItemsDatabaseHelper.updateTodoItem(edited);
                    }else{
                        items.add(edited);
                        todoItemsDatabaseHelper.addTodoItem(edited);
                    }
                    itemsAdapter.notifyDataSetChanged();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItem = etNewItem.getText().toString();
        TodoItem todoItem = new TodoItem();
        todoItem.todoItemName = newItem;
        int todoItemId = addItemToDatabase(todoItem);
        todoItem.todoItemId = todoItemId;
        itemsAdapter.add(todoItem);
        etNewItem.setText("");
    }*/

    private void readItems(){
        items = todoItemsDatabaseHelper.getAllTodoItems();
    }

    private int addItemToDatabase(TodoItem todoItem){
        return todoItemsDatabaseHelper.addTodoItem(todoItem);
    }
}
