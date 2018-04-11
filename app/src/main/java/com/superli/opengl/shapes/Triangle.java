package com.superli.opengl.shapes;

import android.opengl.GLES20;

import com.superli.opengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Kiven on 2018/4/9.
 * Details:
 */

public class Triangle {
    private FloatBuffer vertexBuffer;
    private int mProgram;
    // 数组中，每个点的坐标数。
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // 逆时针方向:
            0.0f,  0.622008459f, 0.0f, // 上
            -0.5f, -0.311004243f, 0.0f, // 左下
            0.5f, -0.311004243f, 0.0f  // 右下
    };
    // 设置颜色为红，绿，蓝，以及透明度值。
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Triangle() {
        // 初始化三角形在坐标轴的的点，并存放在Byte buffer中。申请一个
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // 使用设备硬件使用的字节序
        bb.order(ByteOrder.nativeOrder());

        // 使用浮点buffer视图操作数据
        vertexBuffer = bb.asFloatBuffer();
        // 在浮点缓冲视图中中添加三角形的坐标点集合。
        vertexBuffer.put(triangleCoords);
        // 标记缓冲去的位置为第一个
        vertexBuffer.position(0);

//        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
//                vertexShaderCode);
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode2);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // 创建一个空的OpenGL ES 程序
        mProgram = GLES20.glCreateProgram();

        // 将顶点渲染器添加到程序中
        GLES20.glAttachShader(mProgram, vertexShader);

        // 将碎片着色器添加到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 执行链接程序
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        // 将渲染程序添加到OpenGL ES环境中
        GLES20.glUseProgram(mProgram);

        // 获取顶点渲染器vPosition成员位置的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 准备三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取碎片渲染器的vColor成员句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 弃用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void draw(float[] mvpMatrix) { // 传递已经计算好的变换矩阵
        draw();

        // 获取图形变换矩阵的句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 将投影和视图变换传递给渲染器
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // 绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 弃用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private final String vertexShaderCode2 =
            // 这个矩阵的成员变量提供了一个Hook来操作使用顶点渲染器渲染的对象的坐标
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // 矩阵必须包含gl_Position修饰符
                    // 注意，为了保证乘积的可靠性，uMVPMatrix元素必须是第一个
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // 用于设置视图变换
    private int mMVPMatrixHandle;


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
}
