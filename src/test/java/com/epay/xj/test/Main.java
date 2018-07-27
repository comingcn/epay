package com.epay.xj.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	// 参数初始化
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	// 核心线程数量大小
	private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
	// 线程池最大容纳线程数
	private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
	// 线程空闲后的存活时长
	private static final int keepAliveTime = 30;
	static AtomicInteger mCount = new AtomicInteger(0);

	static class ListRunnable implements Callable {

		Logger logger = LoggerFactory.getLogger(getClass());

		private List<Integer> list;

		public ListRunnable(List<Integer> list) {
			super();
			this.list = list;
		}
		@Override
		public Object call() throws Exception {
			synchronized (this) {
				try {
					for (Integer integer : list) {
						System.out.println(integer + "__________________" + mCount.getAndIncrement());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public static void mtest() {
		// 任务过多后，存储任务的一个阻塞队列
		BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
		// 线程的创建工厂
		ThreadFactory threadFactory = new ThreadFactory() {
			private final AtomicInteger mCount = new AtomicInteger(1);
			
			public Thread newThread(Runnable r) {
				return new Thread(r, "yanghaifeng.AsyncTask #" + mCount.getAndIncrement());
			}
		};
		System.out.println("CPU_COUNT:" + CPU_COUNT + ",corePoolSize:" + corePoolSize + ",maximumPoolSize:" + maximumPoolSize);
		// 线程池任务满载后采取的任务强行策略
		RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		// 线程池对象，创建线程
		ThreadPoolExecutor execute = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				TimeUnit.SECONDS, workQueue, threadFactory, rejectHandler);
		// 做任务
		doWork(threadFactory, execute);
		
		execute.shutdown();
		 while(true){  
	           if(execute.isTerminated()){  
	                System.out.println("所有的子线程都结束了！");  
	                break;  
	            }  
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
	        }
	}

	public static void doWork(final ThreadFactory threadFactory, final ThreadPoolExecutor execute) {
		// 模拟数据List
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <= 20000; i++) {
			list.add(i);
		}
		// 每500条数据开启一条线程
		int threadSize = 1000;
		// 总数据条数
		int dataSize = list.size();
		// 线程数
		int threadNum = dataSize / threadSize + 1;
		// 定义标记,过滤threadNum为整数
		boolean special = dataSize % threadSize == 0;
		List<Integer> cutList = null;
		for (int i = 0; i < threadNum; i++) {
			if (i == threadNum - 1) {
				if (special) {
					break;
				}
				cutList = list.subList(threadSize * i, dataSize);
			} else {
				cutList = list.subList(threadSize * i, threadSize * (i + 1));
			}
			execute.submit(new ListRunnable(cutList));
		}
	}

	public static void test() {
		final BatchService bs = new BatchService();
		// 模拟数据List
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <= 200000; i++) {
			list.add(i);
		}
		// 每threadSize条数据开启一条线程
		int threadSize = 100;
		// 总数据条数
		int dataSize = list.size();
		// 线程数
		int threadNum = dataSize / threadSize + 1;
		// 定义标记,过滤threadNum为整数

		boolean special = dataSize % threadSize == 0;
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadNum);
		List<Integer> cutList = null;
		for (int i = 0; i < threadNum; i++) {
			if (i == threadNum - 1) {
				if (special) {
					break;
				}
				cutList = list.subList(threadSize * i, dataSize);
			} else {
				cutList = list.subList(threadSize * i, threadSize * (i + 1));
			}
			final List<Integer> listStr = cutList;
			fixedThreadPool.submit(new Runnable() {
				public void run() {
					List<Integer> lst = new ArrayList<Integer>();
					// System.out.println("集合大小:"+listStr.size());
					for (Integer string : listStr) {
						lst.add(string);
					}
					System.out.println("集合大小:" + listStr.size());
					bs.addList(lst);
				}
			});
		}
		fixedThreadPool.shutdown();
	}

	public static void main(String[] args) throws Exception {
		// final BatchService bs = new BatchService();
		mtest();
		// 开始时间
		long start = System.currentTimeMillis();
		// test();

		// // 模拟数据List
		// List<Integer> list = new ArrayList<Integer>();
		// for (int i = 0; i <= 10000000; i++) {
		// list.add(i);
		// }
		// // 每500条数据开启一条线程
		// int threadSize = 1000000;
		// // 总数据条数
		// int dataSize = list.size();
		// // 线程数
		// int threadNum = dataSize / threadSize + 1;
		// // 定义标记,过滤threadNum为整数
		// boolean special = dataSize % threadSize == 0;
		// // 创建一个线程池
		// ExecutorService exec = Executors.newFixedThreadPool(threadNum);
		// System.out.println("线程处理数据："+threadSize+",dataSize："+dataSize+",threadNum："+threadNum);
		// // 定义一个任务集合
		// List<Callable<List<Integer>>> tasks = new
		// ArrayList<Callable<List<Integer>>>();
		// Callable<List<Integer>> task = null;
		// List<Integer> cutList = null;
		// // 确定每条线程的数据
		// for (int i = 0; i < threadNum; i++) {
		// if (i == threadNum - 1) {
		// if (special) {
		// break;
		// }
		// cutList = list.subList(threadSize * i, dataSize);
		// } else {
		// cutList = list.subList(threadSize * i, threadSize * (i + 1));
		// }
		// // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
		// final List<Integer> listStr = cutList;
		// task = new Callable<List<Integer>>() {
		// @Override
		// public List<Integer> call() throws Exception {
		// List<Integer> lst = new ArrayList<Integer>();
		//// System.out.println("集合大小:"+listStr.size());
		// for (Integer string : listStr) {
		//// System.out.println("线程："+Thread.currentThread().getName()+"content："+string);
		// lst.add(string);
		//
		//// Thread.currentThread().sleep(10);
		// }
		//// String tname = Thread.currentThread().getName();
		//// System.out.println("线程："+tname);
		// System.out.println("集合大小:"+listStr.size());
		// bs.addList(lst);
		// return null;
		// }
		// };
		// // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
		// tasks.add(task);
		// }
		// List<Future<List<Integer>>> results = exec.invokeAll(tasks);
		//// for (Future<List<Integer>> future : results) {
		//// List<Integer> lst = future.get();
		//// System.out.println("size:"+lst.size());
		//// for (Integer o : lst) {
		//// System.out.println("threadName:"+Thread.currentThread().getName()+":"+o);
		//// }
		//// }
		// // 关闭线程池
		// exec.shutdown();
		System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
	}
}
