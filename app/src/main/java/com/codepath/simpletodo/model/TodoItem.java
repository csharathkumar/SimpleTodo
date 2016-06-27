package com.codepath.simpletodo.model;

import java.io.Serializable;

/**
 * Created by Sharath on 6/25/16.
 */
public class TodoItem implements Serializable{
    public int todoItemId;
    public String todoItemName;
    public String todoItemDescription;
    public long todoItemDueDateLong;
    public Enums.TodoItemStatus todoItemStatus = Enums.TodoItemStatus.TO_DO;
    public Enums.TodoItemPriority todoItemPriority = Enums.TodoItemPriority.LOW;

}
