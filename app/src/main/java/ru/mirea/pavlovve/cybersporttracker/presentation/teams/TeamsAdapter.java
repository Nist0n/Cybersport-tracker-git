package ru.mirea.pavlovve.cybersporttracker.presentation.teams;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import ru.mirea.pavlovve.cybersporttracker.R;
import ru.mirea.pavlovve.cybersporttracker.domain.models.Team;


public class TeamsAdapter extends ListAdapter<TeamListItem, TeamsAdapter.TeamViewHolder> {

    public interface Listener {
        void onTeamClick(Team team);

        void onFavoriteClick(Team team);
    }

    private static final DiffUtil.ItemCallback<TeamListItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TeamListItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull TeamListItem oldItem,
                                               @NonNull TeamListItem newItem) {
                    return oldItem.getTeam().getId() == newItem.getTeam().getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull TeamListItem oldItem,
                                                  @NonNull TeamListItem newItem) {
                    return oldItem.equals(newItem);
                }
            };

    private final Listener listener;

    public TeamsAdapter(Listener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {

        private final ImageView logoView;
        private final TextView tagView;
        private final TextView nameView;
        private final TextView metaView;
        private final ImageButton favoriteButton;
        private final Listener listener;

        TeamViewHolder(@NonNull View itemView, Listener listener) {
            super(itemView);
            this.listener = listener;
            logoView = itemView.findViewById(R.id.imageTeamLogo);
            tagView = itemView.findViewById(R.id.textTeamTag);
            nameView = itemView.findViewById(R.id.textTeamName);
            metaView = itemView.findViewById(R.id.textTeamMeta);
            favoriteButton = itemView.findViewById(R.id.buttonFavorite);
        }

        void bind(TeamListItem item) {
            Team team = item.getTeam();
            logoView.setImageResource(item.getLogoResId());
            tagView.setText(team.getTag());
            nameView.setText(team.getName());
            metaView.setText(String.format(Locale.getDefault(), "%s · место %d",
                    team.getCountry(), team.getWorldRank()));

            favoriteButton.setImageResource(item.isFavorite()
                    ? R.drawable.ic_favorite
                    : R.drawable.ic_favorite_border);
            favoriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(
                    itemView.getContext(),
                    item.isFavorite() ? R.color.primary : R.color.text_secondary)));
            favoriteButton.setAlpha(item.isAuthorized() ? 1f : 0.45f);
            favoriteButton.setContentDescription(itemView.getContext().getString(
                    item.isAuthorized()
                            ? (item.isFavorite()
                            ? R.string.team_favorite_remove
                            : R.string.team_favorite_add)
                            : R.string.teams_login_hint));

            favoriteButton.setOnClickListener(v -> listener.onFavoriteClick(team));
            itemView.setOnClickListener(v -> listener.onTeamClick(team));
        }
    }
}
