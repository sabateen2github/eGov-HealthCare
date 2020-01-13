package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class InstallMedTeamAccounts{
	

	public static class Request
	{
		
		public int req_type;
		public List<MedTeam> med_team;
                public List<Long> ids;

	}



	public static boolean InstallMedTeamAccounts (List<MedTeam> list,List<Long> ids)
	{
		Request req=new Request();
		req.req_type= Types.Type_Install_Accounts_Med_Team;
		req.med_team=list;
                req.ids=ids;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}


	
	public static class MedTeam
	{
		
		public String name;
		public String pass;
		public String phone;
		
		
	}

}
