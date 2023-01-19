package com.yepyuno.notepad.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yepyuno.notepad.Models.Note;
import com.yepyuno.notepad.NotesClickListener;
import com.yepyuno.notepad.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Note> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Note> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.title.setSelected(true);

        holder.note.setText(list.get(position).getNote());

        holder.dateTime.setText(list.get(position).getDateTime());
        holder.dateTime.setSelected(true);

        if(list.get(position).isPinned()){
            holder.pin.setImageResource(R.drawable.pin_icon);
        }else{
            holder.pin.setImageResource(0);
        }

        int colorCode = getRandomColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.container);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.card_1);
        colorCode.add(R.color.card_2);
        colorCode.add(R.color.card_3);
        colorCode.add(R.color.card_4);
        colorCode.add(R.color.card_5);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());
        return colorCode.get(randomColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Note> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView container;
    TextView title, note, dateTime;
    ImageView pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.cardview_note);
        title = itemView.findViewById(R.id.textView_title);
        note = itemView.findViewById(R.id.textView_note);
        dateTime = itemView.findViewById(R.id.textView_dateTime);
        pin = itemView.findViewById(R.id.imageView_pin);
    }
}
