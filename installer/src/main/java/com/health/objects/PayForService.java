package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;


public class PayForService {


	public static class Request
	{
		public int req_type;
		public long patient_id;
		public long procedure_id;
		public long requests;


	}

	public static boolean PayForService (long proc_id,long requests,long id)
	{

		Request req=new Request();
		req.req_type=Types.Type_Pay_Service;
		req.patient_id= id;
		req.procedure_id=proc_id;
		req.requests=requests;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains("success")) return true;
		else return false;
	}


}
