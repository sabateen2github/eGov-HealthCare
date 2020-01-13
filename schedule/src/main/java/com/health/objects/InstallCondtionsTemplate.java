package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;

import java.util.List;

public class InstallCondtionsTemplate {
	
	public static class Request
	{
            public int req_type;
	    public List<GetCondtion.Condtion> condtions;
	}



	public static boolean InstallCondtionsTemplate (List<GetCondtion.Condtion> condtions)
	{

		Request req=new Request();
		req.req_type=Types.Type_Install_Condtions;
		req.condtions=condtions;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}
	
	
	
	
	
	
	
	
	
	
	

}
