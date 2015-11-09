package com.wenym.grooo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wenym.grooo.R;

public class ProgressFragment extends Fragment {
    private View currentView;
    private String currTask;
    private ProgressBar indeterminate;
    private boolean isShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.currTask = bundle.getString("currTask");
        this.isShow = bundle.getBoolean("isShow");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_progress, container,
                false);
        indeterminate = (ProgressBar) currentView
                .findViewById(R.id.progressBarCircularIndetermininate);
        if (!isShow) {
            indeterminate.setVisibility(View.INVISIBLE);
        }
        ((TextView) (currentView.findViewById(R.id.progress_task)))
                .setText(currTask);
        return currentView;
    }

    public void setCurrTask(String currTask) {
        this.currTask = currTask;
        ((TextView) (currentView.findViewById(R.id.progress_task)))
                .setText(currTask);
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
        if (!isShow) {
            indeterminate.setVisibility(View.INVISIBLE);
        }
    }

}
