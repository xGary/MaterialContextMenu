package com.devgary.materialcontextmenu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.devgary.materialcontextmenu.materialcontextmenu.MaterialContextMenu;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView toolbarOverflow;
    private CoordinatorLayout coordinatorLayout;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        initToolbar();

        initRecyclerView();
    }

//    private void showPopup(final Activity context, Point p) {
//
//        PopupWindow changeSortPopUp;
//
//        // Inflate the popup_layout.xml
////        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.llSortChangePopup);
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = layoutInflater.inflate(R.layout.layout_material_context_menu, null, false);
//
//        RecyclerView recyclerView;
//        MenuItemAdapter adapter;
//
//        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
//
//        List<MenuItem> dataset = new ArrayList<>();
//
//        dataset.add(new MenuItem("Settings"));
//        dataset.add(new MenuItem("Settings"));
//        dataset.add(new MenuItem("Settings"));
//        dataset.add(new MenuItem("Settings"));
//        dataset.add(new MenuItem("Settings"));
//
//        adapter = new MenuItemAdapter(dataset);
//
////        adapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
////            @Override
////            public void onItemClick(int position) {
////
////                if (onItemClickListener != null) onItemClickListener.onItemClicked(position);
////            }
////        });
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        recyclerView.addItemDecoration(new RecyclerViewTopAndBottomSpacingItemDecoration((int) AndroidUtils.convertDpToPixel(8), (int) AndroidUtils.convertDpToPixel(8)));
//        recyclerView.scrollToPosition(0);
//
//        // Creating the PopupWindow
//        changeSortPopUp = new PopupWindow(context);
//        changeSortPopUp.setContentView(layout);
//        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        changeSortPopUp.setFocusable(true);
//
//        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
//        int OFFSET_X = -20;
//        int OFFSET_Y = 95;
//
//        // Clear the default translucent background
//        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
//
//        // Displaying the popup at the specified location, + offsets.
//        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
//    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {

        adapter = new RecyclerViewAdapter();
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View v, int position) {

                MaterialContextMenu materialContextMenu = new MaterialContextMenu(MainActivity.this);
                materialContextMenu.listenToScrollEvents(recyclerView);

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
