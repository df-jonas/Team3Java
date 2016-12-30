package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import controller.APIController.APIUrl;
import controller.APIController.RequestType;
import services.APIRequest;
import services.APIThread;

public abstract class BaseDAO
{
	private Connection connection;
	
	protected HashMap<String, String> params;
	protected Boolean isSyncFunction;

	protected Connection getConnection()
	{
		return connection;
	}

	protected void setConnection(Connection connection)
	{
		this.connection = connection;
	}

	public BaseDAO()
	{
		this.isSyncFunction = false;
		try {
			setConnection(DatabaseSingleton.getDatabaseSingleton().getConnection(true));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void syncMainDB(String url, RequestType requestType, HashMap<String, String> params)
	{
		APIRequest request = new APIRequest(UUID.randomUUID(),APIUrl.G3, url, requestType, params, false);
		APIThread apiThread = APIThread.getThread();
		apiThread.addAPIRequest(request);
	}
	
	public void setSyncFunction()
	{
		this.isSyncFunction = true;
	}
}

