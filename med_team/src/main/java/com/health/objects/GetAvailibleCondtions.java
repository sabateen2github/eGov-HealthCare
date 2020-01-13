package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;



public class GetAvailibleCondtions {

	public static class Request
	{
		
		public  int req_type;
		
	}

	public static List<GetCondtion.Condtion> getAvailibleCondtions()
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Availible_Condtions;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		Condtions cond=(new Gson().fromJson(json, Condtions.class));
		return cond.condtions;
	}

	public static class Condtions{
		
		public List<GetCondtion.Condtion> condtions;
	}
	

	
	
}
