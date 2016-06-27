package com.codepath.simpletodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.simpletodo.model.Enums;
import com.codepath.simpletodo.model.TodoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharath on 6/25/16.
 */
public class TodoItemsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = TodoItemsDatabaseHelper.class.getSimpleName();
    private static TodoItemsDatabaseHelper sInstance;
    private static final String DATABASE_NAME = "todo_database";
    private static final int DATABASE_VERSION = 2;

    private static final String TODO_ITEMS_TABLE = "todo_items";

    private static final String KEY_TODO_ITEM_ID = "todo_item_id";
    private static final String KEY_TODO_ITEM_NAME = "todo_item_name";
    private static final String KEY_TODO_ITEM_DESCRIPTION = "todo_item_description";
    private static final String KEY_TODO_ITEM_PRIORITY = "todo_item_priority";
    private static final String KEY_TODO_ITEM_STATUS = "todo_item_status";
    private static final String KEY_TODO_ITEM_DUE_DATE = "todo_item_due_date";

    public static TodoItemsDatabaseHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new TodoItemsDatabaseHelper(context);
        }
        return sInstance;
    }

    public TodoItemsDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TODO_ITEMS_TABLE +
                "(" +
                    KEY_TODO_ITEM_ID + " INTEGER PRIMARY KEY, "+
                    KEY_TODO_ITEM_NAME + " TEXT,  " +
                    KEY_TODO_ITEM_DESCRIPTION + " TEXT,  " +
                    KEY_TODO_ITEM_PRIORITY + " INTEGER, " +
                    KEY_TODO_ITEM_STATUS + " INTEGER , " +
                    KEY_TODO_ITEM_DUE_DATE + " INTEGER " +
                ")";
        Log.d(TAG,"The create table query is - "+CREATE_TODO_ITEMS_TABLE);
        db.execSQL(CREATE_TODO_ITEMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2){
            String updateQuery = "ALTER TABLE "+TODO_ITEMS_TABLE + " ADD "+KEY_TODO_ITEM_DESCRIPTION+" TEXT ";
            db.execSQL(updateQuery);

            updateQuery = "ALTER TABLE "+TODO_ITEMS_TABLE + " ADD "+KEY_TODO_ITEM_STATUS+" INTEGER DEFAULT 0";
            db.execSQL(updateQuery);

            updateQuery = "ALTER TABLE "+TODO_ITEMS_TABLE + " ADD "+KEY_TODO_ITEM_PRIORITY+" INTEGER DEFAULT 0";
            db.execSQL(updateQuery);

            updateQuery = "ALTER TABLE "+TODO_ITEMS_TABLE + " ADD "+KEY_TODO_ITEM_DUE_DATE+" INTEGER DEFAULT 0";
            db.execSQL(updateQuery);
        }
    }

    public int addTodoItem(TodoItem todoItem){
        int returnValue = -1;
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TODO_ITEM_NAME,todoItem.todoItemName);
            contentValues.put(KEY_TODO_ITEM_DESCRIPTION,todoItem.todoItemDescription);
            contentValues.put(KEY_TODO_ITEM_PRIORITY,todoItem.todoItemPriority.getPriorityLevel());
            contentValues.put(KEY_TODO_ITEM_STATUS,todoItem.todoItemStatus.getItemStatus());
            contentValues.put(KEY_TODO_ITEM_DUE_DATE,todoItem.todoItemDueDateLong);
            long rowId = database.insertOrThrow(TODO_ITEMS_TABLE,null,contentValues);
            returnValue = ((int) rowId);
            database.setTransactionSuccessful();
        }catch(Exception e){
            Log.d(TAG, "Error while trying to add todo item to database");
        } finally {
            database.endTransaction();
        }
        return returnValue;
    }

    public boolean updateTodoItem(TodoItem todoItem){
        boolean updateSucceeded = false;
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TODO_ITEM_ID,todoItem.todoItemId);
            contentValues.put(KEY_TODO_ITEM_NAME,todoItem.todoItemName);
            contentValues.put(KEY_TODO_ITEM_DESCRIPTION,todoItem.todoItemDescription);
            contentValues.put(KEY_TODO_ITEM_PRIORITY,todoItem.todoItemPriority.getPriorityLevel());
            contentValues.put(KEY_TODO_ITEM_STATUS,todoItem.todoItemStatus.getItemStatus());
            contentValues.put(KEY_TODO_ITEM_DUE_DATE,todoItem.todoItemDueDateLong);

            int rows = database.update(TODO_ITEMS_TABLE,contentValues,KEY_TODO_ITEM_ID+"=?",new String[]{String.valueOf(todoItem.todoItemId)});
            if(rows == 1){
                updateSucceeded = true;
            }
            database.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG,"Error while updating a todo item");
        }finally {
            database.endTransaction();
        }
        return updateSucceeded;
    }

    public boolean deleteTodoItem(TodoItem todoItem){
        boolean deleteSucceeded = false;
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try{
            int rows = database.delete(TODO_ITEMS_TABLE,KEY_TODO_ITEM_ID+"=?",new String[]{String.valueOf(todoItem.todoItemId)});
            if(rows == 1){
                deleteSucceeded = true;
            }
            database.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(TAG,"Exception while deleting the todo item");
        }finally {
            database.endTransaction();
        }
        return deleteSucceeded;
    }

    public List<TodoItem> getAllTodoItems(){
        List<TodoItem> todoItemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TODO_ITEMS_TABLE;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        Log.d(TAG,"Number of todo items found - "+cursor.getCount());
        try{
           if(cursor.moveToFirst()){
               do{
                   TodoItem todoItem = new TodoItem();
                   todoItem.todoItemId = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ITEM_ID));
                   todoItem.todoItemName = cursor.getString(cursor.getColumnIndex(KEY_TODO_ITEM_NAME));
                   todoItem.todoItemDescription = cursor.getString(cursor.getColumnIndex(KEY_TODO_ITEM_DESCRIPTION));
                   todoItem.todoItemPriority = Enums.TodoItemPriority.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_TODO_ITEM_PRIORITY)));
                   todoItem.todoItemStatus = Enums.TodoItemStatus.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_TODO_ITEM_STATUS)));
                   todoItem.todoItemDueDateLong = cursor.getLong(cursor.getColumnIndex(KEY_TODO_ITEM_DUE_DATE));
                   todoItemList.add(todoItem);
               }while(cursor.moveToNext());
           }
        }catch(Exception e){
            Log.e(TAG,"Error while getting todo items from the table");
        }finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return todoItemList;
    }
}
