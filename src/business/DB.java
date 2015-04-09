package business;

import java.sql.*;
import java.util.ArrayList;

import models.Line;

public class DB {
	
	private Connection connection = null;
	//创建数据库连接
	public Connection getConnection(){	
		if(connection == null){
			try{
			    Class.forName("com.informix.jdbc.IfxDriver");
				String url = "jdbc:informix-sqli://59.34.58.148:12580/gpsmon_bus:informixserver=;user=sms11;password=gzzr-11sms;DB_LOCALE=zh_cn.gb;CLIENT_LOCALE=zh_cn.gb;DBDATE=mdy4;";
				connection = DriverManager.getConnection(url);
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		}
	   return connection;	   
	}

	
	//取得所有线路信息
	public ArrayList<Line> getLine(){
		ArrayList<Line> list = new ArrayList<Line>();
		try{
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM t_line_stop_forecast"
					+ " INNER JOIN t_line_stations ON  t_line_stop_forecast.lineid=t_line_stations.lineid "
					+ "AND t_line_stop_forecast.stopno= t_line_stations.stopno "
					+ "order by t_line_stations.sequence";
			ResultSet  resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				Line line = new Line();
				line.setDirect(Util.ToDBC(resultSet.getString("direct").trim()));
				line.setLineno(Util.ToDBC(resultSet.getString("lineno").trim()));
				line.setStartstopname(Util.ToDBC(resultSet.getString("startstopname").trim()));
				line.setEndstopname(Util.ToDBC(resultSet.getString("endstopname").trim()));
				line.setStopname(Util.ToDBC(resultSet.getString("stopname").trim()));
				line.setStopnumafter(Util.ToDBC(resultSet.getString("stopnumafter").trim()));
				list.add(line);
			}
			statement.close();
			resultSet.close();
			connection.close();
			connection = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
