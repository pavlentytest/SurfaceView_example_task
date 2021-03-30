package ru.samsung.itschool.mdev.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private float x,y,r;
    private MyThread mythread;
    private boolean touchFlag;

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mythread = new MyThread();
        mythread.setRunning(true);
        mythread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // не делать остановку, если нет этого в задании
        
        boolean retry = true;
        mythread.setRunning(false);
        while(retry) {
            try {
                mythread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.x = event.getX();
        this.y = event.getY();
        this.r = 0;
        this.touchFlag = true;
        return true;
    }

    class MyThread extends Thread {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            while(running) {
                Canvas canvas = getHolder().lockCanvas();
                canvas.drawColor(Color.BLUE);
                r += touchFlag ? 5 : 0;
                canvas.drawCircle(x,y,r,paint);
                getHolder().unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}