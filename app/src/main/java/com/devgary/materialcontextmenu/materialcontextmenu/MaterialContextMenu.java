package com.devgary.materialcontextmenu.materialcontextmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.devgary.materialcontextmenu.R;
import com.devgary.materialcontextmenu.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by froger_mcs on 15.12.14.
 */
public class MaterialContextMenu extends LinearLayout {

    public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private boolean isContextMenuDismissing= false;
    private boolean isContextMenuShowing= false;

    private RecyclerView recyclerView;
    private MenuItemAdapter adapter;

    private OnFeedContextMenuItemClickListener onItemClickListener;

    public MaterialContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_material_context_menu, this, true);
        setBackgroundResource(R.drawable.shadow_round2_blur10_color_bbbbbb_ninepatch);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setMinimumWidth((int) AndroidUtils.convertDpToPixel(221));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        List<MenuItem> dataset = new ArrayList<>();

        dataset.add(new MenuItem("Refresh", R.drawable.ic_refresh_white_24dp));
        dataset.add(new MenuItem("Subscribe", R.drawable.ic_rss_feed_white_24dp));
        dataset.add(new MenuItem("Search", R.drawable.ic_search_white_24dp));

        adapter = new MenuItemAdapter(dataset);

        adapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (onItemClickListener != null) onItemClickListener.onItemClicked(position);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new RecyclerViewTopAndBottomSpacingItemDecoration((int) AndroidUtils.convertDpToPixel(8), (int) AndroidUtils.convertDpToPixel(8)));
        recyclerView.scrollToPosition(0);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.v("MainActivity", "(" + motionEvent.getRawX() + ")");
                return false;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }



//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Rect viewRect = new Rect();
//
//        getGlobalVisibleRect(viewRect);
//
//        if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
//
//            hideContextMenu();
//        }
//
//        return true;
//    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(MaterialContextMenu.this);
    }

    public interface OnFeedContextMenuItemClickListener {

        void onItemClicked(int position);
    }

    public void showContextMenuFromView(final View openingView, MaterialContextMenu.OnFeedContextMenuItemClickListener listener) {

        if (!isContextMenuShowing) {
            isContextMenuShowing = true;

//            addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
//                @Override
//                public void onViewAttachedToWindow(View v) {
//
//                }
//
//                @Override
//                public void onViewDetachedFromWindow(View v) {
//
//                }
//            });
//
            setOnFeedMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(this);

            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        View rootView = getRootView();
        int rootViewHeight = rootView.getHeight();
        int contextMenuViewHeight = (int) (0.9 * rootViewHeight / 2);
//        layoutParams.height = contextMenuViewHeight;
        setLayoutParams(layoutParams);

        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = (int) AndroidUtils.convertDpToPixel(0);

        int openingViewCenterY = openingViewLocation[1] + openingView.getHeight() / 2;
        int rootViewCenterY = rootViewHeight / 2;

        setTranslationX((float) (openingViewLocation[0] - getWidth() + openingView.getWidth() * 1.5));

        setTranslationX(getTranslationX() + 27);

        setPivotX(getWidth() / 2);

        if (openingViewCenterY > rootViewCenterY) {

            // Open Upwards
            setTranslationY(openingViewLocation[1] - contextMenuViewHeight + openingView.getHeight());
            setPivotY(getLayoutParams().height);

        }
        else {

            // Open Downwards
            setTranslationY(openingViewLocation[1] + additionalBottomMargin);
            setPivotY(0);
        }
    }

    private void performShowAnimation() {
        setScaleX(0.1f);
        setScaleY(0.1f);
        animate()
                       .scaleX(1f).scaleY(1f)
                       .setDuration(150)
                       .setInterpolator(new AccelerateInterpolator())
                       .setListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationEnd(Animator animation) {
                               isContextMenuShowing = false;
                           }
                       });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnim();
        }
    }

    private void performDismissAnim(){

        performFadeDismissAnim();
    }

    private void performFadeDismissAnim(){

        animate()
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        dismiss();
                        isContextMenuDismissing = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    private void performCollapseDismissAnim() {
        setPivotX(getWidth() / 2);
        setPivotY(getHeight());
        animate()
                       .scaleX(0.1f).scaleY(0.1f)
                       .setDuration(150)
                       .setInterpolator(new AccelerateInterpolator())
                       .setStartDelay(100)
                       .setListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationEnd(Animator animation) {

                               dismiss();
                               isContextMenuDismissing = false;
                           }
                       });
    }

    public void listenToScrollEvents(RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                hideContextMenu();
                setTranslationY(getTranslationY() - dy);
            }
        });
    }
}