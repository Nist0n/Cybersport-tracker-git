package ru.mirea.pavlovve.retrofitapp.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

/**
 * Декларативное описание REST API JSONPlaceholder.
 *
 * <p>Retrofit по аннотациям собирает HTTP-запрос, а Gson-конвертер
 * превращает JSON в объекты {@link TodoDto} и обратно.</p>
 */
public interface TodoApi {

    /** GET /todos — список из 200 задач. */
    @GET("todos")
    Call<List<TodoDto>> getTodos();

    /** PATCH /todos/{id} — частичное обновление задачи (меняем только completed). */
    @PATCH("todos/{id}")
    Call<TodoDto> updateTodo(@Path("id") int id, @Body UpdateTodoBody body);
}
