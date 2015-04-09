package controller;
import java.util.ArrayList;
/*import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;*/

import models.Line;
import business.DB;
import business.Redis;

public class Task_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 DB db = new DB();
	      ArrayList<Line> list = db.getLine();
		  Redis redis = new Redis();
		  redis.flushdb();
	      redis.setLines(list);
		  redis.setBuses(list);
		  redis.setLineInformation();
		  
		/*  try{
		  File f = new File("log.txt");
		  Date nowTime=new Date();
		  SimpleDateFormat time=new SimpleDateFormat("yyyy MM dd HH mm ss");
		  FileOutputStream fileOutputStream = new FileOutputStream(f,true);
	      PrintStream printStream = new PrintStream(fileOutputStream);
	      System.setOut(printStream);
	      System.out.println(time.format(nowTime)); 
	      System.out.println("task1 run");
	  }catch(Exception e){
		  System.out.println(e);
	  }*/
	}

}
