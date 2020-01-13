package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetActiveCondtions {
	
	
	public static class Request
	{
		
		public  int req_type;
		public  long patient_id;
		
	}

	public static List<GetCondtion.Condtion> GetActiveCondtions(long patient_id)
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Active_Cond;
		req.patient_id= patient_id;

		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		Condtions cond=(new Gson().fromJson(json, Condtions.class));
		return cond.condtions;
	}
	public static class Condtions{
		
		
		public List<GetCondtion.Condtion> condtions;
	}
	
	
	

}
