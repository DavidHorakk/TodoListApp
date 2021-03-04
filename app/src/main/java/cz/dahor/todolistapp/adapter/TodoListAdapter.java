package cz.dahor.todolistapp.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
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



//    static class TodoDiff extends DiffUtil.ItemCallback<Todo> {
//
//        @Override
//        public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
//            return oldItem == newItem;
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
//            return oldItem.getTitle().equals(newItem.getTitle());
//        }
//    }

class TodoViewHolder extends RecyclerView.ViewHolder{
    private TextView textViewTitle, textViewDescription, textViewPriority;

    public TodoViewHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription=itemView.findViewById(R.id.textViewDescription);
//        wordItemView = itemView.findViewById(R.id.textView);

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

