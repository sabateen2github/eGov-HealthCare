package com.health.objects;


import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import java.util.List;

public class SkipTime {

	public static class Request
	{
		public int req_type;
		
		public int seconds;
		public int minutes;
		public int hours;
		public int days;
		public int months;
		public int years;
	}


	public static boolean SkipTime (int sec,int min,int hour ,int day,int month, int year)
	{

		Request req=new Request();
		req.req_type=Types.Type_Skip_Time;

		req.seconds=sec;
		req.minutes=min;
		req.hours=hour;
		req.days=day;
		req.months=month;
		req.years=year;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return false;
		if(json.contains(Strings.SUCCESS)) return true;
		else return false;
	}


}
