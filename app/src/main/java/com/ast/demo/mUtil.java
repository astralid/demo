package com.ast.demo;

class mUtil
{
    // radians to degrees multiplier
    public static final double rad2deg = 180 / Math.PI;
    // degrees to radians multiplier
    public static final double deg2rad = Math.PI / 180;
    /*Compute the cross product of two vectors
     p1 The first vector
     p2 The second vector*/
    public static void cross(float[] result, float[] p1, float[] p2)
    {
        result[0] = p1[1] * p2[2] - p2[1] * p1[2];
        result[1] = p1[2] * p2[0] - p2[2] * p1[0];
        result[2] = p1[0] * p2[1] - p2[0] * p1[1];
    }
    public static float dot(float[] v1, float[] v2)
    {
        return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }
    public static void normalize (float[] v)
    {
        float magnitude = android.opengl.Matrix.length(v[0],v[1],v[2]);
        v[0] /= magnitude;
        v[1] /= magnitude;
        v[2] /= magnitude;
    }
    public static void scale (float[] v, float s) {
        v[0] *= s; v[1] *= s; v[2] *= s;
    }
    // project u onto the plane of p1,p2,origin
    public static float[] proj (float[] p1, float[] p2, float[] u) {
        return new float[] {u[0],u[1]};
    }
    public static float[] rotate2d (float x0, float y0, float th) {
        float cos = (float) Math.cos(th);
        float sin = (float) Math.sin(th);
        float x = x0*cos - y0*sin;//x' = x*cos b - y*sin b
        float y = x0*sin + y0*cos;//y' = x*sin b + y*cos b
        return new float[] {x,y};
    }
    public static float angle (float[] u, float[] v) {
        // cosθ = a.b / |a||b|
        return (float) Math.acos(
                dot(u,v) / (length(u)*length(v)) );
    }
    public static float length (float[] u) {
        return android.opengl.Matrix.length(u[0],u[1],u[2]);
    }
    public static float[] unit (float[] u) {
        return div(u, length(u));
    }
    public static float[] div (float[] u, float s) {
        return new float[] { u[0]/s, u[1]/s, u[2]/s };
    }
    public static float[] mult (float[] u, float s) {
        return new float[] { u[0]*s, u[1]*s, u[2]*s };
    }
    public static float[] minus (float[] u, float[] v) {
        return new float[] {
                u[0]-v[0], u[1]-v[1], u[2]-v[2] };
    }
    public static float[] plus (float[] u, float[] v) {
        return new float[] {
                u[0]+v[0], u[1]+v[1], u[2]+v[2] };
    }
}

