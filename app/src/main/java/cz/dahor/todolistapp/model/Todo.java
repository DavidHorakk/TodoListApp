package cz.dahor.todolistapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "todo_table")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private Date created;


}
