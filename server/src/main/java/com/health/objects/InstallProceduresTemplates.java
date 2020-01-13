package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class InstallProceduresTemplates {
	
	
	
	public static class Request
	{
            public int req_type;
	    public List<GetProcedure.Procedure> procedures;

	}


	public static boolean InstallProceduresTemplates (List<GetProcedure.Procedure> procedures)
	{
            
            
		Request req=new Request();
		req.req_type=Types.Type_Install_Procedures;
		req.procedures=procedures;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}

	

}
