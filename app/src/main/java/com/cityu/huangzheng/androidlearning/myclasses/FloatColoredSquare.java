package com.cityu.huangzheng.androidlearning.myclasses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by huangzheng on 2/10/15.
 */
public class FloatColoredSquare extends Square
{
    float[] colors = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f
    };
   FloatBuffer colorBuffer;

    public FloatColoredSquare()
    {
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    @Override
    public void draw(GL10 gl)
    {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        super.draw(gl);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
