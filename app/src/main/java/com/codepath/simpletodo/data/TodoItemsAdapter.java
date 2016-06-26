package com.codepath.simpletodo.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.TodoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharath on 6/25/16.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    private static class ViewHolder{
        TextView itemIdTV;
        TextView itemNameTV;
    }
    public TodoItemsAdapter(Context context, List<TodoItem> todoItems){
        super(context,0,todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_item_row, parent, false);
            viewHolder.itemIdTV = (TextView) convertView.findViewById(R.id.todoItemIdTV);
            viewHolder.itemNameTV = (TextView) convertView.findViewById(R.id.todoItemNameTV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemIdTV.setText(String.valueOf(todoItem.todoItemId));
        viewHolder.itemNameTV.setText(todoItem.todoItemName);
        // Return the completed view to render on screen
        return convertView;
    }

}
