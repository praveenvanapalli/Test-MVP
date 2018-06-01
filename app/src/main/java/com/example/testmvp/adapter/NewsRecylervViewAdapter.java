package com.example.testmvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testmvp.R;
import com.example.testmvp.generic.RoundedImageView;
import com.example.testmvp.model.MediaMetadatum;
import com.example.testmvp.model.Medium;
import com.example.testmvp.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Praveen
 */

public class NewsRecylervViewAdapter  extends RecyclerView.Adapter<NewsRecylervViewAdapter.MovieViewHolder> { //hi

    private List<Result> getDataReponseList;
    private Context _context;
    private onItemClickListner listner;

    public void addData(List<Result> newsList) {
        getDataReponseList=newsList;
        notifyDataSetChanged();
    }


    public  class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvBYAutoh;
        TextView tvDate;
        RoundedImageView avatar;
        RelativeLayout main_row_lay;

        public MovieViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
            tvBYAutoh = (TextView) v.findViewById(R.id.tvBYAutoh);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            avatar = (RoundedImageView) v.findViewById(R.id.ivImg);
            main_row_lay = (RelativeLayout) v.findViewById(R.id.main_row_lay);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClicked(getAdapterPosition(),getDataReponseList);

                }
            });
        }
    }

    public NewsRecylervViewAdapter(Context context) {
        this._context = context;
    }

    @Override
    public NewsRecylervViewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_row_layout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Result _Result=getDataReponseList.get(position);

        holder.tvTitle.setText(_Result.getTitle().toString() != null ? _Result.getTitle().toString() : "");
        holder.tvSubtitle.setText(_Result.getAbstract().toString() != null ? _Result.getAbstract().toString() : "");
        holder.tvBYAutoh.setText(_Result.getByline().toString() != null ? _Result.getByline().toString() : "");
        holder.tvDate.setText(_Result.getPublishedDate().toString() != null ? _Result.getPublishedDate().toString() : "");


        List<Medium> mediumList = _Result.getMedia();
        for (int i=0; i<mediumList.size(); i++){
            List<MediaMetadatum> mediaMetadataList = mediumList.get(i).getMediaMetadata();
            Picasso.get().load( mediaMetadataList.get(1).getUrl()).into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return getDataReponseList == null ? 0 : getDataReponseList.size();
    }

    public interface onItemClickListner{
        void onItemClicked(int position, List<Result> getDataReponseList);
    }

    public void onSetonItemClickListner(onItemClickListner clickListner){
        listner=clickListner;
    }


}
