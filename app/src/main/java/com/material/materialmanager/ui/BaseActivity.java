package com.material.materialmanager.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Doing on 2016/12/23 0023.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     *    findViewById
     *    使用示例：
     *    ibTakePicture = $(R.id.ib_camera_take_picture);
     */
    protected  <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

}
