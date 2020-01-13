package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;


public class UpdateProcedure {
	

	public static class Request
	{
		public int req_type;
		public long patient_id;
		public long procedure_id;
		public GetProcedure.Procedure procedure;

	}



	public static boolean UpdateProcedure (long patient_id, long proc_id,GetProcedure.Procedure proc)
	{
		Request req=new Request();
		req.req_type=Types.Type_UpdateProcedure;
		req.patient_id=patient_id;
		req.procedure_id=proc_id;
		req.procedure=proc;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}



	
}
