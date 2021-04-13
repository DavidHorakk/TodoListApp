package cz.dahor.todolistapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "todo_table")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String created;
    private String finished;
    private String longitude;
    private String latitude;
    private int priority;



    public Todo() {
    }

    public Todo(String title) {
        this.title = title;
    }

    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Todo(String title, String description, String created, String finished, int priority) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.finished = finished;
        this.priority = priority;
    }

    public Todo(String title, String description, String created, String finished, String longitude, String latitude, int priority) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.finished = finished;
        this.longitude = longitude;
        this.latitude = latitude;
        this.priority = priority;
    }

    //    public Todo(String title, String description, Date created) {
//        this.title = title;
//        this.description = description;
//        this.created = created;
//    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
