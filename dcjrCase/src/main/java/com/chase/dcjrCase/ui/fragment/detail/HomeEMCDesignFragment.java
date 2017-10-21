package com.chase.dcjrCase.ui.fragment.detail;

import android.graphics.Color;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chase on 2017/9/6.
 */

public class HomeEMCDesignFragment extends BaseChildFragment {
    @Override
    protected View getSuccessView() {
        TextView view = new TextView(mActivity);
        view.setText("HomeEMCDesignFragment");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    protected Object requestData() {
        SystemClock.sleep(1000);/*模拟请求服务器的延时过程*/
        return null;/*加载失败*/
    }
}
