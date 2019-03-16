package screen.from.text.read.com.readtextfromscreen;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class Wrap extends LinearLayoutManager {

    public Wrap(Context context) {
        super(context);
    }

    public Wrap(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public Wrap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("probe", "meet a IOOBE in RecyclerView");
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}