package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datapoint;
import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.model.MailBean;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DatapointRepository;
import com.hydata.intelligence.platform.cell_link.repository.DatastreamRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DatapointService
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/5 17:10
 * @Version
 */
@Service
@Transactional
public class DatapointService {
    @Autowired
    private DatapointRepository datapointRepository;
    @Autowired
    private DatastreamRepository datastreamRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DatastreamService datastreamService;

    private static Logger logger = LogManager.getLogger(DatapointService.class);

    //保存数据队列
    private static BlockingQueue<JSONObject> queue = new ArrayBlockingQueue<JSONObject>(2000);
    //数据存储状态
    private static boolean isRunning = false;
    //超时时间间隔
    private static long TIMEOUT_INTERVAL = 3*60*1000;

    public void dealWithData(Long device_id, JSONArray data){
        JSONObject object = new JSONObject();
        object.put("deviceId",device_id);
        object.put("data",data);
        boolean rsp = false;
        try {
            //阻塞3秒
            rsp = queue.offer(object, 3, TimeUnit.SECONDS);
            if(!rsp){
                throw new Exception("服务器繁忙，请稍后再发");
            }
            if(!isRunning){
                startup();
                isRunning = true;
            }
        } catch (Exception e) {
            logger.error("添加数据队列出错", e);
        }
        if (rsp) logger.info("数据储存成功");
        else logger.info("数据储存失败");
    }

    private void startup(){
        if (isRunning){
            return;
        }
        Thread th = new  Thread(){
            @Override
            public void run(){
                logger.debug("存储数据线程启动");
                long timestamp = System.currentTimeMillis();
                while(isRunning)
                {
                    long timeout = timestamp + TIMEOUT_INTERVAL;
                    if(System.currentTimeMillis()>timeout)
                    {
                        //空跑一段时间(3分钟)后线程退出
                        break;
                    }
                    try
                    {
                        if(queue.size()==0)
                        {
                            Thread.sleep(1000);
                            continue;
                        }
                        timestamp = System.currentTimeMillis();
                        JSONObject object = queue.poll();
                        assert object != null;
                        dealWithDatas(object);
                        //休息2s
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        logger.error("数据存储线程出错",e);
                    }
                }
                isRunning = false;
                logger.debug("数据存储线程停止。");
            }
        };
        th.start();
    }

    private void dealWithDatas(JSONObject object1) {
        Long deviceId = (Long) object1.get("deviceId");
        JSONArray data = (JSONArray) object1.get("data");
        logger.debug("进入dealWithData处理数据");
/*        logger.debug(data.toJSONString());
        logger.info("设备编号为：" + deviceId);*/
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            List<Datastream> datastreamList = datastreamRepository.findListByDeviceAndDatastreamName(deviceId,"");
            //1.获取设备编号：deviceSn下全部数据流名称deviceDatastreamName
//            logger.info(datastreamList.size());
            //2.上传数据中未存入的数据流存入
//            logger.info(data.size());
            for (Object datum : data) {
                JSONObject object = (JSONObject) datum;
                synchronized (object) {
                    JSONArray names = new JSONArray();
                    for (Datastream ds : datastreamList) {
                        String datastreamName = ds.getDatastreamName();
                        names.add(datastreamName);
                    }
                    if (!names.contains(object.getString("dm_name"))) {
                        Datastream datastream = new Datastream();
                        datastream.setDevice(device);
                        datastream.setDatastreamName(object.getString("dm_name"));
                        datastreamService.add(datastream);
                        names.add(object.getString("dm_name"));
                    }
                }
                Optional<Datastream> datastreamOptional = datastreamRepository.findByDeviceAndDatastreamName(
                        deviceId,object.getString("dm_name"));
                if (datastreamOptional.isPresent()){
                    Datastream datastream = datastreamOptional.get();
                    Datapoint datapoint = new Datapoint();
                    datapoint.setDatastream(datastream);
                    datapoint.setDeviceId(datastream.getDevice().getDeviceId());
                    datapoint.setDatastreamId(datastream.getDatastreamId());
                    datapoint.setDatastreamName(datastream.getDatastreamName());
                    datapoint.setCreated(StringUtil.getDate(object.getString("time")));
                    Float value = object.getFloatValue("value");
                    datapoint.setValue(value);
                    datapointRepository.save(datapoint);
                }
            }
        }
    }
}
