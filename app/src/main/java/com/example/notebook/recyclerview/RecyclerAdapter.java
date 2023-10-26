package com.example.notebook.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.ViewModel.Note_Viewmodel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {


    private List<Note> list = new ArrayList<>();
    private Context context;
    private RecyclerListener listener;
    private Note_Viewmodel viewmodel;


    public List<Note> getList() {
        return list;
    }

    public void setList(List<Note> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public RecyclerAdapter(Context context, RecyclerListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public RecyclerAdapter(List<Note> list, Context context, RecyclerListener listener, Note_Viewmodel viewmodel) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.viewmodel = viewmodel;
    }

    public RecyclerAdapter(List<Note> list, Context context) {
        this.list = list;
        this.context = context;

    }

    public Note getNotePosition(int position) {
        return list.get(position);
    }

    public void addNotePosition(int position, Note note) {
        list.add(position, note);

    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.cardview_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.Set_Data_in_Holder(list.get(position));
        holder.CardView_Recyclerview_Listener();
        holder.Favourite_Recyclerview_Listener();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView titel, description, date;
        private ImageView clor, record, Favourites;

        private CardView cardView;
        private Note note;


        public Holder(@NonNull View itemView) {
            super(itemView);
            titel = itemView.findViewById(R.id.Card_Title);
            description = itemView.findViewById(R.id.Card_Description);
            date = itemView.findViewById(R.id.Card_Date);
            Favourites = itemView.findViewById(R.id.Card_Favourite);
            cardView = itemView.findViewById(R.id.Card_View);
            record = itemView.findViewById(R.id.Card_Record);
            clor = itemView.findViewById(R.id.Card_Color);
        }

        private void CardView_Recyclerview_Listener() {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ClickListener(view, note);
                }
            });

        }

        private void Favourite_Recyclerview_Listener() {
            Favourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (note.isFavourites()) {
                        viewmodel.update_Favourite(note.getId(), false);
                        Favourites.setImageResource(R.drawable.ic_favorite_40);

                    } else {
                        viewmodel.update_Favourite(note.getId(), true);
                        Favourites.setImageResource(R.drawable.ic_favorite_true_40);
                    }
                }
            });

        }


        private void Set_Data_in_Holder(Note note1) {
            note = note1;
            titel.setText(note.getTitle());
            description.setText(note.getDescription());
            clor.setBackgroundColor(note.getColor());
            date.setText(note.getDate());


            if (note1.getRecord().equals(""))
                record.setImageResource(0);
            else {
                record.setImageResource(R.drawable.ic_voice_dark_40);

            }
            if (note1.isFavourites())
                Favourites.setImageResource(R.drawable.ic_favorite_true_40);
            else
                Favourites.setImageResource(R.drawable.ic_favorite_40);


        }
    }


}
