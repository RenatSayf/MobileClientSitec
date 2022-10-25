package com.renatsayf.mobileclientsitec.ui.users;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.renatsayf.mobileclientsitec.databinding.ItemUserBinding;
import com.renatsayf.mobileclientsitec.model.users.User;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends ListAdapter<User, UsersAdapter.ViewHolder>
{

    protected UsersAdapter(@NonNull DiffUtil.ItemCallback<User> diffCallback)
    {
        super(diffCallback);
    }

    protected UsersAdapter(@NonNull AsyncDifferConfig<User> config)
    {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final com.renatsayf.mobileclientsitec.databinding.ItemUserBinding binding;

        public ViewHolder(@NonNull ItemUserBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.tvUser.setText(user.user);
            binding.tvUid.setText(user.uid);
            binding.tvLanguage.setText(user.language);
        }
    }

    public static DiffUtil.ItemCallback<User> diffCallback = new DiffUtil.ItemCallback<User>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem)
        {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem)
        {
            return Objects.equals(oldItem, newItem);
        }
    };
}
