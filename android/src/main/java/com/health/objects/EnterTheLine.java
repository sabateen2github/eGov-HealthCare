package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;


public class EnterTheLine {
	
	public static class Request
	{
		public int req_type;
		public long patient_id;
		public long procedure_id;
		public long request_id;

	}


	public static boolean enterTheLine(long proc,long requests,long id)
	{
		Request req=new Request();
		req.req_type=Types.Type_Enter_Line;
		req.patient_id=id;
		req.procedure_id=proc;
		req.request_id=requests;
		String json=(new Gson()).toJson(req);
		RequestHandler handler=new RequestHandler(json);
		String msg=handler.send();
		return msg!=null;
	}





}
