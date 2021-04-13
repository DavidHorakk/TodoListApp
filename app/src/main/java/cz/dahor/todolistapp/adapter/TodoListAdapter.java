package cz.dahor.todolistapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cz.dahor.todolistapp.R;
import cz.dahor.todolistapp.model.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private List<Todo> todos = new ArrayList<>();
    private OnItemClickListener listener;


    public TodoListAdapter() {
    }



    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
//        Todo current = getItem(position);
//        holder.bind(current.getTitle());
        Todo currentTodo = todos.get(position);
        holder.textViewTitle.setText(currentTodo.getTitle());
        holder.textViewDescription.setText(currentTodo.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentTodo.getPriority()));
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void setTodos(List<Todo> todos){
        this.todos=todos;
        notifyDataSetChanged();
    }

    public Todo getTodoAt(int position){
        return todos.get(position);
    }

class TodoViewHolder extends RecyclerView.ViewHolder{
    private TextView textViewTitle, textViewDescription, textViewPriority;

    public TodoViewHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription=itemView.findViewById(R.id.textViewDescription);
        textViewPriority=itemView.findViewById(R.id.textViewPriority);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(listener!= null && position!= RecyclerView.NO_POSITION){
                    listener.onItemClick(todos.get(position));
                }
            }
        });
    }
}

    public void setOnItemClickedListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }







}

