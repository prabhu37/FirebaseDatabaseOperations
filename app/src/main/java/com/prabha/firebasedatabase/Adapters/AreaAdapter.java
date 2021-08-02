package com.prabha.firebasedatabase.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabha.firebasedatabase.Pojo.LocationSelectPojo;
import com.prabha.firebasedatabase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.Viewholder> {
    List<LocationSelectPojo> areas = new ArrayList<>();
    private AreaSelectedListener areaSelectedListener;

    public AreaAdapter(Context adminActivity, List<LocationSelectPojo> areas) {
        this.areas = areas;
    }

    @NonNull
    @Override
    public AreaAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
       LocationSelectPojo  locationSelectPojo = areas.get(position);
        holder.tvArea.setText(locationSelectPojo.getLocation());

        if(locationSelectPojo.isSelected()){
            holder.tvArea.setBackgroundResource(R.drawable.boc_bg_orange_stroke);
        }else {
            holder.tvArea.setBackgroundResource(R.drawable.box_bg);
        }

        holder.ltParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaSelectedListener.selectItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_location)
        TextView tvArea;
        @BindView(R.id.lt_parent)
        LinearLayout ltParent;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void  setAreaSelectListener(AreaSelectedListener areaSelectedListener)
    {
        this.areaSelectedListener = areaSelectedListener;

    }
    public interface AreaSelectedListener{
        void selectItem(int position);
    }
}
