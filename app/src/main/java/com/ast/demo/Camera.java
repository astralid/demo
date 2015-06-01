package com.ast.demo;

import android.opengl.GLU;
import android.util.FloatMath;

import javax.microedition.khronos.opengles.GL10;

public class Camera {
    private float[] refOut = new float[] {0,0,-1};
    private float[] out = new float[] {0,1,-1};
    private float[] up = new float[] {0,1,1};
    private float[] right = new float[] {1,0,0};
    public float[] pos = new float[] {0,-10,10};
    int sW, sH;
    float fovy, zNear, zFar;
    public float[] farHit;
    // orbit around [0,0,0]
    float[] pt = new float[] {0,0,0};

    Camera () {
        mUtil.normalize(out);
        mUtil.normalize(up);
        mUtil.normalize(right);
    }

    public void orbit (float dx, float dy) {
        float dist = mUtil.length(pos);

        // rotate projections on xy plane
        float[] o = mUtil.rotate2d(out[0], out[1], dx);
        float[] r = mUtil.rotate2d(right[0], right[1], dx);
        float[] u = mUtil.rotate2d(up[0], up[1], dx);
        // restore z
        out = new float[] { o[0], o[1], out[2] };
        right = new float[] { r[0], r[1], right[2] };
        up = new float[] { u[0], u[1], up[2] };

        pitch(-dy);
        pos = mUtil.plus(pt, mUtil.mult(out, -dist));
    }

    public void setProjection (int w, int h, float fy, float zN, float zF) {
        sW = w;
        sH = h;
        fovy = fy;
        zNear = zN;
        zFar = zF;
        farHit = mUtil.plus(mUtil.mult(out,zFar),pos);
    }
    public float[] getNearFarRay (float x, float y) {
        // x,y between [-1,1]
        double ym = Math.tan(mUtil.deg2rad*fovy/2);
        double xm = ym * sW/sH;
        double nearY = zNear * ym * y;
        double nearX = zNear * xm * x;
        double farY = zFar * ym * y;
        double farX = zFar * xm * x;
        // in cam coord space
        return new float[] {
                (float)nearX, (float)nearY, zNear,
                (float)farX, (float)farY, zFar
        };
    }
    public void move (float dx, float dy) {
        pos = mUtil.plus(
                pos, mUtil.plus(
                        mUtil.mult(right, dx),
                        mUtil.mult(up, dy)));
    }
    public void zoom (float d) {
        pos = mUtil.plus(
                pos, mUtil.mult(out, d) );
    }
    public void look (float dx, float dy) {
        float pAng = mUtil.angle(out, refOut);
        yaw(-dx * FloatMath.sin(pAng));
        roll(dx * FloatMath.cos(pAng));
        pitch(dy);
        pAng = mUtil.angle(out, refOut);
        double min = Math.PI/12;
        double max = Math.PI*3/4;
        if (pAng < min || pAng > max) {
            pitch(-dy);
        }
    }

    public void pitch (float th) {
        float cos = (float) Math.cos(th);
        float sin = (float) Math.sin(th);
        up = mUtil.unit(
                mUtil.minus(
                        mUtil.mult(up,cos),
                        mUtil.mult(out,sin) ));
        out = mUtil.unit(
                mUtil.plus(
                        mUtil.mult(up,sin),
                        mUtil.mult(out,cos) ));
    }
    public void yaw (float th) {
        float cos = (float) Math.cos(th);
        float sin = (float) Math.sin(th);
        right = mUtil.unit(
                mUtil.plus(
                        mUtil.mult(right,cos),
                        mUtil.mult(out,sin) ));
        out = mUtil.unit(
                mUtil.minus(
                        mUtil.mult(out,cos),
                        mUtil.mult(right,sin) ));
    }
    void roll (float th) {
        float cos = (float) Math.cos(th);
        float sin = (float) Math.sin(th);
        right = mUtil.unit(
                mUtil.minus(
                        mUtil.mult(right,cos),
                        mUtil.mult(up,sin) ));
        up = mUtil.unit(
                mUtil.plus(
                        mUtil.mult(right,sin),
                        mUtil.mult(up,cos) ));
    }
    void applyView (GL10 gl) {
        GLU.gluLookAt(gl,
                pos[0], pos[1], pos[2],
                pos[0] + out[0], pos[1] + out[1], pos[2] + out[2],
                up[0], up[1], up[2]);
    }
}
