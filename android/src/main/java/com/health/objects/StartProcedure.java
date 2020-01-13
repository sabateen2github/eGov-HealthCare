package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;



public class StartProcedure {
	
	
	

	public static class Request
	{
		public int req_type;
		public long patient_id;
		public GetProcedure.Procedure procedure;

	}


	public static boolean StartProcedure (long patient_id, GetProcedure.Procedure proc)
	{
		Request req=new Request();
		req.req_type=Types.Type_StartProcedure;
		req.patient_id=patient_id;
		req.procedure=proc;

		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));

		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}


	
}
