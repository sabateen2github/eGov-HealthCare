package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleMedications {
	

	public static class Request
	{
		
		public  int req_type;
		
	}

	public static List<GetCondtion.Medication> getAvailibleMedications()
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Availible_Medications;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		Medications in=(new Gson().fromJson(json, Medications.class));
		return in.medications;
	}


	public static class Medications
	{
		public List<GetCondtion.Medication> medications;
	}

	
	

}
