package com.superli.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

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

    // mMVPMatrix 的全称是 "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // 这个投影矩阵被用于onDrawFrame()中绘制对象的坐标系
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // 重绘背景色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //绘制三角
//        mTriangle.draw();

        /**
         * 设置相机的位置（相当于观察点的坐标）
         *void setLookAtM(matrix, offset,//视图矩阵和偏移量
         *              eysx, eyey, eyez,//定义目标观察点的位置
         *	           centerx, centery, centerz,//指定视线上任意一点
         *				upx, upy, upz)//表示那个方向朝上
         **/
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // 计算绘制物体最终在屏幕上的投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // 为三角形创建旋转变换
        float[] scratch = new float[16];
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        // 将旋转矩阵和投影、相机视图链接到一起
        // 为了矩阵乘积的正确性，必须将*mMVPMatrix*放在首位
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // 绘制三角形
        mTriangle.draw(scratch);

        // 绘制图形
//        mTriangle.draw(mMVPMatrix);
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
