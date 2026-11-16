package ru.mirea.pavlovve.retrofitapp.data;

/**
 * Тело PATCH-запроса. Отправляется только изменившееся поле.
 */
public class UpdateTodoBody {

    private final boolean completed;

    public UpdateTodoBody(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
