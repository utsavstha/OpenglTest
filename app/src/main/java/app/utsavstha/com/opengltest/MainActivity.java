package app.utsavstha.com.opengltest;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mGLView = (GLSurfaceView) findViewById(R.id.glview);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
}
