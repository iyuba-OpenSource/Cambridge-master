package tv.lycam.gift.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AndroidEventManager extends EventManager {
	
	public static AndroidEventManager getInstance(){
		return sInstance;
	}
	
	static{
		sInstance = new AndroidEventManager();
	}
	
	private static AndroidEventManager sInstance;
	
	private static final int WHAT_EVENT_NOTIFY 	= 1;
	private static final int WHAT_EVENT_PUSH	= 2;
	private static final int WHAT_EVENT_END		= 3;
	private static final int WHAT_EVENT_PROGRESS= 4;
	
	private ExecutorService	mExecutorService;
	
	private MultiValueMap<Integer,OnEventRunner>		mMapCodeToEventRunner = new MultiValueMap<Integer, OnEventRunner>();
	
	private SparseArray<List<OnEventListener>> 			mMapCodeToEventListener = new SparseArray<List<OnEventListener>>();
	private SparseArray<List<OnEventListener>> 			mMapCodeToEventListenerAddCache = new SparseArray<List<OnEventListener>>();
	private SparseArray<List<OnEventListener>> 			mMapCodeToEventListenerRemoveCache = new SparseArray<List<OnEventListener>>();
	private boolean 									mIsMapListenerLock = false;
		
	private MultiValueMap<Integer, OnEventProgressListener> mMapEventCodeToProgressListeners = new MultiValueMap<Integer, OnEventProgressListener>();
	
	private ConcurrentHashMap<Event, Event> 			mMapRunningEvent = new ConcurrentHashMap<Event, Event>();
	
	private static Handler mHandler = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			final int nWhat = msg.what;
			if(nWhat == WHAT_EVENT_PROGRESS){
				final Event e = (Event)msg.obj;
				for(OnEventProgressListener listener : e.getProgressListeners()){
					listener.onEventProgress(e, e.getProgress());
				}
			}else if(nWhat == WHAT_EVENT_END){
				sInstance.onEventRunEnd((Event)msg.obj);
			}else if(nWhat == WHAT_EVENT_PUSH){
				final Event event = (Event)msg.obj;
				sInstance.mExecutorService.execute(new Runnable() {
					@Override
					public void run() {
						sInstance.processEvent(event);
						mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
					}
				});
			}else if(nWhat == WHAT_EVENT_NOTIFY){
				sInstance.doNotify((Event)msg.obj);
			}
		}
	};

	private AndroidEventManager(){
		mExecutorService = Executors.newCachedThreadPool();
	}

	public Event runEvent(int eventCode, Object... params) {
		final Event event = new Event(eventCode, params);
		pushEventInternal(event, 0);
		return event;
	}
	
	public Event pushEventDelayed(int eventCode, long delayMillis, Object... params){
		final Event event = new Event(eventCode, params);
		pushEventInternal(event, delayMillis);
		return event;
	}
	
	public Event pushEventCheckRunning(int eventCode, Object... params){
		Event e = getRuningEvent(eventCode, params);
		if(e == null){
			return runEvent(eventCode, params);
		}else{
			return e;
		}
	}
	
	public Event pushEventEx(int eventCode, OnEventListener listener, Object... params){
		Event event = new Event(eventCode, params);
		event.addEventListener(listener);
		pushEventInternal(event, 0);
		return event;
	}
	
	private void	pushEventInternal(Event event, long delayMillis){
		Event runEvent = mMapRunningEvent.putIfAbsent(event, event);
		if(runEvent == null){
			mHandler.sendMessageDelayed(
					mHandler.obtainMessage(WHAT_EVENT_PUSH, event),
					delayMillis);
		}else{
			runEvent.addAllEventListener(event.getEventListeners());
			runEvent.addAllProgressListener(event.getProgressListeners());
		}
	}
	
	public Event runUiEvent(int eventCode, Object... params) {
		return runEventEx(eventCode, null, null,params);
	}
	
	public Event runEventEx(int eventCode, EventDelegateCanceller canceller, EventProgressDelegate progress, Object... params){
		final Event event = new Event(eventCode, params);
		if(canceller != null){
			canceller.setExecuteEvent(event);
		}
		if(progress != null){
			progress.setExecuteEvent(event);
		}
		final Event runEvent = mMapRunningEvent.putIfAbsent(event, event);
		if(runEvent == null){
			processEvent(event);
			mMapRunningEvent.remove(event);
			mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
		}else{
			runEvent.addEventListener(new OnEventListener() {
				@Override
				public void onEventEnd(Event e) {
					event.setResult(e);
					synchronized (event) {
						event.notify();
					}
				}
			});
			synchronized (event) {
				try{
					event.wait(20000);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return event;
	}
	
	public void		cancelEvent(Event e){
		if(e != null){
			e.cancel();
		}
	}
	
	public void		notifyEvent(int eventCode,Object... params){
		Event e = new Event(eventCode, params);
		e.setSuccess(true);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, e));
	}
	
	public void		cancelAllEvent(){
		mMapRunningEvent.clear();
		mHandler.removeMessages(WHAT_EVENT_PUSH);
		
		mExecutorService.shutdownNow();
		mExecutorService = Executors.newCachedThreadPool();
	}
	
	public void 	registerEventRunner(int eventCode, OnEventRunner runner) {
		mMapCodeToEventRunner.remove(eventCode);
		mMapCodeToEventRunner.put(eventCode, runner);
	}
	
	public void		removeEventRunner(int eventCode, OnEventRunner runner){
		mMapCodeToEventRunner.removeMapping(eventCode, runner);
	}
	
	public void 	clearAllRunners(){
		mMapCodeToEventRunner.clear();
	}
	
	public boolean	hasEventRunning(int eventCode){
		for(Event e : new ArrayList<Event>(mMapRunningEvent.keySet())){
			if(e.getEventCode() == eventCode){
				return true;
			}
		}
		return false;
	}
	
	public boolean	hasEventRunning(int eventCode,Object...params){
		final int paramLength = params == null ? 0 : params.length;
		for(Event e : new ArrayList<Event>(mMapRunningEvent.keySet())){
			if(e.getEventCode() == eventCode){
				final Object otherParams[] = e.getParams();
				int length = Math.min(otherParams == null ? 0 : otherParams.length, paramLength);
				boolean equal = true;
				for(int index = 0;index < length;++index){
					final Object p1 = otherParams[index];
					final Object p2	= params[index];
					if(p1 == null && p2 == null){
						continue;
					}
					if(p1 == null || !p1.equals(p2)){
						equal = false;
						break;
					}
				}
				if(equal){
					return true;
				}
			}
		}
		return false;
	}
	
	public Collection<Event> getRuningEvents(int eventCode){
		List<Event> events = new ArrayList<Event>();
		for(Event e : new ArrayList<Event>(mMapRunningEvent.keySet())){
			if(e.getEventCode() == eventCode){
				events.add(e);
			}
		}
		return events;
	}
	
	public void cancelRunningEvent(int eventCode){
		for(Event e : new ArrayList<Event>(mMapRunningEvent.keySet())){
			if(e.getEventCode() == eventCode){
				e.cancel();
			}
		}
	}
	
	public boolean	isEventRunning(Event e){
		final Event runEvent = mMapRunningEvent.get(e);
		return runEvent != null && !runEvent.isCancel();
	}
	
	public boolean	isEventRunning(int eventCode,Object... params){
		return isEventRunning(new Event(eventCode, params));
	}
	
	public Event getRuningEvent(int eventCode, Object... params){
		return mMapRunningEvent.get(new Event(eventCode, params));
	}
	
	public	void 	addEventListener(int eventCode,OnEventListener listener){
		if(mIsMapListenerLock){
			addToListenerMap(mMapCodeToEventListenerAddCache, eventCode, listener);
		}else{
			addToListenerMap(mMapCodeToEventListener, eventCode, listener);
		}
	}
	
	public void		addEventListenerOnce(int eventCode,final OnEventListener listener){
		addEventListener(eventCode, new OnEventListener() {
			@Override
			public void onEventEnd(Event event) {
				removeEventListener(event.getEventCode(), this);
				if(listener != null){
					listener.onEventEnd(event);
				}
			}
		});
	}
	
	public  void	removeEventListener(int eventCode,OnEventListener listener){
		if(mIsMapListenerLock){
			addToListenerMap(mMapCodeToEventListenerRemoveCache, eventCode, listener);
		}else{
			List<OnEventListener> listeners = mMapCodeToEventListener.get(eventCode);
			if(listeners != null){
				listeners.remove(listener);
			}
		}
	}
	
	public void		clearEventListenerEx(Event e){
		e.clearEventListener();
	}
	
	private void	addToListenerMap(SparseArray<List<OnEventListener>> map,
			int nEventCode,OnEventListener listener){
		List<OnEventListener> listeners = map.get(nEventCode);
		if(listeners == null){
			listeners = new LinkedList<OnEventListener>();
			map.put(nEventCode, listeners);
		}
		listeners.add(listener);
	}
	
	public void		addEventProgressListener(Event e, OnEventProgressListener listener){
		e.addProgressListener(listener);
	}
	
	public void		removeEventProgressListener(Event e, OnEventProgressListener listener){
		e.removeProgressListener(listener);
	}
	
	public void		addEventProgressListener(int code,OnEventProgressListener listener){
		mMapEventCodeToProgressListeners.put(code, listener);
	}
	
	public void		removeEventProgressListener(int code,OnEventProgressListener listener){
		mMapEventCodeToProgressListeners.removeMapping(code, listener);
	}
	
	void			notifyEventProgress(Event e){
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_PROGRESS, e));
	}
	
	protected boolean processEvent(Event event){
		event.addProgressListener(mProgressListener);
		
		try {
			Collection<OnEventRunner> runners = mMapCodeToEventRunner.getCollection(event.getEventCode());
			if(runners != null){
				for(OnEventRunner runner : runners){
					runner.onEventRun(event);
				}
			}
			if(event.isSuccess()){
				event.setProgress(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
			event.setFailException(e);
		} 
		
		return true;
	}
	
	protected void	onEventRunEnd(Event event){
		mMapRunningEvent.remove(event);
		doNotify(event);
	}
	
	private void	doNotify(Event event){
		final List<OnEventListener> eventListeners = event.getEventListeners();
		if(eventListeners != null && eventListeners.size() > 0){
			try{
				for(OnEventListener listener : eventListeners){
					listener.onEventEnd(event);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		mIsMapListenerLock = true;
		List<OnEventListener> list = mMapCodeToEventListener.get(event.getEventCode());
		if(list != null){
			for(OnEventListener listener : list){
				try{
					listener.onEventEnd(event);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		mIsMapListenerLock = false;
		
		if(mMapCodeToEventListenerAddCache.size() > 0){
			int nSize = mMapCodeToEventListenerAddCache.size();
			for(int nIndex = 0;nIndex < nSize;++nIndex){
				int nCode = mMapCodeToEventListenerAddCache.keyAt(nIndex);
				List<OnEventListener> listCache = mMapCodeToEventListenerAddCache.get(nCode);
				if(listCache.size() > 0){
					List<OnEventListener> listeners = mMapCodeToEventListener.get(nCode);
					if(listeners == null){
						listeners = new LinkedList<OnEventListener>();
						mMapCodeToEventListener.put(nCode, listeners);
					}
					listeners.addAll(listCache);
				}
			}
			mMapCodeToEventListenerAddCache.clear();
		}
		if(mMapCodeToEventListenerRemoveCache.size() > 0){
			int nSize = mMapCodeToEventListenerRemoveCache.size();
			for(int nIndex = 0;nIndex < nSize;++nIndex){
				int nCode = mMapCodeToEventListenerRemoveCache.keyAt(nIndex);
				List<OnEventListener> listCache = mMapCodeToEventListenerRemoveCache.get(nCode);
				if(listCache.size() > 0){
					List<OnEventListener> listeners = mMapCodeToEventListener.get(nCode);
					if(listeners != null){
						for(OnEventListener listener : listCache){
							listeners.remove(listener);
						}
					}
				}
			}
			mMapCodeToEventListenerRemoveCache.clear();
		}
	}

	private OnEventProgressListener mProgressListener = new OnEventProgressListener() {
		@Override
		public void onEventProgress(Event e, int progress) {
			Collection<OnEventProgressListener> listeners = mMapEventCodeToProgressListeners.getCollection(e.getEventCode());
			if(listeners != null){
				for(OnEventProgressListener listener : listeners){
					listener.onEventProgress(e, progress);
				}
			}
		}
	};
}
