package com.adria.facebooktest.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adria.facebooktest.Models.Album;
import com.adria.facebooktest.R;

import java.util.ArrayList;

//Adapter to show name of albums in Recyclerview
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

    private ArrayList<Album> albums;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_album);


        }
    }

    public GridAdapter(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return albums.size();
    }


}
