package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

import org.jetbrains.annotations.NonNls;

import java.util.List;


public class InstallSchedules {
	

	public static class Request
	{
		public int req_type;
		public long room_id;
		public List<GetRoomsAppointments.Appointment> schedule;
	}


	public static boolean InstallSchedule (List<GetRoomsAppointments.Appointment> appointments, long room_id)
	{

		Request req=new Request();
		req.req_type=Types.Type_Install_Schedules;
		req.room_id=room_id;
		req.schedule=appointments;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		@NonNls String json=handler.send();
		if(json==null) return false;
		if(json.contains("success")) return true;
		else return false;
	}

}
