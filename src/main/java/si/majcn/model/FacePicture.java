package si.majcn.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FacePicture {
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_DOWN = 3;
    public static final int MOVE_UP = 4;

    private float x;
    private float y;

    private Bitmap img;
    private int imgSize;

    public int animate_pos = 0;

    private float animateX = 0;
    private float animateY = 0;

    public FacePicture(Bitmap img, float posx, float posy) {
        this.x = posx;
        this.y = posy;
        this.img = img;
        imgSize = img.getWidth();
    }

    public void update(int delta) {
        float distance = delta * imgSize / 500f;
        switch (animate_pos) {
            case MOVE_RIGHT:
                animateX += distance;
                break;
            case MOVE_LEFT:
                animateX -= distance;
                break;
            case MOVE_UP:
                animateY -= distance;
                break;
            case MOVE_DOWN:
                animateY += distance;
                break;
        }

        if (Math.abs(animateX) > imgSize) {
            this.x += (animateX > 0) ? imgSize : -imgSize;
            animateX = 0;
            animate_pos = 0;
        }

        if (Math.abs(animateY) > imgSize) {
            this.y += (animateY > 0) ? imgSize : -imgSize;
            animateY = 0;
            animate_pos = 0;
        }
    }

    public void animate(int position) {
        if (isAnimating()) {
            return;
        }
        this.animate_pos = position;
    }

    public boolean isAnimating() {
        return animate_pos != 0;
    }

    public void drawOnCanvas(Canvas c) {
        c.drawBitmap(img, x+animateX, y+animateY, null);
    }
}
