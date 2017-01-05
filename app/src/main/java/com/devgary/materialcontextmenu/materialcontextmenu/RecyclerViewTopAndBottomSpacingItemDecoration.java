package com.devgary.materialcontextmenu.materialcontextmenu;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gary on 4/5/2016.
 *
 * Adds spacing to top and bottom of RecyclerView (top spacing to first item, bottom spacing to last item)
 */
public class RecyclerViewTopAndBottomSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int topHeight;
    private final int bottomHeight;

    public RecyclerViewTopAndBottomSpacingItemDecoration(int topHeight, int bottomHeight) {
        this.topHeight = topHeight;
        this.bottomHeight = bottomHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) == 0) outRect.top = topHeight;

        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) outRect.bottom = bottomHeight;

    }
}
