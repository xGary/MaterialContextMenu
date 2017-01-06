package com.devgary.materialcontextmenu.materialcontextmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.devgary.materialcontextmenu.R;

/**
 * Created by Gary on 2017-01-05.
 */

public class MaterialContextMenuWindow extends FrameLayout {

    private MaterialContextMenu materialContextMenu;

    public MaterialContextMenuWindow(Context context) {
        super(context);

        init();
    }

    public MaterialContextMenuWindow(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MaterialContextMenuWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

        LayoutInflater.from(getContext()).inflate(R.layout.layout_material_context_menu_window, this, true);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (materialContextMenu != null){

                    materialContextMenu.hideContextMenu();
                    setVisibility(GONE);
                }
            }
        });
    }

    public void showContextMenuFromView(View v, MaterialContextMenu.OnFeedContextMenuItemClickListener listener){

        ((ViewGroup) v.getRootView().findViewById(android.R.id.content)).addView(this);

        materialContextMenu = new MaterialContextMenu(v.getContext());

        materialContextMenu.showContextMenuFromView(v, listener);
    }
}
