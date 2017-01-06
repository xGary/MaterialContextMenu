package com.devgary.materialcontextmenu.materialcontextmenu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devgary.materialcontextmenu.R;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<MenuItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId, //resourceId=your layout
                                 List<MenuItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        MenuItem menuItemItem = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);

            holder = new ViewHolder();

            holder.textView = (TextView) convertView.findViewById(R.id.menu_item_textview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.menu_item_imageview);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (menuItemItem != null) {

            holder.textView.setText(menuItemItem.getString());

            if (menuItemItem.getIcon() == 0) {

                holder.imageView.setVisibility(View.GONE);
                holder.imageView.setImageDrawable(null);
            }
            else {

                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, menuItemItem.getIcon()));
            }
        }

        return convertView;
    }

} 