package si.majcn.Demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.view.View;

public class MyView extends View {

    private Bitmap mImage;
    private FaceDetector.Face mFace;

    private PorterDuffColorFilter mFilter;
    private Paint mPaint;

    public MyView(Context context) {
        super(context);

        BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
        BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.lena, BitmapFactoryOptionsbfo);

        mFace = detectFace(mImage);
        mPaint = new Paint();
    }

    private FaceDetector.Face detectFace(Bitmap image) {
        FaceDetector detector;
        FaceDetector.Face[] faces;

        faces = new FaceDetector.Face[1];
        detector = new FaceDetector(image.getWidth(), image.getHeight(), 1);

        detector.findFaces(image, faces);
        return faces[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mFilter = new PorterDuffColorFilter (Color.CYAN, PorterDuff.Mode.OVERLAY);
        mPaint.setColorFilter(mFilter);

        float eyesDistance = mFace.eyesDistance();
        PointF myMidPoint = new PointF();
        mFace.getMidPoint(myMidPoint);
        Rect targetRect = new Rect((int) (myMidPoint.x - eyesDistance * 2),
                (int) (myMidPoint.y - eyesDistance * 2),
                (int) (myMidPoint.x + eyesDistance * 2),
                (int) (myMidPoint.y + eyesDistance * 2));

        Bitmap output = Bitmap.createBitmap(targetRect.width(), targetRect.height(), Bitmap.Config.RGB_565);
        Canvas imageCanvas = new Canvas(output);
        imageCanvas.drawBitmap(mImage, targetRect, new Rect(0, 0, targetRect.width(), targetRect.height()), null);
        Bitmap img = Bitmap.createScaledBitmap(output, 30, 30, false);
        img = Bitmap.createScaledBitmap(img, targetRect.width()*2, targetRect.height()*2, false);

        canvas.drawBitmap(mImage, targetRect, new Rect(0, 0, targetRect.width(), targetRect.height()), mPaint);
        canvas.drawBitmap(mImage, targetRect, new Rect(targetRect.width() + 10, 0, targetRect.width() * 2 + 10, targetRect.height()), null);
        canvas.drawBitmap(img, null, new Rect(0, targetRect.height() + 10, img.getWidth(), img.getHeight() + targetRect.height() + 10), null);
    }
}
