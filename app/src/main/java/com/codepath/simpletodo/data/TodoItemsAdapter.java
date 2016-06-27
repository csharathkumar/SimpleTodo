package com.codepath.simpletodo.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.Enums;
import com.codepath.simpletodo.model.TodoItem;

import java.util.List;

/**
 * Created by Sharath on 6/25/16.
 */
public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    Context mContext;
    private static class ViewHolder{
        TextView itemDueDate;
        TextView itemNameTV;
        CheckBox itemDoneCB;
        TextView itemPriorityTV;
    }
    public TodoItemsAdapter(Context context, List<TodoItem> todoItems){
        super(context,0,todoItems);
        mContext = context;
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
            viewHolder.itemDueDate = (TextView) convertView.findViewById(R.id.todoItemDueDate);
            viewHolder.itemNameTV = (TextView) convertView.findViewById(R.id.todoItemNameTV);
            viewHolder.itemDoneCB = (CheckBox) convertView.findViewById(R.id.itemDoneCB) ;
            viewHolder.itemPriorityTV = (TextView) convertView.findViewById(R.id.priorityTV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemDueDate.setText(String.valueOf(todoItem.todoItemDueDateLong));
        viewHolder.itemNameTV.setText(todoItem.todoItemName);
        viewHolder.itemPriorityTV.setText(todoItem.todoItemPriority.toString());
        if(todoItem.todoItemPriority == Enums.TodoItemPriority.HIGH){
            viewHolder.itemPriorityTV.setTextColor(mContext.getResources().getColor(R.color.priority_high_color));
        }else if(todoItem.todoItemPriority == Enums.TodoItemPriority.MEDIUM){
            viewHolder.itemPriorityTV.setTextColor(mContext.getResources().getColor(R.color.priority_medium_color));
        }else{
            viewHolder.itemPriorityTV.setTextColor(mContext.getResources().getColor(R.color.priority_low_color));
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
