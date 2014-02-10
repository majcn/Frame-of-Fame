package si.majcn.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import si.majcn.model.FacePicture;

public class DrawingBoard extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private long lastUpdateTime;
    private long currentTime;

    private Bitmap bmp;
    private Bitmap resizedBmp;

    private SurfaceHolder mSurfaceHolder;

    private Thread mThread;
    private boolean running = false;

    private static final int NR_TILES = 30;
    private int mTileSize;

    private ArrayList<FacePicture> models;

    public DrawingBoard(Context context) {
        super(context);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        models = new ArrayList<FacePicture>(NR_TILES*NR_TILES);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Debug.printLog("surfaceCreated");

        mThread = new Thread(this);
        running = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Debug.printLog("surfaceChanged");

        if (width < height) {
            mTileSize = width / NR_TILES;
        } else {
            mTileSize = height / NR_TILES;
        }

        resizedBmp = Bitmap.createScaledBitmap(bmp, mTileSize, mTileSize, true);

        models.clear();
        for(int i=0; i<NR_TILES; i++) {
            for(int j=0; j<NR_TILES; j++) {
                 models.add(new FacePicture(resizedBmp, i*mTileSize, j*mTileSize));
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Debug.printLog("surfaceDestroyed");

        boolean retry = true;
        running = false;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            if (!mSurfaceHolder.getSurface().isValid()) {
                continue;
            }

            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    onDraw(c);
                    //postInvalidate();
                }
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private int getDeltaMillis() {
        currentTime = System.currentTimeMillis();
        int delta = (int)(currentTime - lastUpdateTime);
        lastUpdateTime = currentTime;

        return (delta > 0) ? delta : 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        int countAnimations = 0;
        for (FacePicture fp : models) {
            fp.update(getDeltaMillis());
            fp.drawOnCanvas(canvas);
            countAnimations += fp.isAnimating() ? 1 : 0;
        }
        if(countAnimations < 10) {
            Random generator = new Random();
            int i = generator.nextInt(NR_TILES*NR_TILES);
            models.get(i).animate(generator.nextInt(5));
        }
        /*canvas.save();
        canvas.drawColor(Color.BLACK);
        float s = mTileSize / (float)bmp.getWidth();
        canvas.scale(s,s);
        for(int i=0; i<NR_TILES; i++) {
            for(int j=0; j<NR_TILES; j++) {
                canvas.drawBitmap(bmp, i*mTileSize/s, j*mTileSize/s, null);
            }
        }
        canvas.restore();
        */


    }

 /*   @Override
    public void run() {
        while(running) {
            if(mSurfaceHolder.getSurface().isValid()) {
                Canvas c = mSurfaceHolder.lockCanvas();
                onDraw(c);
                mSurfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    public void pause(){
        running = false;
        while(true){
            try{
                mThread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }break;
        }
        mThread = null;
    }

    public void resume(){
        running = true;
        mThread = new Thread(this);
        mThread.start();
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bmp, 10, 10, null);
    }*/
}
