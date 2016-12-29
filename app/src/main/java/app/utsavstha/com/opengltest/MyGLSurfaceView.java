package app.utsavstha.com.opengltest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
 * Created by utsav on 12/24/2016.
 */
public class MyGLSurfaceView extends GLSurfaceView  {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private float mDensity;

    private final MyGLRenderer mRenderer;
    Context context;
    public MyGLSurfaceView(Context context){
        super(context);
        this.context = context;
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer(getContext());

        // Render the view only when there is a change in the drawing data.
        // To allow the triangle to rotate automatically, this line is commented out:

        // Set the Renderer for drawing on the GLSurfaceView

        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();


        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:

                x = e.getX();
                mPreviousX = x;

                y = e.getY();
                mPreviousY = y;
                mRenderer.redrawLine(mRenderer.GetWorldCoords(mPreviousX, mPreviousY), 0, mRenderer.GetWorldCoords(e.getX(), e.getY()), 0);
                //requestRender();
                break;
            case MotionEvent.ACTION_MOVE:
                if(e.getX() != mPreviousX){
                    //mRenderer.redrawLine(mRenderer.GetWorldCoords(mPreviousX, mPreviousY), 0, mRenderer.GetWorldCoords(e.getX(), e.getY()), 0);
                    mPreviousX = e.getX();
                    mPreviousY = e.getY();
                    requestRender();
                }
                break;
            case MotionEvent.ACTION_UP:
                mPreviousX = 0;
                mPreviousY = 0;
                break;



        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}

