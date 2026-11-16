package ru.mirea.pavlovve.retrofitapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.pavlovve.retrofitapp.data.ApiClient;
import ru.mirea.pavlovve.retrofitapp.data.TodoApi;
import ru.mirea.pavlovve.retrofitapp.data.TodoDto;
import ru.mirea.pavlovve.retrofitapp.data.UpdateTodoBody;
import ru.mirea.pavlovve.retrofitapp.ui.ImagesAdapter;
import ru.mirea.pavlovve.retrofitapp.ui.TodosAdapter;

/**
 * Практическая работа №5: Retrofit + Picasso.
 *
 * <ul>
 *   <li>GET /todos — список задач (асинхронный вызов {@code enqueue()});</li>
 *   <li>PATCH /todos/{id} — сохранение состояния CheckBox;</li>
 *   <li>Picasso — галерея изображений с настраиваемым размером,
 *       обрезкой и поворотом.</li>
 * </ul>
 */
public class MainActivity extends AppCompatActivity implements TodosAdapter.Listener {

    private TodoApi api;

    private RecyclerView todosRecycler;
    private ProgressBar todosProgress;
    private View todosErrorGroup;
    private TextView todosErrorText;

    private TodosAdapter todosAdapter;
    private ImagesAdapter imagesAdapter;

    private TextView sizeValueView;
    private TextView rotationValueView;
    private int imageSizeDp = 120;
    private boolean imageCrop = true;
    private int imageRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ApiClient.api();

        setupTodoList();
        setupImageGallery();
        loadTodos();
    }

    // ------------------------------------------------------------------ задачи

    private void setupTodoList() {
        todosRecycler = findViewById(R.id.recyclerTodos);
        todosProgress = findViewById(R.id.progressTodos);
        todosErrorGroup = findViewById(R.id.todosErrorGroup);
        todosErrorText = findViewById(R.id.textTodosError);

        todosAdapter = new TodosAdapter(this);
        todosRecycler.setLayoutManager(new LinearLayoutManager(this));
        todosRecycler.setAdapter(todosAdapter);

        MaterialButton retry = findViewById(R.id.buttonTodosRetry);
        retry.setOnClickListener(v -> loadTodos());
    }

    /** GET /todos через асинхронный enqueue(): колбэк приходит в главный поток. */
    private void loadTodos() {
        todosProgress.setVisibility(View.VISIBLE);
        todosErrorGroup.setVisibility(View.GONE);
        todosRecycler.setVisibility(View.INVISIBLE);

        api.getTodos().enqueue(new Callback<List<TodoDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<TodoDto>> call,
                                   @NonNull Response<List<TodoDto>> response) {
                todosProgress.setVisibility(View.GONE);

                List<TodoDto> body = response.body();
                if (response.isSuccessful() && body != null) {
                    todosRecycler.setVisibility(View.VISIBLE);
                    todosAdapter.submitList(body);
                    showMessage(getString(R.string.todos_loaded, body.size()));
                } else {
                    showTodosError(getString(R.string.http_error, response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TodoDto>> call,
                                  @NonNull Throwable throwable) {
                todosProgress.setVisibility(View.GONE);
                showTodosError(ApiClient.describe(throwable));
            }
        });
    }

    private void showTodosError(String message) {
        todosErrorText.setText(message);
        todosErrorGroup.setVisibility(View.VISIBLE);
        todosRecycler.setVisibility(View.INVISIBLE);
    }

    /**
     * PATCH /todos/{id}: отправляем только поле completed.
     * При ошибке CheckBox возвращается в исходное состояние.
     */
    @Override
    public void onCompletedChanged(TodoDto todo, boolean completed) {
        final int id = todo.getId();
        todosAdapter.setPending(id, true);

        api.updateTodo(id, new UpdateTodoBody(completed))
                .enqueue(new Callback<TodoDto>() {
                    @Override
                    public void onResponse(@NonNull Call<TodoDto> call,
                                           @NonNull Response<TodoDto> response) {
                        todosAdapter.setPending(id, false);
                        if (response.isSuccessful()) {
                            TodoDto updated = response.body();
                            todo.setCompleted(updated != null
                                    ? updated.isCompleted()
                                    : completed);
                            todosAdapter.notifyTodoChanged(id);
                            showMessage(getString(R.string.todo_saved, id));
                        } else {
                            // Состояние в todo не меняли — CheckBox вернётся сам.
                            todosAdapter.notifyTodoChanged(id);
                            showMessage(getString(R.string.todo_save_failed,
                                    getString(R.string.http_error, response.code())));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TodoDto> call,
                                          @NonNull Throwable throwable) {
                        todosAdapter.setPending(id, false);
                        showMessage(getString(R.string.todo_save_failed,
                                ApiClient.describe(throwable)));
                    }
                });
    }

    // -------------------------------------------------------------- изображения

    private void setupImageGallery() {
        RecyclerView imagesRecycler = findViewById(R.id.recyclerImages);
        imagesAdapter = new ImagesAdapter(8);
        imagesRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesRecycler.setAdapter(imagesAdapter);

        sizeValueView = findViewById(R.id.textSizeValue);
        rotationValueView = findViewById(R.id.textRotationValue);
        MaterialSwitch cropSwitch = findViewById(R.id.switchCrop);

        SeekBar sizeSeek = findViewById(R.id.seekSize);
        sizeSeek.setOnSeekBarChangeListener(new SimpleSeekListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageSizeDp = progress;
                sizeValueView.setText(getString(R.string.size_value, progress));
                applyImageParams();
            }
        });

        SeekBar rotationSeek = findViewById(R.id.seekRotation);
        rotationSeek.setOnSeekBarChangeListener(new SimpleSeekListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageRotation = Math.round(progress / 15f) * 15;
                rotationValueView.setText(getString(R.string.rotation_value, imageRotation));
                applyImageParams();
            }
        });

        cropSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            imageCrop = isChecked;
            applyImageParams();
        });

        MaterialButton reload = findViewById(R.id.buttonReloadImages);
        reload.setOnClickListener(v -> {
            imagesAdapter.reload();
            showMessage(getString(R.string.images_reloaded));
        });
    }

    private void applyImageParams() {
        imagesAdapter.setParams(imageSizeDp, imageCrop, imageRotation);
    }

    // --------------------------------------------------------------------- утилиты

    private void showMessage(String text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
    }

    /** Пустая реализация onStartTrackingTouch/onStopTrackingTouch, чтобы не дублировать код. */
    private abstract static class SimpleSeekListener
            implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
