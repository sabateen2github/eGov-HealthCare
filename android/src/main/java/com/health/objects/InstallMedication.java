package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class InstallMedication {
	
	

	public static class Request
	{
		public int req_type;
	
		public List<GetCondtion.Medication> medications;

	}

	public static boolean InstallMedication (List<GetCondtion.Medication> medications)
	{
		Request req=new Request();
		req.req_type=Types.Type_Install_Medications;
		req.medications=medications;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}






}
