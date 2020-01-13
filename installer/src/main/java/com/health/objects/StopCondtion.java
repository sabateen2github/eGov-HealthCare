package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;

public class StopCondtion {
	
	public static class Request
	{
		public int req_type;
		public long patient_id;
		public long condtion_id;

	}


	public static boolean StopCondtion (long patient_id, long cond_id)
	{
		Request req=new Request();
		req.req_type= Types.Type_StopCondtin;
		req.patient_id=patient_id;
		req.condtion_id=cond_id;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains("success")) return true;
		else return false;
	}

}
