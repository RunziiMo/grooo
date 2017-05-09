package net.azstudio.groooseller.ui.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by runzii on 16-5-21.
 */
public class EmptySupportRecyclerView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && mEmptyView != null) {
                if(adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    EmptySupportRecyclerView.this.setVisibility(View.GONE);
                }
                else {
                    mEmptyView.setVisibility(View.GONE);
                    EmptySupportRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public View getmEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        emptyObserver.onChanged();
    }

    public EmptySupportRecyclerView(Context context) {
        super(context);
    }

    public EmptySupportRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySupportRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
