package app.utsavstha.com.opengltest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.DisplayMetrics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.xml.transform.Transformer;

/**
 * Created by utsav on 12/24/2016.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    public volatile float mAngle;
    private Triangle mTraingle;
    private Square mSquare;
    private Cube mCube;
    private Line line;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    //matrix for rotation
    private float[] mRotationMatrix = new float[16];
    private Context context;
    public MyGLRenderer(Context context) {
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Since the coordinates of these dont change during execution initializing them n on surface created
        //for efficiency
        //initialising a trianlge
        mTraingle = new Triangle();

        //initialising a square
        mSquare = new Square();

        mCube = new Cube();
        line = new Line();

    }
    public void redrawLine(Simple2DCordinate one, float z, Simple2DCordinate two, float c){
//        line2.SetVerts(0.7f, -0.7f, 0f, 0.5f, 0.5f, 0f);
//        line2.SetColor(.5f, .5f, 1f, 1.0f);
    }
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

       // mCube.draw(scratch);
       // line.draw(scratch);
       // mCube.draw(scratch);
        mSquare.draw(scratch);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }
    public Simple2DCordinate GetWorldCoords(float toucX, float toucY) {
        Camera camera;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenH = displaymetrics.heightPixels;
        int screenW = displaymetrics.widthPixels;


        // Auxiliary matrix and vectors
        // to deal with ogl.
        float[] invertedMatrix, transformMatrix,
                normalizedInPoint, outPoint;
        invertedMatrix = new float[16];
        transformMatrix = new float[16];
        normalizedInPoint = new float[4];
        outPoint = new float[4];

        // Invert y coordinate, as android uses
        // top-left, and ogl bottom-left.
        int oglTouchY = (int) (screenH - toucY);

       /* Transform the screen point to clip
       space in ogl (-1,1) */
        normalizedInPoint[0] =
                (float) ((toucX) * 2.0f / screenW - 1.0);
        normalizedInPoint[1] =
                (float) ((oglTouchY) * 2.0f / screenH - 1.0);
        normalizedInPoint[2] = -1.0f;
        normalizedInPoint[3] = 1.0f;

       /* Obtain the transform matrix and
       then the inverse. */
//        Print("Proj", getCurrentProjection(gl));
//        Print("Model", getCurrentModelView(gl));
        Matrix.multiplyMM(
                transformMatrix, 0,
                mProjectionMatrix, 0,
                mViewMatrix, 0);
        Matrix.invertM(invertedMatrix, 0,
                transformMatrix, 0);

       /* Apply the inverse to the point
       in clip space */
        Matrix.multiplyMV(
                outPoint, 0,
                invertedMatrix, 0,
                normalizedInPoint, 0);

        if (outPoint[3] == 0.0) {
            // Avoid /0 error.

            return null;
        }

        // Divide by the 3rd component to find
        // out the real position.
//        worldPos.Set(
//                outPoint[0] / outPoint[3],
//                outPoint[1] / outPoint[3]);
        return new Simple2DCordinate((outPoint[0] / outPoint[3]),(outPoint[0] / outPoint[3]));
    }
}