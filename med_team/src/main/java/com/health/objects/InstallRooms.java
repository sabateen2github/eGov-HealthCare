package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;


public class InstallRooms {

	
	public static class Request
	{
		public int req_type;
		public List<GetAvailibleRooms.Room> rooms;
	}

	public static boolean InstallProceduresTemplates (List<GetAvailibleRooms.Room> rooms)
	{

		Request req=new Request();
		req.req_type=Types.Type_Install_Rooms;
		req.rooms=rooms;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}

	
	

	
}
