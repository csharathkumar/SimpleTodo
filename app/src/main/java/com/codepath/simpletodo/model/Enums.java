package com.codepath.simpletodo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sharath on 6/25/16.
 */
public class Enums {

    public enum TodoItemPriority{

        HIGH(2),
        MEDIUM(1),
        LOW(0);
        int priorityLevel;

        private static Map<Integer, TodoItemPriority> map = new HashMap<Integer, TodoItemPriority>();

        static {
            for (TodoItemPriority todoItemPriority : TodoItemPriority.values()) {
                map.put(todoItemPriority.priorityLevel, todoItemPriority);
            }
        }
        TodoItemPriority(int i) {
            priorityLevel = i;
        }

        public int getPriorityLevel() {
            return priorityLevel;
        }

        public static TodoItemPriority valueOf(int priorityLevel) {
            return map.get(priorityLevel);
        }

    }

    public enum TodoItemStatus{
        TO_DO(0), DONE(1);
        int todoItemStatusValue;

        private static Map<Integer, TodoItemStatus> map = new HashMap<Integer, TodoItemStatus>();

        static {
            for (TodoItemStatus todoItemStatus : TodoItemStatus.values()) {
                map.put(todoItemStatus.todoItemStatusValue, todoItemStatus);
            }
        }
        TodoItemStatus(int i) {
            todoItemStatusValue = i;
        }

        public int getItemStatus(){
            return todoItemStatusValue;
        }

        public static TodoItemStatus valueOf(int todoItemStatus){
            return map.get(todoItemStatus);
        }
    }
}
