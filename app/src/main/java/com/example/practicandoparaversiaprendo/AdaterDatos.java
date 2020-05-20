package com.example.practicandoparaversiaprendo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdaterDatos extends RecyclerView.Adapter<AdaterDatos.ViewHolderDatos> implements View.OnClickListener  {
    public ArrayList<Musica> Musicas;
    private View.OnClickListener listener;
    public AdaterDatos(ArrayList<Musica> musicas) {
        this.Musicas= musicas;
    }
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }

    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.name.setText("Nombre:   "+ Musicas.get(position).getName());
        holder.duration.setText(("Duracion:   "+Musicas.get(position).getDuration()));
        holder.artist.setText(("Artista:   "+Musicas.get(position).getArtist()));

    }

    @Override
    public int getItemCount() {
        return Musicas.size();

    }
    public  void setOnClickListener(View.OnClickListener listener){
        this.listener= listener;

    }



    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView name;
        TextView duration;
        TextView artist;
        public ViewHolderDatos(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            duration= itemView.findViewById(R.id.duration);
            artist = itemView.findViewById(R.id.artist);


        }

    }
}
