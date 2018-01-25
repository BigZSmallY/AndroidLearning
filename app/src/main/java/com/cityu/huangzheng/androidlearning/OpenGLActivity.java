package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

import com.cityu.huangzheng.androidlearning.myclasses.Cube;
import com.cityu.huangzheng.androidlearning.myclasses.FloatColoredSquare;
import com.cityu.huangzheng.androidlearning.myclasses.Plane;
import com.cityu.huangzheng.androidlearning.myclasses.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by huangzheng on 2/10/15.
 */
public class OpenGLActivity extends Activity
{
    private final String TAG = "OpenGLActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new OpenGLRender());
        setContentView(glSurfaceView);
    }
}

class OpenGLRender implements GLSurfaceView.Renderer
{
    private final String TAG = "OpenGlRender";
    private Square squareA = new Square();
    private Square squareB = new FloatColoredSquare();
    private Plane plane = new Plane();
    private Cube cube = new Cube(1.0f, 1.0f, 1.0f);
    private float angle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glClearColor(0.0f, 1.0f, 1.0f, 0.5f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -10);
        gl.glPushMatrix();
        gl.glTranslatef(0, -1, 0);
        gl.glPushMatrix();
        gl.glRotatef(angle, 0, 0, 1);
        squareA.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(-angle, 0, 0, 1);
        gl.glTranslatef(2, 0, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        squareA.draw(gl);

        gl.glPushMatrix();
        gl.glRotatef(-angle, 0, 0, 1);
        gl.glTranslatef(2, 0, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(angle * 10, 0, 0, 1);
        squareB.draw(gl);
        gl.glPopMatrix();
        gl.glPopMatrix();
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 3.0f, 0.0f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(angle, 0, 0, 1);
        squareA.draw(gl);

        gl.glRotatef(angle, 0, 0, 1);
        gl.glTranslatef(2, 0, 0);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(-angle * 10, 0, 0, 1);
        squareB.draw(gl);
        gl.glPopMatrix();

        cube.rx = 45;
        cube.ry = 45;
        gl.glPushMatrix();
        gl.glTranslatef(1.0f, 1.0f, 0);
        cube.draw(gl);
        gl.glPopMatrix();
        angle++;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
