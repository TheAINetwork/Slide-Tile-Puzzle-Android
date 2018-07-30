package com.tain.slidepuzzle;

public class POGAnimationStrategy implements IAnimation {
    @Override
    public double speedFactor(int from, int to, int current) {
        if (to - from == 0 || current == to) {
            return 0;
        }
        double t = (double)(current - from) / (double)(to - from);
        return t<.5 ? 16*t*t*t*t*t + 0.1 : 1+16*(--t)*t*t*t*t + 0.1;
    }
}
