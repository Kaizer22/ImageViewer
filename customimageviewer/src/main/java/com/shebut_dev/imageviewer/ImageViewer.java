package com.shebut_dev.imageviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.shebut_dev.imageviewer.customimageviewer.R;

public class ImageViewer extends View {

    private int MAX_IMAGE_WIDTH = 1000;
    private int MAX_IMAGE_HEIGHT = 1000;

    private int MAX_OUTER = 200;

    private final int DEFAULT_IMAGE = R.drawable.iv_default_image;

    private Bitmap viewableImage;
    private Rect actualImagePos;

    private boolean isScaled = false;

    //private Drawable viewableImage;

    private ScaleGestureDetector scaleDetector;
    private float scaleFactor;

    private Paint ivPaint;

    int startEventMoveX1 = 0, startEventMoveY1 = 0;
    int startEventMoveX = 0, startEventMoveY = 0;
    int finalX = 0, finalY = 0;
    int finalX1 = 0, finalY1 = 0;

    private int screenWidth;
    private int screenHeight;
    private int imageViewerWidth;
    private int imageViewerHeight;

    public ImageViewer(Context context, AttributeSet attributes) {
        super(context, attributes);

        ivPaint = new Paint();

        TypedArray typedArray = getContext().obtainStyledAttributes(attributes,
                R.styleable.ImageViewer);
        //viewableImage = typedArray.getDrawable(R.styleable.ImageViewer_iv_source_image);
        viewableImage = BitmapFactory.decodeResource(getResources(),
                typedArray.getResourceId(R.styleable.ImageViewer_iv_source_image,
                R.drawable.iv_default_image));

        typedArray.recycle();

        actualImagePos = new Rect();
        scaleFactor = 1;
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

    }

    //public void setViewableImage(Drawable image){
    public void setViewableImage(Bitmap bitmap){
        int sourceWidth = bitmap.getWidth();
        int sourceHeight = bitmap.getHeight();
        double dep = sourceWidth / (double)sourceHeight;
        int newHeight, newWidth;

        bitmap = bitmap.copy(bitmap.getConfig(),true);
        if (sourceWidth > MAX_IMAGE_WIDTH){
            newWidth = MAX_IMAGE_WIDTH;
            newHeight = (int) (( 1 / dep) * MAX_IMAGE_WIDTH);
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,false);
        }else if (sourceHeight > MAX_IMAGE_HEIGHT){
            newHeight = MAX_IMAGE_HEIGHT;
            newWidth = (int) (MAX_IMAGE_HEIGHT * dep);
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    newWidth, newHeight,false);
        }
        viewableImage = bitmap;
        invalidate();

    }
    public void setViewableBitmap(Bitmap bitmap){
        //viewableImage = new BitmapDrawable(getResources(), bitmap);
        viewableImage = bitmap;
        invalidate();
    }

    public void setViewableResource(int resourceId){
        //viewableImage = ResourcesCompat.getDrawable(getResources(),
                //resourceId, null);
        //viewableImage = Bit
        invalidate();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        MAX_IMAGE_HEIGHT = screenHeight / 10;
        MAX_IMAGE_WIDTH = MAX_IMAGE_HEIGHT;
        Log.d("IMAGE_VIEWER", "Screen" + screenWidth + "__"  +screenHeight);
        Log.d("IMAGE_VIEWER", w + "__" + h);
        imageViewerHeight = h;
        imageViewerWidth = w;
        actualImagePos.left = 0;
        actualImagePos.top = 0;
        actualImagePos.right = imageViewerWidth;
        actualImagePos.bottom = imageViewerHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d("IV", "Drawing...");
        //viewableImage.setBounds(actualImagePos);
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        //viewableImage.draw(canvas);
        canvas.drawBitmap(viewableImage, null, actualImagePos, ivPaint);
        canvas.restore();
        isScaled = false;



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getAction();

        scaleDetector.onTouchEvent(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startEventMoveX = (int) event.getX();
                startEventMoveY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (index == 0){
                    //Log.d("IMAGE_VIEWER", String.valueOf(index));
                    finalX = (int) event.getX();
                    finalY = (int) event.getY();

                    int deltaX;
                    int deltaY;
                    if (scaleFactor > 50){
                        deltaX = (int) ((finalX - startEventMoveX) / 50);
                        deltaY = (int) ((finalY - startEventMoveY) / 50);
                    }else {
                        deltaX = (int) ((finalX - startEventMoveX) / scaleFactor);
                        deltaY = (int) ((finalY - startEventMoveY) / scaleFactor);
                    }

                    deltaX = Math.abs(deltaX) > (screenWidth / 15f) ?
                    deltaX / 10 : deltaX;
                    deltaY = Math.abs(deltaY) > (screenWidth / 15f) ?
                            deltaY / 10 : deltaY;
                    Log.d("IMAGE_VIEWER", String.valueOf(deltaX) +":"+ deltaY);

                    //if (actualImagePos.top - deltaY < -MAX_OUTER ||
                            //actualImagePos.bottom + deltaY > imageViewerHeight + MAX_OUTER){
                        //deltaY = 0;
                    //}
                    //if (actualImagePos.left + deltaX < -MAX_OUTER ||
                           // actualImagePos.right + deltaX > imageViewerWidth + MAX_OUTER){
                       // deltaX = 0;
                    //}
                    actualImagePos.offset(deltaX, deltaY);
                    invalidate();
                    //Log.d("ACTION_MOVE", "Moving...");

                    startEventMoveX = finalX;
                    startEventMoveY = finalY;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 20f));
            //actualImagePos.top -= scaleFactor * 10;
            //actualImagePos.left -= scaleFactor * 10;
            //actualImagePos.right +=scaleFactor * 10;
            //actualImagePos.bottom += scaleFactor * 10;
            isScaled = true;

            invalidate();
            return true;
        }
    }
}
