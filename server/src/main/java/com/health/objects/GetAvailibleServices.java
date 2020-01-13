package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetAvailibleServices {
	


	public static class Request
	{
		
		public  int req_type;

	}


	public static List<Service> getAvailibleServices()
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Availible_Services;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		Services in=(new Gson().fromJson(json, Services.class));
		return in.services;
	}
	
	public static class Services
	{

		public List<Service> services;
	}
	
	public static class Service
	{
		public long service_id;
		public String service_path;
		public float service_price;
		public List<GetAvailibleInsurances.Insurance> insurances;
	}




}
