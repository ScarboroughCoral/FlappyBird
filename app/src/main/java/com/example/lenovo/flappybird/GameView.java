package com.example.lenovo.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private boolean mIsGameOver=false;
    private Bitmap bitmap;
    private int i=0;
    private double heightRate;
    private double widthRate;
    private int velocity = 16;
    private boolean win=false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            gameOver();
            if (msg.what==1){
                Toast.makeText(getContext(),"游戏失败！",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),"游戏完成！",Toast.LENGTH_SHORT).show();
            }

            ((Activity)getContext()).finish();

        }
    };

    public void gameOver(){
        this.mIsGameOver=true;
    }

    public GameView(Context context) {
        super(context);
        mSurfaceHolder=getHolder();
        mPaint = new Paint();
        mSurfaceHolder.addCallback(this);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.atlas,opts);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                velocity=-32;
                return false;
            }
        });

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        heightRate=Background.height*1.0/getHeight();
        widthRate=Background.width*1.0/getWidth();

        restoreGameData();
        myDraw();
        new Thread(this).start();

    }

    private void restoreGameData() {
        //bird coordinate
        Bird.x=0;
        Bird.y=0;
        Bird.realHeight= (int) (Bird.height/heightRate);
        Bird.realWidth= (int) (Bird.width/widthRate);
        //background coordinate
        Background.srcX=0;
        Background.srcY=0;
        Background.srcTmpX=getWidth();
        Background.srcTmpY=0;
        Background.realHeight=(int) (Background.height/heightRate);
        Background.realWidth=(int) (Background.width/widthRate);

        //pillars coordinate
        Pillar.realHeight=(int) (Pillar.height/heightRate);
        Pillar.realWidth=(int) (Pillar.width/heightRate);
        Pillar.pillars = new ArrayList<>();
        int tmpY=getHeight()-Pillar.realHeight;
        Pillar.pillars.add(new Pillar(500,tmpY,false));
        Pillar.pillars.add(new Pillar(1800,0,true));
        Pillar.pillars.add(new Pillar(2800,tmpY,false));
        Pillar.pillars.add(new Pillar(3700,0,true));
        Pillar.pillars.add(new Pillar(3900,0,true));
        Pillar.pillars.add(new Pillar(4800,tmpY,false));
        Pillar.pillars.add(new Pillar(5100,tmpY,false));
        Pillar.pillars.add(new Pillar(5900,0,true));
        Pillar.pillars.add(new Pillar(6500,tmpY,false));
        Pillar.pillars.add(new Pillar(6900,tmpY,false));


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }



    @Override
    public void run() {
        while (!mIsGameOver){
            logic();
            show();
        }
    }

    private void show() {
        myDraw();
    }

    private void logic() {
        i=(i+1)%18;
        if (Bird.x<getWidth()/2-Bird.realWidth){
            Bird.x+=4;
        }
        //background and pillars start moving
        else {

            Background.srcX-=4;
            if (Background.srcX==-getWidth()){
                Background.srcX=getWidth();
            }
            Background.srcTmpX-=4;
            if (Background.srcTmpX==-getWidth()){
                Background.srcTmpX=getWidth();
            }

            for (int j = 0; j < Pillar.pillars.size(); j++) {
                Pillar pillar =  Pillar.pillars.get(j);
                pillar.x-=4;

            }


        }
        if (Bird.y<(getHeight()-Bird.realHeight)&&Bird.y>0){
            Bird.y+=velocity;
        }else if (Bird.y>=(getHeight()-Bird.realHeight)&&velocity<0){
            Bird.y+=velocity;
        }else if (Bird.y<=0&&velocity>0){
            Bird.y+=velocity;
        }
        if (velocity<16) velocity+=4;
        if (velocity>16) velocity=16;
        Rect birdRect = new Rect(Bird.x,Bird.y,Bird.x+Bird.realWidth,Bird.y+Bird.realHeight);
        win=true;
        for (int j = 0; j < Pillar.pillars.size(); j++) {
            Pillar pillar =  Pillar.pillars.get(j);
            Rect rect = new Rect(pillar.x,pillar.y,pillar.x+Pillar.realWidth,pillar.y+Pillar.realHeight);
            if (rect.intersect(birdRect)){
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
                return;
            }
            if (pillar.x+Pillar.realWidth>Bird.x){
                win=false;
            }
        }
        if (win){
            Message message = new Message();
            message.what=0;
            handler.sendMessage(message);
            return;
        }
    }

    private void myDraw(){
        Canvas canvas = mSurfaceHolder.lockCanvas();

        //clear all
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //draw background
        canvas.drawBitmap(bitmap,Background.srcRect,
                new Rect(Background.srcX,Background.srcY, Background.srcX+Background.realWidth, Background.srcY+Background.realHeight),
                mPaint);
        //draw peer background
        canvas.drawBitmap(bitmap,Background.srcRect,
                new Rect(Background.srcTmpX,Background.srcTmpY, Background.srcTmpX+Background.realWidth, Background.srcTmpY+Background.realHeight),
                mPaint);
        //draw bird
        canvas.drawBitmap(bitmap,Bird.srcRects.get(i/6),
                new Rect(Bird.x,Bird.y, Bird.x+Bird.realWidth, Bird.y+Bird.realHeight)
                ,mPaint);
        //draw pillar
        for (int j = 0; j < Pillar.pillars.size(); j++) {
            Pillar pillar = Pillar.pillars.get(j);
            //is upside
            if (pillar.up){
                canvas.drawBitmap(bitmap,Pillar.srcUpRect,
                        new Rect(pillar.x,pillar.y,pillar.x+Pillar.realWidth,pillar.y+Pillar.realHeight),
                        mPaint);
            }
            //is downside
            else {
                canvas.drawBitmap(bitmap,Pillar.srcDownRect,
                        new Rect(pillar.x,pillar.y,pillar.x+Pillar.realWidth,pillar.y+Pillar.realHeight),
                        mPaint);
            }
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


}
