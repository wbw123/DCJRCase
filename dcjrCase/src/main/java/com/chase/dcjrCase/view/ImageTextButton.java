package com.chase.dcjrCase.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chase.dcjrCase.R;

/**
 * Created by chase on 2017/10/20.
 */

public class ImageTextButton extends LinearLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.chase.dcjrCase";
    private ImageView ib_image;
    private TextView ib_text;

    public ImageTextButton(Context context) {
        super(context);
        initView();
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        String title = attrs.getAttributeValue(NAMESPACE, "title");
        int src = attrs.getAttributeResourceValue(NAMESPACE, "src", R.drawable.btn_midcase_selector);
        int textColor = attrs.getAttributeResourceValue(NAMESPACE, "textColor", Color.BLACK);

        setTitle(title);
        setImage(src);
        setTextColor(textColor);
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.image_text_button, null);

        ib_image = view.findViewById(R.id.ib_image);
        ib_text = view.findViewById(R.id.ib_text);

        setClickable(true);
        setFocusable(true);

        this.addView(view);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        ib_text.setText(title);
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public void setTextColor(int color) {
        ib_text.setTextColor(color);
    }

    /**
     * 设置图片id
     * @param id
     */
    public void setImage(int id) {
        ib_image.setImageResource(id);
    }

}

