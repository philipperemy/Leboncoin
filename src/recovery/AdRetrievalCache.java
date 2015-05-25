package recovery;

import java.util.HashMap;
import java.util.Map;

import log.Logger;

public class AdRetrievalCache {

	public Map<String, String> cacheById = new HashMap<>();
	
	public String getById(String id)
	{
		String content = cacheById.get(id);
		String status = (content == null) ? "MISS" : "HIT";
		Logger.traceINFO("Query cache with id : " + id + ", status : " + status);
		return content;
	}
	
	public void updateId(String id, String content)
	{
		Logger.traceINFO("Update cache with id : " + id);
		cacheById.put(id, content);
	}
	
}
