package com.example.findmy.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmy.R;
import com.example.findmy.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public Button viewDetailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.friendName);
            viewDetailsButton = (Button) itemView.findViewById(R.id.viewFriendButton);
        }
    }

    private List<User> friends;
    public FriendsAdapter(List<User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View friendView = layoutInflater.inflate(R.layout.friends_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(friendView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, int position) {
        User friend = friends.get(position);

        TextView textView = holder.nameText;
        textView.setText(friend.getName());

        // TODO: set button functionality
        Button button = holder.viewDetailsButton;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
