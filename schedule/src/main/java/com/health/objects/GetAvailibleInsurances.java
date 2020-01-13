package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleInsurances {
	


	public static class Request
	{

		public  int req_type;

	}

	public static List<Insurance> getAvailibleInsurances()
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Availible_Insurances;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		Insurances in=(new Gson().fromJson(json, Insurances.class));
		return in.insurances;
	}


	public static class Insurances
	{

		public List<Insurance> insurances;
	}


	public static class Insurance
	{
		public long insurance_id;
		public String insurance_path;
		public float percentage; //0.0 to 1.0
	}
	
	
	

}
