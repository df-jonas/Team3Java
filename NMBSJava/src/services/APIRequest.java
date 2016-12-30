package services;

import java.util.HashMap;
import java.util.UUID;

import controller.APIController;
import controller.APIController.APIUrl;
import controller.APIController.RequestType;

public class APIRequest
{
	private APIController api;
	private UUID requestID;
	private boolean metResult;
	
	public APIRequest(UUID requestID, APIUrl apiUrl, String url, RequestType requestType, HashMap<String, String> params)
	{
		this.api = new APIController(apiUrl, url, requestType, params);
		this.requestID = requestID;
		this.metResult = true;
	}
	
	public APIRequest(UUID requestID, APIUrl apiUrl, String url, RequestType requestType, HashMap<String, String> params, Boolean metResult)
	{
		this.api = new APIController(apiUrl, url, requestType, params);
		this.requestID = requestID;
		this.metResult = metResult;
	}
	
	public UUID getRequestID()
	{
		return requestID;
	}

	public APIController getApi()
	{
		return api;
	}

	public boolean isMetResult()
	{
		return metResult;
	}
	
}
