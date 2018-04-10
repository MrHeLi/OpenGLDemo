package com.superli.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Kiven on 2018/4/9.
 * Details:
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;
    public MyGLSurfaceView(Context context) {
        super(context);
        // 创建一个OpenGL ES2.0的上下文。
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();
        // 将绘制用的渲染器设置给GLSurfaceView。
        setRenderer(mRenderer);
        // 只有当绘制数据变化时，才绘制/重绘视图。
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
