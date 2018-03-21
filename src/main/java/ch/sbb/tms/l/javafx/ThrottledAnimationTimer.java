package ch.sbb.tms.l.javafx;

import javafx.animation.AnimationTimer;

public abstract class ThrottledAnimationTimer extends AnimationTimer {

    private final double frameRate;

    private long[] frameTimes = new long[20];
    private int frameTimesIdx = 0;

    protected ThrottledAnimationTimer(double frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    public void handle(long now) {
        long oldFrameTime = frameTimes[frameTimesIdx];
        long elapsedNanos = now - oldFrameTime;
        long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
        double currentFrameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;

        if (currentFrameRate < frameRate) {
            handleThrottled(now, currentFrameRate);
            frameTimes[frameTimesIdx] = now;
            frameTimesIdx = (frameTimesIdx + 1) % frameTimes.length;
        }
    }

    protected abstract void handleThrottled(long now, double frameRate);
}
