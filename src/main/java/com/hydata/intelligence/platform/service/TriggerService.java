package com.hydata.intelligence.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.transaction.Transactional;

import com.hydata.intelligence.platform.controller.DeviceController;
import com.hydata.intelligence.platform.dto.TriggerModel;
import com.hydata.intelligence.platform.utils.SendMailUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.DeviceTrigger;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.TriggerType;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.model.TriggerModelModel;
import com.hydata.intelligence.platform.repositories.DeviceTriggerRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.TriggerRepository;
import com.hydata.intelligence.platform.repositories.TriggerTypeRepository;

/**
 * @author pyt
 * @createTime 2018年11月5日下午3:29:25
 */
@Transactional
@Service
public class TriggerService {
	@Autowired
	private TriggerRepository triggerRepository;
	
	@Autowired
	private TriggerTypeRepository triggerTypeRepository;
	
	@Autowired
	private DeviceTriggerRepository deviceTriggerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private static Logger logger = LogManager.getLogger(TriggerService.class);


	/**
	 * 添加触发器
	 * @param trigger
	 * @return
	 */
	public JSONObject addTrigger(TriggerModel trigger) {
		Optional<Product> productoptional = productRepository.findById(trigger.getProductId());
		Optional<TriggerType> triggerTypeOptional = triggerTypeRepository.findById(trigger.getTriggerTypeId());
		if(productoptional.isPresent()&&triggerTypeOptional.isPresent()) {		
			trigger.setCreateTime(new Date());
			TriggerModel triggerReturn = triggerRepository.save(trigger);
			logger.debug("触发器保存结束");
			if(triggerReturn!=null) {
				DeviceTrigger deviceTrigger = new DeviceTrigger();
				deviceTrigger.setTriggerId(triggerReturn.getId());
				deviceTrigger.setDeviceId(trigger.getDatastreamId());
				DeviceTrigger deviceTriggerReturn = deviceTriggerRepository.save(deviceTrigger);
				if(deviceTriggerReturn!=null) {
					return RESCODE.SUCCESS.getJSONRES();
				}
				return RESCODE.TRIGGER_DEVICE_ADD_FAILURE.getJSONRES();
			}
			return RESCODE.TRIGGER_ADD_FAILURE.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 根据触发器id删除触发器，
	 * 包括删除触发器设备流关联
	 * @param id
	 * @return
	 */
	public JSONObject delTrigger(Integer id) {
		logger.debug("进入删除触发器");
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(id);
		if(triggerOptional.isPresent()) {
			triggerRepository.deleteById(id);
			int result = deviceTriggerRepository.deleteByTriggerId(id);
			logger.debug("共删除触发器设备流关联数据："+result+"条");
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 修改触发器
	 * @param triggerModel
	 * @return
	 */
	public JSONObject modifyTrigger(TriggerModel triggerModel) {
		Optional<Product> productoptional = productRepository.findById(triggerModel.getProductId());
		Optional<TriggerModel> triggerOptional = triggerRepository.findById(triggerModel.getId());
		if(productoptional.isPresent()&&triggerOptional.isPresent()) {
			triggerModel.setModifyTime(new Date());
			triggerRepository.save(triggerModel);
			return RESCODE.SUCCESS.getJSONRES();
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	
	/**
	 * 查询产品下全部触发器
	 * @param productId
	 * @return
	 */
	public JSONObject getTriggersByProductId(Integer productId) {
		List<TriggerModel> triggerList = triggerRepository.findByProductId(productId);
		JSONArray triggerModelList = new JSONArray();
		for(TriggerModel trigger :triggerList) {
			TriggerModelModel model = new TriggerModelModel();
			Integer triggerId = trigger.getId();
			List<DeviceTrigger> deviceTriggerList = deviceTriggerRepository.findByTriggerId(triggerId);
			model.setTrigger(trigger);
			model.setDeviceTriggerList(deviceTriggerList);
			triggerModelList.add(model);
		}
		return RESCODE.SUCCESS.getJSONRES(triggerModelList);
	}
	
	/**
	 * 获取触发器下已关联设备
	 * @param triggerId
	 * @return
	 */
	public JSONObject getAssociatedDevice(Integer triggerId) {
		return null;
	}

	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月20日上午11:31:11
	 * @description: <触发器触发模块-生产者> 载入数据流
	 * @modified:
	 */
	public  class TriggerProducer implements Runnable {
		private volatile boolean isRunning = true;
		private BlockingQueue<DeviceController.LiveDataStream> queue;// 内存缓冲区
		private AtomicInteger count = new AtomicInteger();// 总数
		private static final int SLEEPTIME = 1000;

		public TriggerProducer(BlockingQueue<DeviceController.LiveDataStream> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			DeviceController.LiveDataStream data = null;
			Random r = new Random();
			//System.out.println("start producting id:" + Thread.currentThread().getId());
			try {
				while (isRunning) {
					Thread.sleep(r.nextInt(SLEEPTIME));
					data = new DeviceController.LiveDataStream(count.incrementAndGet());
					//System.out.println(data + " 加入队列");
					if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
						//System.err.println(" 加入队列失败");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		public void stop() {
			isRunning = false;
		}
	}
	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月20日上午11:34:27
	 * @description: <触发器触发模块-消费者> 判断触发情况，发送警报
	 * @modified:
	 */
	public class TriggerConsumer implements Runnable{
		private BlockingQueue<DeviceController.LiveDataStream> queue;
		private static final int SLEEPTIME = 1000;
		public TriggerConsumer(BlockingQueue<DeviceController.LiveDataStream> queue){
			this.queue = queue;
		}

		@Override
		public void run() {
			//System.out.println("start Consumer id :"+Thread.currentThread().getId());
			Random r = new Random();
			try{
				while(true){
					DeviceController.LiveDataStream data = queue.take();
					if(data != null)
					{
						//达到触发条件

						//发送警报
						//TriggerAlarmProducer email = new TriggerAlarmProducer(queue);
						//service.execute(email);
						//保存数据		DeviceController.SaveDataStream(data);
						//System.out.println();
						Thread.sleep(r.nextInt(SLEEPTIME));
					}
				}
			}catch (InterruptedException e) {
				//e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}


	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月23日上午09:27:11
	 * @description: <触发器警报模块-生产者> 加入警报队列
	 * @modified:
	 */
	public class TriggerAlarmProducer implements Runnable {
		private volatile boolean isRunning = true;
		private BlockingQueue<DeviceController.LiveDataStream> queue;// 内存缓冲区
		private  AtomicInteger count = new AtomicInteger();// 总数
		private static final int SLEEPTIME = 1000;

		public TriggerAlarmProducer(BlockingQueue<DeviceController.LiveDataStream> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			DeviceController.LiveDataStream data = null;
			Random r = new Random();
			//System.out.println("start producting id:" + Thread.currentThread().getId());
			try {
				while (isRunning) {
					Thread.sleep(r.nextInt(SLEEPTIME));
					data = new DeviceController.LiveDataStream(count.incrementAndGet());
					//System.out.println(data + " 加入队列");
					if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
						//System.err.println(" 加入队列失败");
					}
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
				Thread.currentThread().interrupt();
			}

		}

		public void stop() {
			isRunning = false;
		}
	}

	/**
	 * @author: Jasmine
	 * @createTime: 2018年11月23日上午09:17:11
	 * @description: <触发器警报模块-消费者> email警报
	 * @modified:
	 */
	public class TriggerAlarmConsumer implements Runnable{
		private BlockingQueue<DeviceController.LiveDataStream> queue;
		private static final int SLEEPTIME = 1000;
		public TriggerAlarmConsumer(BlockingQueue<DeviceController.LiveDataStream> queue){
			this.queue = queue;
		}

		@Override
		public void run() {
			//System.out.println("start Consumer id :"+Thread.currentThread().getId());
			Random r = new Random();
			try{
				while(true){
					DeviceController.LiveDataStream data = queue.take();
					if(data != null)
					{
						//发送警报
						SendMailUtils.sendMail("jiasimin@hiynn.com", "test", "触发器警报测试");
						//System.out.println();
						Thread.sleep(r.nextInt(SLEEPTIME));
					}
				}
			}catch (InterruptedException e) {
				//e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

	}
}

