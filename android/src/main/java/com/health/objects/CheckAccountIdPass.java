package com.health.objects;


import android.util.Log;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

public class CheckAccountIdPass {

	public static class Request
	{
		public int req_type;
		public long id;
		public String pass;
		public int account_type;
	}

	public static boolean CheckPassID(long id,String pass,int acctype)
	{


		Request req=new Request();
		req.req_type=Types.Type_Check_IDPASS;
		req.id=id;
		req.pass=pass;
		req.account_type=acctype;
		String json=(new Gson()).toJson(req);
		RequestHandler handler=new RequestHandler(json);
		String msg=handler.send();

		if (msg==null)return false;
		if(msg.contains(Strings.LET_PASS)) return true;
		else return  false;
	}




}
