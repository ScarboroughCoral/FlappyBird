package com.example.lenovo.flappybird;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * 小鸟类
 */
public class Bird {

    //bird 坐标
    public static int x;
    public static int y;

    //bird大小
    public static final int width=35;
    public static final int height=26;

    public static int realWidth=width;
    public static int realHeight=height;


    //资源文件坐标
    public static List<Rect> srcRects = new ArrayList<>();

    static {
        srcRects.add(new Rect(5,981,5+width,981+height));
        srcRects.add(new Rect(60,981,60+width,981+height));
        srcRects.add(new Rect(117,981,117+width,981+height));
    }
}
