package com.devgary.materialcontextmenu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.devgary.materialcontextmenu.materialcontextmenu.CustomListViewAdapter;
import com.devgary.materialcontextmenu.materialcontextmenu.MaterialContextMenu;
import com.devgary.materialcontextmenu.materialcontextmenu.MaterialContextMenuWindow;
import com.devgary.materialcontextmenu.materialcontextmenu.MenuItem;
import com.devgary.materialcontextmenu.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View toolbarOverflowTouchContainer;
    private View toolbarTopRightAnchor;
    private ImageView toolbarOverflow;
    private CoordinatorLayout coordinatorLayout;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<MenuItem> dataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        toolbarOverflowTouchContainer = findViewById(R.id.toolbar_overflow_touch_container);
        toolbarTopRightAnchor = findViewById(R.id.toolbar_top_right_anchor);


        dataset.add(new MenuItem("Refresh", R.drawable.ic_refresh_white_24dp));
        dataset.add(new MenuItem("Subscribe", R.drawable.ic_rss_feed_white_24dp));
        dataset.add(new MenuItem("Search", R.drawable.ic_search_white_24dp));


        initToolbar();

        initRecyclerView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        toolbarOverflowTouchContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){

                    showPopupWindow();
                }

                return false;
            }
        });

        toolbarOverflowTouchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupWindow();
            }
        });
    }

    boolean isPopupWindowShown = false;

    private void showPopupWindow() {

        if (isPopupWindowShown) return;

        isPopupWindowShown = true;

        final ListPopupWindow listPopupWindow = new ListPopupWindow(MainActivity.this);

        final CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(MainActivity.this, R.layout.layout_menu_item, dataset);
        listPopupWindow.setAdapter(customListViewAdapter);

        listPopupWindow.setAnchorView(toolbarTopRightAnchor);

        listPopupWindow.getVerticalOffset();
        listPopupWindow.getHorizontalOffset();

        listPopupWindow.setWidth((int) AndroidUtils.convertDpToPixel(221));

        listPopupWindow.setModal(true);

        View.OnTouchListener dragToOpenTouchListener = listPopupWindow.createDragToOpenListener(toolbarOverflowTouchContainer);

        toolbarOverflowTouchContainer.setOnTouchListener(dragToOpenTouchListener);

        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                isPopupWindowShown = false;
            }
        });

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(recyclerView, "Item Clicked: " + String.valueOf(i), Snackbar.LENGTH_LONG).show();

                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.show();
    }

    private void initRecyclerView() {

        adapter = new RecyclerViewAdapter();
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View v, int position) {

                MaterialContextMenuWindow materialContextMenu = new MaterialContextMenuWindow(MainActivity.this);

                materialContextMenu.showContextMenuFromView(v, new MaterialContextMenu.OnFeedContextMenuItemClickListener() {
                    @Override
                    public void onItemClicked(int position) {

                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
