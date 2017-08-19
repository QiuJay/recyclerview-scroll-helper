package jay.android.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jay on 2017/8/19 上午10:30
 * 一个监听 RecyclerView 滑动方向、滑动状态及最后一个 Item 可见等监听操作
 *
 * @author jay
 */
public class RecyclerViewScrollHelper {
    private final RecyclerView mView;
    private OnScrollStateListener mOnScrollStateListener;
    private OnLastItemVisibleListener mOnLastItemVisibleListener;
    private boolean isListenerLastItemVisible = false;

    public RecyclerViewScrollHelper(RecyclerView view) {
        mView = view;
        init();
    }

    private void init() {
        mView.addOnScrollListener(mOnScrollListener);
    }

    /**
     * 设置滑动状态监听器
     *
     * @param listener {@link OnScrollStateListener}
     */
    public void setOnScrollStateListener(OnScrollStateListener listener) {
        mOnScrollStateListener = listener;
    }

    /**
     * 设置最后一个 item 视图可见监听器
     *
     * @param listener {@link OnLastItemVisibleListener}
     */
    public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        mOnLastItemVisibleListener = listener;
    }

    /**
     * 取消监听最后一个 item 视图可见，但是，不会移除监听器，
     * 此方法的用处主要防止 {@link OnLastItemVisibleListener#onLastItemVisible(View)} 被多次调用
     */
    public void cancelOnLastItemVisibleListener() {
        isListenerLastItemVisible = false;
    }

    /**
     * 最后 item 视图可见监听器
     */
    public interface OnLastItemVisibleListener {
        /**
         * 当最后一个 item 视图可见时
         *
         * @param view 最后一个视图 View
         */
        void onLastItemVisible(View view);
    }

    /**
     * 滑动状态监听器
     */
    public interface OnScrollStateListener {
        /**
         * 当 RecyclerView 往右滑动时。
         *
         * @param recyclerView 滑动的 RecyclerView。
         */
        void onRightScrolled(RecyclerView recyclerView);

        /**
         * 当 RecyclerView 往左滑动时。
         *
         * @param recyclerView 滑动的 RecyclerView。
         */
        void onLeftScrolled(RecyclerView recyclerView);

        /**
         * 当 RecyclerView 往下滑动时。
         *
         * @param recyclerView 滑动的 RecyclerView。
         */
        void onDownScrolled(RecyclerView recyclerView);

        /**
         * 当 RecyclerView 往上滑动时。
         *
         * @param recyclerView 滑动的 RecyclerView。
         */
        void onUpScrolled(RecyclerView recyclerView);

        /**
         * @see RecyclerView.OnScrollListener#onScrollStateChanged(RecyclerView, int)
         */
        void onScrollStateChanged(RecyclerView recyclerView, int newState);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnScrollStateListener != null) {
                mOnScrollStateListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOnScrollStateListener != null) {
                if (dx > 0) {
                    mOnScrollStateListener.onRightScrolled(recyclerView);
                } else if (dx < 0) {
                    mOnScrollStateListener.onLeftScrolled(recyclerView);
                }
                if (dy > 0) {
                    mOnScrollStateListener.onDownScrolled(recyclerView);
                } else if (dy < 0) {
                    mOnScrollStateListener.onUpScrolled(recyclerView);
                }
            }

            if (mOnLastItemVisibleListener != null && isListenerLastItemVisible) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleCount = recyclerView.getChildCount();
                View lastVisibleView = recyclerView.getChildAt(visibleCount - 1);
                int lastChildLayoutPosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                int itemCount = layoutManager.getItemCount();
                if (itemCount + 1 == lastChildLayoutPosition) {
                    lastVisibleView.setVisibility(View.VISIBLE);
                    isListenerLastItemVisible = false;
                    mOnLastItemVisibleListener.onLastItemVisible(lastVisibleView);
                }
            }
        }
    };
}
