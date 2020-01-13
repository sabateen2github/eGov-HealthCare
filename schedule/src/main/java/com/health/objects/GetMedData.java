/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.objects;

import com.google.gson.Gson;
import com.health.requestHandler.RequestHandler;

/**
 *
 * @author Inspiron
 */
public class GetMedData {
    
    
	public static class Request
	{
		
		public  int req_type;
		public  long med_id;

        }

	public static MedData GetPersonalData(long id)
	{

		Request req=new Request();
		req.req_type=Types.Type_Get_Med_Data;
		req.med_id=id;
		RequestHandler handler=new RequestHandler((new Gson()).toJson(req));
		String json=handler.send();
		if(json==null) return null;
		MedData in=(new Gson().fromJson(json, MedData.class));
		return in;
	}
        
	public static class MedData
        {	
		public String name;
		public long id;
	}
    
    
}
