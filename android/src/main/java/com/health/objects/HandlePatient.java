package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;



public class HandlePatient {

	
	

	
	public static class Request
	{
		
		public int req_type;
		public long patient_id;
		public long procedure_id;
		public Long request_id;
		public long room_id;
		public long appointment_id;
	}

	public static boolean HandlePatient (long patient_id,long proedure_id,long room_id,long appointment_id,Long request)
	{

		Request req=new Request();
		req.req_type=Types.Type_Handle_Patient;
		req.appointment_id= appointment_id;
		req.patient_id=patient_id;
		req.procedure_id=proedure_id;
		req.request_id=request;
		req.room_id=room_id;
                
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}


}
