package com.prabha.firebasedatabase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabha.firebasedatabase.Pojo.User;
import com.prabha.firebasedatabase.R;
import com.prabha.firebasedatabase.UsersDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {
    List<User> users = new ArrayList<>();

    public UserAdapter(UsersDetailsActivity usersDetailsActivity, List<User> userList) {
        this.users = userList;
    }

    @NonNull
    @Override
    public UserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Viewholder holder, int position) {
        User user = users.get(position);
        holder.tvUser.setText(user.getName());
        holder.tvArea.setText(user.getArea());
        holder.tvMobile.setText(user.getMobile());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvUser;
        @BindView(R.id.tvArea)
        TextView tvArea;
        @BindView(R.id.tvMobile)
        TextView tvMobile;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
