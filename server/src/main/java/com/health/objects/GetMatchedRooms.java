package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class GetMatchedRooms {
	
	
	
	public static class Request
	{
		
		public  int req_type;
		public  long patient_id;
		public  List<Long> services;
		
	}
        
        
        
	public static List<GetAvailibleRooms.Room> GetMatchedRooms(long patient_id, List<Long> services)
	{
		Request req=new Request();
		req.req_type=Types.Type_GetMatched_Rooms;
		req.patient_id=patient_id;
		req.services=services;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return new ArrayList<>();
		MatchedRooms in=(new Gson().fromJson(json, MatchedRooms.class));
		return in.rooms;
	}
	
	public static class MatchedRooms
	{

		public List<GetAvailibleRooms.Room> rooms;

	}

	


}
