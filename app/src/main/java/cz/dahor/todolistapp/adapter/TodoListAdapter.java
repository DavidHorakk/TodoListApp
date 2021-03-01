package cz.dahor.todolistapp.adapter;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import cz.dahor.todolistapp.model.Todo;

public class TodoListAdapter extends ListAdapter<Todo, TodoViewHolder> {

    public TodoListAdapter(@NonNull DiffUtil.ItemCallback<Todo> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TodoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo current = getItem(position);
        holder.bind(current.getTitle());
//TODO
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    public static class WordDiff extends DiffUtil.ItemCallback<Todo> {

        @Override
        public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}

