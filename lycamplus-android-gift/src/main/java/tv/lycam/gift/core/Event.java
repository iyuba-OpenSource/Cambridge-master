package tv.lycam.gift.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Event {

    protected final int mEventCode;

    protected boolean mIsSuccess = false;

    protected Exception mFailException;

    protected Object mParams[];

    protected int mHashCode;

    protected List<Object> mReturnParams;

    protected boolean mIsCancel;

    protected List<EventManager.OnEventListener> mEventListeners;
    protected EventCanceller mCanceller;
    protected List<OnEventProgressListener> mProgressListeners;
    protected int mProgress;

    public Event(int eventCode, Object params[]) {
        mEventCode = eventCode;
        mParams = params;
        mHashCode = getEventCode();
        if (mParams != null) {
            for (Object obj : mParams) {
                if (obj != null) {
                    mHashCode = mHashCode * 29 + obj.hashCode();
                }
            }
        }
    }

    public int getEventCode() {
        return mEventCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o != null && o instanceof Event) {
            final Event other = (Event) o;
            if (getEventCode() == other.getEventCode()) {
                return Arrays.equals(mParams, other.getParams());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("code=");
        sb.append(mEventCode);
        sb.append("{");
        for (Object obj : mParams) {
            if (obj != null) {
                sb.append(obj.toString()).append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public void setSuccess(boolean bSuccess) {
        mIsSuccess = bSuccess;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public void setCanceller(EventCanceller canceller) {
        mCanceller = canceller;
    }

    void cancel() {
        mIsCancel = true;
        mIsSuccess = false;
        if (mCanceller != null) {
            mCanceller.cancelEvent(this);
        }
    }

    void setResult(Event other) {
        mReturnParams = other.mReturnParams;
        mFailException = other.mFailException;
        mIsSuccess = other.mIsSuccess;
        mIsCancel = other.mIsCancel;
    }

    public void addEventListener(EventManager.OnEventListener listener) {
        if (mEventListeners == null) {
            mEventListeners = new ArrayList<EventManager.OnEventListener>();
        }
        mEventListeners.add(listener);
    }

    public void addEventListener(int pos, EventManager.OnEventListener listener) {
        if (mEventListeners == null) {
            mEventListeners = new ArrayList<EventManager.OnEventListener>();
        }
        mEventListeners.add(pos, listener);
    }

    public void addAllEventListener(Collection<EventManager.OnEventListener> listeners) {
        if (listeners == null) {
            return;
        }
        if (mEventListeners == null) {
            mEventListeners = new ArrayList<EventManager.OnEventListener>();
        }
        mEventListeners.addAll(listeners);
    }

    public void removeEventListener(EventManager.OnEventListener listener) {
        if (mEventListeners != null) {
            mEventListeners.remove(listener);
        }
    }

    void clearEventListener() {
        mEventListeners = null;
    }

    List<EventManager.OnEventListener> getEventListeners() {
        return mEventListeners;
    }

    void addProgressListener(OnEventProgressListener listener) {
        if (mProgressListeners == null) {
            mProgressListeners = new ArrayList<OnEventProgressListener>();
        }
        mProgressListeners.add(listener);
    }

    void addAllProgressListener(Collection<OnEventProgressListener> listeners) {
        if (listeners == null) {
            return;
        }
        if (mProgressListeners == null) {
            mProgressListeners = new ArrayList<OnEventProgressListener>();
        }
        mProgressListeners.addAll(listeners);
    }

    void removeProgressListener(OnEventProgressListener listener) {
        if (mProgressListeners != null) {
            mProgressListeners.remove(listener);
        }
    }

    public List<OnEventProgressListener> getProgressListeners() {
        return mProgressListeners;
    }

    public void setProgress(int progress) {
        if (mProgress != progress) {
            mProgress = progress;
            if (mProgressListeners != null) {
                AndroidEventManager.getInstance().notifyEventProgress(this);
            }
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public boolean isCancel() {
        return mIsCancel;
    }

    public <E extends Object> E[] getParams() {
        return (E[]) mParams;
    }

    public <E extends Object> E getParamAtIndex(int index) {
        if (mParams != null && mParams.length > index) {
            return (E) mParams[index];
        }
        return null;
    }

    public void setFailException(Exception e) {
        mFailException = e;
    }

    public String getFailMessage() {
        return mFailException == null ? null : mFailException.getMessage();
    }

    public Exception getFailException() {
        return mFailException;
    }

    public void addReturnParam(Object obj) {
        if (mReturnParams == null) {
            mReturnParams = new ArrayList<Object>();
        }
        mReturnParams.add(obj);
    }

    public <E extends Object> E getReturnParamAtIndex(int index) {
        if (mReturnParams == null || index >= mReturnParams.size()) {
            return null;
        }
        return (E) mReturnParams.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T findParam(Class<T> c) {
        if (mParams != null) {
            for (Object obj : mParams) {
                if (c.isInstance(obj)) {
                    return (T) obj;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T findReturnParam(Class<T> c) {
        if (mReturnParams != null) {
            for (Object obj : mReturnParams) {
                if (c.isInstance(obj)) {
                    return (T) obj;
                }
            }
        }
        return null;
    }
}
