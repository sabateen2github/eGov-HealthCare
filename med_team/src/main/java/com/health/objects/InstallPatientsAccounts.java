package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class InstallPatientsAccounts {
	
	
	public static class Request
	{
		
		public int req_type;
		public List<GetPersonalData.PersonalData> patients;

	}



	public static boolean InstallPatientsAccounts (List<GetPersonalData.PersonalData> list)
	{
		Request req=new Request();
		req.req_type= Types.Type_Install_Accounts_Patients;
		req.patients=list;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}

}
