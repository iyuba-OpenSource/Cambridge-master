package tv.lycam.gift.core;

public abstract class EventManager {
    public abstract void registerEventRunner(int eventCode, OnEventRunner runner);

    public abstract Event runEvent(int eventCode, Object... params);

    public abstract Event runUiEvent(int eventCode, Object... params);

    public abstract void addEventListener(int eventCode, OnEventListener listener);

    public abstract void removeEventListener(int eventCode, OnEventListener listener);

    public interface OnEventRunner {
        void onEventRun(Event event) throws Exception;
    }

    public interface OnEventListener {
        void onEventEnd(Event event);
    }
}
