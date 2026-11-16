package ru.mirea.pavlovve.retrofitapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import ru.mirea.pavlovve.retrofitapp.R;
import ru.mirea.pavlovve.retrofitapp.data.TodoDto;

/**
 * Список задач: элемент содержит CheckBox, заголовок и идентификатор.
 *
 * <p>Пока PATCH-запрос выполняется, CheckBox блокируется — так пользователь
 * не отправит второй запрос по той же строке.</p>
 */
public class TodosAdapter extends ListAdapter<TodoDto, TodosAdapter.TodoViewHolder> {

    public interface Listener {
        void onCompletedChanged(TodoDto todo, boolean completed);
    }

    private static final DiffUtil.ItemCallback<TodoDto> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TodoDto>() {
                @Override
                public boolean areItemsTheSame(@NonNull TodoDto oldItem, @NonNull TodoDto newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull TodoDto oldItem,
                                                  @NonNull TodoDto newItem) {
                    return oldItem.isCompleted() == newItem.isCompleted()
                            && String.valueOf(oldItem.getTitle())
                            .equals(String.valueOf(newItem.getTitle()));
                }
            };

    private final Listener listener;
    private final Set<Integer> pendingIds = new HashSet<>();

    public TodosAdapter(Listener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public void setPending(int todoId, boolean pending) {
        if (pending) {
            pendingIds.add(todoId);
        } else {
            pendingIds.remove(todoId);
        }
        notifyTodoChanged(todoId);
    }

    public void notifyTodoChanged(int todoId) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).getId() == todoId) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view, listener, pendingIds);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView titleView;
        private final TextView subtitleView;
        private final Listener listener;
        private final Set<Integer> pendingIds;

        TodoViewHolder(@NonNull View itemView, Listener listener, Set<Integer> pendingIds) {
            super(itemView);
            this.listener = listener;
            this.pendingIds = pendingIds;
            checkBox = itemView.findViewById(R.id.checkTodo);
            titleView = itemView.findViewById(R.id.textTodoTitle);
            subtitleView = itemView.findViewById(R.id.textTodoSubtitle);
        }

        void bind(TodoDto todo) {
            // Снимаем слушатель, иначе программный setChecked отправит PATCH.
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(todo.isCompleted());

            boolean pending = pendingIds.contains(todo.getId());
            checkBox.setEnabled(!pending);
            checkBox.setAlpha(pending ? 0.4f : 1f);

            titleView.setText(todo.getTitle());
            subtitleView.setText(itemView.getContext().getString(
                    R.string.todo_subtitle, todo.getId(), todo.getUserId()));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    listener.onCompletedChanged(todo, isChecked));
        }
    }
}
