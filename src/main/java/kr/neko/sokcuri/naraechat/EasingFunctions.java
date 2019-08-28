package kr.neko.sokcuri.naraechat;

public class EasingFunctions {
    public static float easeOutQuad(float t) {
        return t*(2-t);
    }
    public static float easeOutCubic(float t) {
        return (--t)*t*t+1;
    }
    public static float easeOutQuint(float t) {
        return 1+(--t)*t*t*t*t;
    }
}
