package com.devgary.materialcontextmenu.materialcontextmenu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devgary.materialcontextmenu.R;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    private List<MenuItem> dataset;
    private OnItemClickListener listener;

    public MenuItemAdapter(List<MenuItem> dataset) {
        this.dataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.layoutTouchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null){
                    listener.onItemClick(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.layoutTouchContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.v("MotionEvent", String.valueOf(motionEvent.getActionMasked()));
                switch (motionEvent.getActionMasked()){

                    case MotionEvent.ACTION_DOWN:

                        viewHolder.layoutTouchContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        return true;

                    case MotionEvent.ACTION_OUTSIDE:

                        viewHolder.layoutTouchContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
                        return true;
                }

                return false;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Context context = holder.imageView.getContext();

        MenuItem menuItemItem = dataset.get(position);

        holder.textView.setText(menuItemItem.getString());

        if (menuItemItem.getIcon() == 0){

            holder.imageView.setVisibility(View.GONE);
            holder.imageView.setImageDrawable(null);
        }
        else{

            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, menuItemItem.getIcon()));
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public View layoutTouchContainer;
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutTouchContainer = itemView.findViewById(R.id.layout_touch_container);
            textView = (TextView)itemView.findViewById(R.id.menu_item_textview);
            imageView = (ImageView) itemView.findViewById(R.id.menu_item_imageview);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}