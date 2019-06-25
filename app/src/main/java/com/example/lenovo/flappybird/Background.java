package com.example.lenovo.flappybird;

import android.graphics.Rect;

public class Background {
    //Background 坐标
    public static int srcX=0;
    public static int srcY=0;
    public static int srcTmpX=0;
    public static int srcTmpY=0;


    //Background大小
    public static final int width=288;
    public static final int height=512;
    public static int realWidth=288;
    public static int realHeight=512;

    public static Rect srcRect = new Rect(0,0,width,height);
}
