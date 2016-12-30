package services;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.json.JSONArray;

public class APIThread extends Thread
{
	private static APIThread apiThread;
	private static Queue<APIRequest> workQueue;
	private static HashMap<UUID, ThreadListener> threadListeners;
	
	
	private APIThread()
	{
		super("api-thread");
		workQueue  = new LinkedList<APIRequest>();
		threadListeners = new HashMap<UUID, ThreadListener>();
	}
	
	public static APIThread getThread()
	{
		if (apiThread == null || !apiThread.isAlive())
			apiThread = new APIThread();
		return apiThread;
	}

	public void addAPIRequest(APIRequest newRequest)
	{
		synchronized (workQueue)
		{
			workQueue.add(newRequest);
			
			if (!this.isAlive())
				this.start();
		}
	}
	
	public void addListener(UUID requestID, ThreadListener listener)
	{
		synchronized (threadListeners)
		{
			threadListeners.put(requestID, listener);
		}
	}
	
	private ThreadListener getListener(UUID requestID)
	{
		return threadListeners.get(requestID);
	}

	@Override
	public void run()
	{
		while(!workQueue.isEmpty())
		{
			try {
				APIRequest r = workQueue.poll();
				JSONArray apiResult = r.getApi().getJsonResult();
				if (r.isMetResult())
				{
					getListener(r.getRequestID()).setResult(apiResult);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public APIThread(APIUrl apiUrl, String url, RequestType requestType, HashMap<String, String> params)
//	{
//		super();
//		this.api = new APIController(apiUrl, url, requestType, params);
//		this.metResult = true;
//	}
//	
//	public APIThread(APIUrl apiUrl, String url, RequestType requestType, HashMap<String, String> params, Boolean metResult)
//	{
//		super();
//		this.api = new APIController(apiUrl, url, requestType, params);
//		this.metResult = metResult;
//	}
	
//	public APIController getAPIController()
//	{
//		return this.api;
//	}

}
