package com.epay.xj.test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchTest {
	
	public static List<A> getData(){
		List<A> lst = new ArrayList<A>();
		A a = new A();
		a.setId(1);
		a.setCode("1");
		a.setMoney(10);
		lst.add(a);
		a = new A();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		a.setId(2);
		a.setCode("1");
		a.setMoney(300);
		lst.add(a);
		a = new A();
		a.setId(3);
		a.setCode("1");
		a.setMoney(500);
		lst.add(a);
		a = new A();
		a.setId(4);
		a.setCode("0");
		a.setMoney(300);
		lst.add(a);
		a = new A();
		a.setId(5);
		a.setCode("1");
		a.setMoney(100);
		lst.add(a);
		a = new A();
		a.setId(6);
		a.setCode("0");
		a.setMoney(500);
		lst.add(a);
		a = new A();
		a.setId(7);
		a.setCode("0");
		a.setMoney(100);
		lst.add(a);
		a = new A();
		a.setId(8);
		a.setCode("1");
		a.setMoney(400);
		lst.add(a);
		a = new A();
		a.setId(9);
		a.setCode("0");
		a.setMoney(400);
		lst.add(a);
		a = new A();
		a.setId(10);
		a.setCode("1");
		a.setMoney(100);
		lst.add(a);
		a = new A();
		a.setId(11);
		a.setCode("0");
		a.setMoney(1000);
		lst.add(a);
		return lst;
	}
	public static void main(String[] args) throws Exception, JsonMappingException, IOException {
		List<A> lst = getData();
		System.out.println(JSON.toJSONString(lst));
//		StringWriter str=new StringWriter();
//		ObjectMapper om = new ObjectMapper();
//		om.writeValue(str, lst);
//		System.out.println(str);
//		Map<String,Object> point1 = new HashMap<String,Object>();
//		search(lst, point1);
		Point point = new Point();
		search(lst, point);
//		StringWriter str1=new StringWriter();
//		ObjectMapper om1 = new ObjectMapper();
//		om1.writeValue(str1, point);
//		System.out.println(str1);
//		StringWriter str2=new StringWriter();
//		ObjectMapper om2 = new ObjectMapper();
//		om1.writeValue(str2, lst);
//		System.out.println(str2);
//		for(Map.Entry<String, Object> entry:point1.entrySet()){
//			System.out.println("key:"+entry.getKey()+",value:"+entry.getValue());
//		}
	}
	
	
	public static void  search(List<A> lst,Point point){
		//是否找到成功标识
		for (int i=0;i<lst.size();i++) {
			A a = lst.get(i);
			if(!a.getCode().equals("0")){
				if(point.getCurrent()==null){//如果当前没有失败的，则线下寻找失败记录
					point.setCurrent(a);//当前记录为失败记录的开始
					lst.remove(i);//在list中移除当前值，寻找下一次的成功记录
				}
			}else{
				//成功情况指针下探，就说明没有找到第一条逾期的开始记录，
				if(point.getCurrent()==null || point.getCurrent().getMoney()==a.getMoney())continue;
				int tms = point.getTimes();
					point.setTimes(++tms);
					System.out.println(point.getCurrent().getId());
				lst.remove(i);
				point.setCurrent(null);
				search(lst, point);
			}
		}
	}
	public static void  search(List<A> lst,Map<String,Object> point){
		int flg = 0;
		int times = 0;
		if(point.containsKey("times")){               
			times = Integer.valueOf(point.get("times").toString());
		}
		A overDue = null;
		if(point.containsKey("nextOverDue")){
			overDue = (A) point.get("nextOverDue");
			flg = overDue.getId();
		}
		for (int i=0;i<lst.size();i++) {
			A a = lst.get(i);
			if(!a.getCode().equals("0")){//逾期开始
				if(flg!=0){
					//查看当前是否有金额不行同的作为下一次逾期的开始
					if(overDue.getMoney()!=a.getMoney()){
						if(!point.containsKey("nextOverDue")){
							point.put("nextOverDue", a);
						}
					}
					continue;
				}
				overDue = a;
				flg = a.getId();
			}
			if(overDue!=null){
				if(a.getCode().equals("0")){
					//口径1条件成立
					if(a.getMoney()==overDue.getMoney()){
						//计算逾期天数
						times ++;  
						if(!point.containsKey("nextOverDue")){
							point.put("nextOverDue", String.valueOf(a.getId()));
						}
						point.put("times", String.valueOf(times));
						System.out.println(i+"-------------"+overDue.getId());
						List<A> tmpList = lst.subList(i, lst.size()-1);
						search(tmpList, point);
					}
				}
			}
		}
	}

}
