package ru.mirea.pavlovve.retrofitapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.mirea.pavlovve.retrofitapp.R;

/**
 * Горизонтальная галерея изображений.
 *
 * <p>Picasso получает URL, размер, режим масштабирования и поворот.
 * Все параметры меняются из UI и применяются при следующей привязке.</p>
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private static final String IMAGE_URL_TEMPLATE =
            "https://picsum.photos/seed/practice5_%d/300/300";

    private final List<Integer> seeds = new ArrayList<>();
    private final Random random = new Random();

    private int sizeDp = 120;
    private boolean crop = true;
    private int rotationDegrees;

    public ImagesAdapter(int count) {
        for (int i = 0; i < count; i++) {
            seeds.add(100 + i);
        }
    }

    public void setParams(int sizeDp, boolean crop, int rotationDegrees) {
        this.sizeDp = sizeDp;
        this.crop = crop;
        this.rotationDegrees = rotationDegrees;
        notifyDataSetChanged();
    }

    /** Новые случайные изображения — видно, что запросы действительно уходят в сеть. */
    public void reload() {
        seeds.clear();
        for (int i = 0; i < 8; i++) {
            seeds.add(random.nextInt(10_000));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(seeds.get(position), sizeDp, crop, rotationDegrees);
    }

    @Override
    public int getItemCount() {
        return seeds.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePhoto);
        }

        void bind(int seed, int sizeDp, boolean crop, int rotationDegrees) {
            int sizePx = Math.round(sizeDp
                    * itemView.getResources().getDisplayMetrics().density);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            if (params.width != sizePx || params.height != sizePx) {
                params.width = sizePx;
                params.height = sizePx;
                imageView.setLayoutParams(params);
            }

            String url = String.format(IMAGE_URL_TEMPLATE, seed);

            RequestCreator request = Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.bg_image_placeholder)
                    .error(R.drawable.bg_image_placeholder)
                    .resize(sizePx, sizePx);

            if (crop) {
                request.centerCrop();
            } else {
                request.centerInside();
            }
            if (rotationDegrees != 0) {
                request.rotate(rotationDegrees);
            }
            request.into(imageView);
        }
    }
}
