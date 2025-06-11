package com.iyuba.camstory.event;

public class OnPlayChangeEvent {
    private boolean isPlay;

    public OnPlayChangeEvent() {
    }

    public OnPlayChangeEvent(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
