package com.chase.dcjrCase.ui.fragment.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chase.dcjrCase.view.ContentPage;

public abstract class BaseChildFragment extends Fragment {
    protected ContentPage contentPage;
    public Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentPage = new ContentPage(getActivity()) {
            @Override
            public Object loadData() {
                return requestData();
            }

            @Override
            public View createSuccessView() {
                return getSuccessView();
            }
        };
        return contentPage;
    }

    /**
     * 每个子fragment去实现对应的成功View
     * @return
     */
    protected abstract View getSuccessView();

    /**
     * 每个子fragment去实现请求数据的过程
     * @return
     */
    protected abstract Object requestData();
}