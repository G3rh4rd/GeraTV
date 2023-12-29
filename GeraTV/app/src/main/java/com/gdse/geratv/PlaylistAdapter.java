package com.gdse.geratv;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import rx.Observable;
import rx.subjects.PublishSubject;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {
    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    private List<Stream> mItem;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private int row_index = 0;

    public PlaylistAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
    }

    public void selectRow(int index) {
        row_index = index;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View sView = mInflater.inflate(R.layout.item_playlist, parent, false);
        return new ItemHolder(sView);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder( final ItemHolder holder,int position) {
        int pos = holder.getBindingAdapterPosition();
        final Stream item = mItem.get(pos);
        if (item != null) {
            holder.update(item);
        }
        holder.number.setText((pos + 1) + "");
        if (pos == row_index) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("index", pos);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        if (mItem != null)
            return mItem.size();
        else return 0;
    }

    public void update(List<Stream> _list) {
        this.mItem = _list;
        notifyDataSetChanged();
    }

    public Observable<String> getPositionClicks() {
        return onClickSubject.asObservable();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView number;
        ImageView logo;
        //    TextView url;

        public ItemHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.item_name);
            number = (TextView) view.findViewById(R.id.item_number);
            // url = (TextView) view.findViewById(R.id.item_url);
            //logo = (ImageView) view.findViewById(R.id.item_logo);

        }

        public void update(final Stream item) {
            name.setText(item.getName());
            //  url.setText(item.getUrl());
/*
            Thread thread = new Thread() {
                @Override
                public void run() {
                    URL newurl = null;
                    try {
                        newurl = new URL(item.getLogo());
                        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                logo.setImageBitmap(mIcon_val);
                            }
                        });

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
            */
        }
    }
}
