package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;

public class SmartFeedBackDoneRequest {

	
	

	public static class Request
	{
		
		public  int req_type;
		public  long patient_id;
		public  long procedure_id;
		public  long request_id;

		
	}

	public static boolean SmartFeedBackDoneRequest (long patient_id,long procedure_id,long req_id)
	{

		Request req=new Request();
		req.req_type=Types.Type_Smart_Done_Request;
		req.patient_id=patient_id;
		req.procedure_id=procedure_id;
		req.request_id=req_id;

		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains("success")) return true;
		else return false;
	}


}
