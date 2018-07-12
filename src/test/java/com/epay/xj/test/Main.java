package com.epay.xj.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) throws Exception {
        // 开始时间
        long start = System.currentTimeMillis();
        // 模拟数据List
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 10000; i++) {
            list.add(i + "");
        }
        // 每500条数据开启一条线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(2);
        System.out.println("线程处理数据："+threadSize+",dataSize："+dataSize+",threadNum："+threadNum);
        // 定义一个任务集合
        List<Callable<List<Integer>>> tasks = new ArrayList<Callable<List<Integer>>>();
        Callable<List<Integer>> task = null;
        List<String> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<String> listStr = cutList;
            task = new Callable<List<Integer>>() {
                @Override
                public List<Integer> call() throws Exception {
                	List<Integer> lst = new ArrayList<Integer>();
                	System.out.println("集合大小:"+listStr.size());
                    for (String string : listStr) {
                    	System.out.println("线程："+Thread.currentThread().getName()+"content："+string);
                    	lst.add(Integer.valueOf(string));
                    	
//                    	Thread.currentThread().sleep(10);
					}
                    return lst;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<List<Integer>>> results = exec.invokeAll(tasks);
        for (Future<List<Integer>> future : results) {
        	List<Integer> lst = future.get();
        	System.out.println("size:"+lst.size());
//        	for (Integer o : lst) {
//        		System.out.println("threadName:"+Thread.currentThread().getName()+":"+o);
//			}
        }
        // 关闭线程池
        exec.shutdown();
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
