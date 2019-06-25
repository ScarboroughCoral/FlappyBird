package com.example.lenovo.flappybird;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Pillar {
    public int x;
    public int y;
    public boolean up;

    public Pillar(int x, int y, boolean up) {
        this.x = x;
        this.y = y;
        this.up = up;
    }

    public static final int width=55;

    public static final int height=350;

    public static Rect srcDownRect = new Rect(0,646,55,966);

    public static Rect srcUpRect = new Rect(110,646,165,966);

    public static int realWidth=width;

    public static int realHeight=height;

    public static List<Pillar> pillars;


}
