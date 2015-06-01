package com.ast.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Cube {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer texBuffer;
    private FloatBuffer mColorBuffer;
    public int[] textureIDs = new int[6];

    private float vertices[] = {
            // z face cw
            -1.0f, -1.0f, 1.0f, // V1 - bottom left
            -1.0f, 1.0f, 1.0f, // V2 - top left
            1.0f, -1.0f, 1.0f, // V3 - bottom right
            1.0f, 1.0f, 1.0f, // V4 - top right
            // -z
            -1f,1f,-1f,
            -1f,-1f,-1f,
            1f,1f,-1f,
            1f,-1f,-1f,
            // y (north)
            -1f,1f,1f,
            -1f,1f,-1f,
            1f,1f,1f,
            1f,1f,-1f,
            // -y
            -1f,-1f,-1f,
            -1f,-1f,1f,
            1f,-1f,-1f,
            1f,-1f,1f,
            // x (right)
            1f,-1f,1f,
            1f,1f,1f,
            1f,-1f,-1f,
            1f,1f,-1f,
            // -x
            -1f,-1f,-1f,
            -1f,1f,-1f,
            -1f,-1f,1f,
            -1f,1f,1f
    };

    private float texCoords[] = {
            0.0f, 1.0f, // A. left-bottom
            0.0f, 0.0f, // C. left-top
            1.0f, 1.0f, // B. right-bottom
            1.0f, 0.0f, // D. right-top
            0f, 1f, 0f, 0f, 1f, 1f,	1f, 0f,
            0f, 1f, 0f, 0f, 1f, 1f,	1f, 0f,
            0f, 1f, 0f, 0f, 1f, 1f,	1f, 0f,
            0f, 1f, 0f, 0f, 1f, 1f,	1f, 0f,
            0f, 1f, 0f, 0f, 1f, 1f,	1f, 0f
    };

    private float colors[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f,	0.0f, 1.0f,
            1.0f, 0.5f, 0.0f, 1.0f,
            1.0f, 0.5f, 0.0f, 1.0f,
            1.0f, 0.0f,	0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f
    };

    public Cube()
    {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length*4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        texBuffer = byteBuf.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colors.length*4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0,texBuffer);
        gl.glColorPointer(4,GL10.GL_FLOAT,0,mColorBuffer);

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        for(int i=0; i<6; ++i) {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]); //use texture of ith face
            //draw 2 triangles making up this face
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4*i, 4);
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    // Load an image into GL texture
    public void loadTexture(GL10 gl, Context context) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array
//		for (int i=0; i<4; ++i) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]); // Bind to texture ID
//		}
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        // Construct an input stream to texture image
        InputStream istream = context.getResources().openRawResource(R.raw.ic_launcher);
        Bitmap bitmap;
        try { // Read and decode input as bitmap
            bitmap = BitmapFactory.decodeStream(istream);
        } finally { try { istream.close(); } catch(IOException e) { } }

        // Build Texture from loaded bitmap for the currently bound texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}

