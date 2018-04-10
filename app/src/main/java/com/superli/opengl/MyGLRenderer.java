package com.superli.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.superli.opengl.shapes.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kiven on 2018/4/9.
 * Details:
 */

public class MyGLRenderer implements GLSurfaceView.Renderer{
    private Triangle mTriangle;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // 设置黑色的背景
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // 初始化三角形
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // 重绘背景色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //绘制三角
        mTriangle.draw();
    }

    public static int loadShader(int type, String shaderCode){

        // 创建一个顶点渲染器类型(GLES20.GL_VERTEX_SHADER)
        // 或者碎片渲染器类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // 将渲染器源码加入渲染器并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
