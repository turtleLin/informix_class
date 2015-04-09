package business;

import java.util.*;


import models.Line;
import redis.clients.jedis.*;

public class Redis {
	
  private static JedisPool pool = null;
  
  public static JedisPool getPool(){
	  if(pool == null){
		  JedisPoolConfig config = new JedisPoolConfig();
		  config.setMaxTotal(500);
		  config.setMaxIdle(5);
		  config.setMaxWaitMillis(100*1000);
		  config.setTestOnBorrow(true);
		  pool = new JedisPool(config,"localhost");
	  }
	  return pool;
  }
  
  public void setLines(ArrayList<Line> list){
	  JedisPool pool = null;
	  Jedis jedis = null;
	  try{
		  pool = getPool();
		  jedis = pool.getResource();
		  jedis.hset("4-1H", "市一中新校", "0");
		  jedis.hset("8-1H", "肇庆牌坊公园", "0");
		  jedis.hset("201-1H", "肇庆牌坊公园", "0");
		  jedis.hset("201-2H", "大旺客运站", "0");
		  jedis.hset("201-2H", "贺华站", "0");
		  jedis.hset("202-1H", "肇庆牌坊公园", "0");
		  jedis.hset("203-1H", "肇庆牌坊公园", "0");
		  jedis.hset("203-2H", "肇庆牌坊公园", "0");
		  jedis.hset("204-1H", "肇庆牌坊公园", "0");
		  jedis.hset("K01-1H", "肇庆牌坊公园", "0");
		  jedis.hset("31-1H", "台湾城", "0");
		  for(Line line:list){
			  int direct = Integer.parseInt(line.getDirect()) % 2;
			  if(direct == 0) direct = 2;
			 String lineKey = line.getLineno() + "-" + direct ;
			 String  lineKeyH = lineKey + "H";
			 jedis.hset(lineKeyH,"start",line.getStartstopname());
			 jedis.hset(lineKeyH, "end", line.getEndstopname());
			switch(line.getStopname())
			{
				case "外坑":
					jedis.hset(lineKeyH,"台湾城",line.getStopnumafter());
					break;
				case "港航局三公司":
					jedis.hset(lineKeyH,"铁路区",line.getStopnumafter());
					break;
				case "中移动肇庆分公司":
					if(lineKeyH.equals("27-1H"))
					{
						jedis.hset(lineKeyH,"君安花苑",line.getStopnumafter());
					}
					else
					{
						jedis.hset(lineKeyH,line.getStopname(),line.getStopnumafter());
					}
					break;
				case "龟顶新城":
					jedis.hset(lineKeyH,"光大锦绣山河东",line.getStopnumafter());
					break;
				case "南门":
					jedis.hset(lineKeyH, "人民南",line.getStopnumafter());
					break;
				case "管桩厂":
					 jedis.hset(lineKeyH,"水木天娇",line.getStopnumafter());
					 break;
				case "市一医院":
					jedis.hset(lineKeyH,"旧市一医院",line.getStopnumafter());
					 break;
				case "电力大楼":
					jedis.hset(lineKeyH,"华生广场",line.getStopnumafter());
					 break;
				case "市交警支队":
					break;
				case "蓝塘路北":
					jedis.hset(lineKeyH,"蓝塘北路口",line.getStopnumafter());
					 break;
					default:
						 jedis.hset(lineKeyH,line.getStopname(),line.getStopnumafter());
			}
		  }	 
		  jedis.hset("31-1H", "start", "台湾城");
		  jedis.hset("4-1H", "start", "市一中新校");
		  jedis.hset("31-2H", "台湾城", "0");
		  jedis.hset("31-2H", "end", "台湾城");
		  jedis.hset("17-1H", "start", "市二医院");
		  jedis.hset("3-1H", "start", "人民南");
		  jedis.hset("3-2H", "end", "人民南");
	  }catch(Exception e){
		  pool.returnBrokenResource(jedis);
		  e.printStackTrace();
	  }
	  pool.returnResource(jedis);
  }
 
  
  public void setBuses(ArrayList<Line> list){
	  JedisPool pool = null;
	  Jedis jedis = null;
	  try{
		  pool = getPool();
		  jedis = pool.getResource();
		  for(Line line:list){
			  String busKey;
			  if(Integer.parseInt(line.getDirect()) % 2 == 0){
				   busKey = line.getStopname() + "-" + 2;
			  }else{
				   busKey = line.getStopname() + "-" + 1;
			  }
		  jedis.lpush(busKey, line.getLineno());	
		  }
	  }catch(Exception e){
		  pool.returnBrokenResource(jedis);
		  e.printStackTrace();
	  }
	  pool.returnResource(jedis);
  }
  
  public void setLineInformation(){
	  JedisPool pool = null;
	  Jedis jedis = null;
	  try{
		  pool = getPool();
		  jedis = pool.getResource();
		  String lines = "1,2,3,4,5,5B,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,25B,26,27,28,29,30,31,201,202,203,204,205,K01,K02";
		  String array[] = lines.split(",");
		  for(String v : array){
			  String key = v + "-1H";
			  List<String> list = jedis.hmget(key, "start","end");
			  jedis.rpush("lines", v,list.get(0),list.get(1));
		  }
	  }catch(Exception e){
		  pool.returnBrokenResource(jedis);
		  e.printStackTrace();
	  }
	  pool.returnResource(jedis);
  }
  
  
  public void flushdb(){
	  JedisPool pool = null;
	  Jedis jedis = null;
	  try{
		  pool = getPool();
		  jedis = pool.getResource();
		  jedis.flushDB();
	  }catch(Exception e){
		  pool.returnBrokenResource(jedis);
		  e.printStackTrace();
	  }
	  pool.returnResource(jedis);
  }
  
}
