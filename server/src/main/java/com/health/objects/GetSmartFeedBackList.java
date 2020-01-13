package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetSmartFeedBackList {
	
	
	
	public static class Request
	{
		
		public  int req_type;
		public List<Long> services;
		
	}


	public static List<Patient>  GetSmartFeedBackList (List<Long> services)
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Smart_FeedBackList;
		req.services= services;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		Patients in=(new Gson().fromJson(json, Patients.class));
		return in.patients;
	}


	public static class Patients
	{
		
		public List<Patient> patients;
	}
	
	
	public static class Patient
	{
		
		public long patient_id;
		public long procedure_id;
		public long request_id;
		public String name;
		public int age;
		public String date;	
		public int remaining_days;
	}
	

}
