package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;

import java.util.List;

public class InstallInsurances {
	

	public static class Request
	{
		public int req_type;
		public List<String> insurances_paths;

	}


	public static boolean InstallInsurances (List<String> insurances)
	{

		Request req=new Request();
		req.req_type=Types.Type_Install_Insurances;
		req.insurances_paths=insurances;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains("success")) return true;
		else return false;
	}



}
