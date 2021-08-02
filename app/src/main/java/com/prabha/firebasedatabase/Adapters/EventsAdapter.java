package com.prabha.firebasedatabase.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabha.firebasedatabase.Pojo.Event;
import com.prabha.firebasedatabase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by prabhakaranpanjalingam on 02,August,2021
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.Viewholder> {
    List<Event> events = new ArrayList<>();
    private boolean isUser = false;
    private EventsListener eventsListener;


    public EventsAdapter(Context adminActivity, List<Event> events,boolean fromUser) {
        this.events = events;
        this.isUser = fromUser;
    }

    @NonNull
    @Override
    public EventsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        Event event = events.get(position);
        holder.tvEvents.setText(event.getEventName());
        holder.tvArea.setText(event.getArea());
        holder.tvDate.setText(event.getStartDate());
        holder.ltEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsListener.jonEvent(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvEventName)
        TextView tvEvents;
        @BindView(R.id.tvArea)
        TextView tvArea;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.lt_event)
        RelativeLayout ltEvent;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public void setEventsListener(EventsListener eventsListener){
        this.eventsListener = eventsListener;
    }

    public interface  EventsListener{
        void jonEvent(int position);
    }
}
