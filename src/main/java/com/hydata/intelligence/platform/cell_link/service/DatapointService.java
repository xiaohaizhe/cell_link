package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datapoint;
import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DatapointRepository;
import com.hydata.intelligence.platform.cell_link.repository.DatastreamRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.MqttUtils;
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
        logger.info(object1);
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
                Optional<Datastream> datastreamOptional =null;
                synchronized (object) {
                    JSONArray names = new JSONArray();
                    for (Datastream ds : datastreamList) {
                        String datastreamName = ds.getDatastreamName();
                        names.add(datastreamName);
                    }
                    if (!names.contains(object.getString("dm_name"))) {
                        String dmName = object.getString("dm_name");
                        Datastream datastream = new Datastream();
                        datastream.setDevice(device);
                        datastream.setDatastreamName(dmName);
                        datastreamService.add(datastream);
                        names.add(dmName);
                        datastreamOptional = datastreamRepository.findByDeviceAndDatastreamName(
                                deviceId,object.getString("dm_name"));
                    }
                }

                if (datastreamOptional !=null && datastreamOptional.isPresent()){
                    Datastream datastream = datastreamOptional.get();
                    Datapoint datapoint = new Datapoint();
                    datapoint.setStatus(0);
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
    /**
     * 解析设备上传的数据流
     * regCode：设备注册码
     * 1.存储数据流
     * 2.存储数据
     * 3.触发
     *
     * @param jsonObject 上传的数据流信息
     */
    public JSONObject resolveDatapoint(Long id, JSONObject jsonObject) {
        logger.info("设备" + id + "发来了http实时信息：" + jsonObject);
        //jsonObject
        //检查设备鉴权码
        boolean isHttp = false;
        Optional<Device> device = deviceRepository.findById(id);
        if (device.isPresent()) {
            DeviceGroup dg = device.get().getDeviceGroup();
            if(dg.getProtocol().getProtocolName().equals("HTTP")){
                isHttp = true;
            }
        }
/*
        List<Product> products = productRepository.findByProtocolId(1);
        for (Product product : products) {
            Optional<Device> device = deviceRepository.findByProductIdandId(product.getId(),id);
                if (device.isPresent()) {
                    isHttp = true;
                    break;
                }
        }*/
        logger.info("HTTP新信息开始处理，设备注册码已找到：" + isHttp);
        if (isHttp) {
            try {
                dataPointHandler(id, jsonObject);
            } catch (Exception e) {
                return RESCODE.FAILURE.getJSONRES("HTTP数据解析失败" + e);
            }
        } else {
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
        }
        return RESCODE.SUCCESS.getJSONRES();

    }

    /**
     * 解析实时数据流
     *
     * @param data
     * @return http实时数据流格式
     * {
     * "device_sn":"123456",
     * "datastreams": [
     * {
     * "dm_name": "temperature", //数据流名称或数据流模板名称
     * "value": 42 //上传数据点值
     * },
     * {
     * "dm_name": "humid", //数据流名称或数据流模板名称
     * "value": 35 //上传数据点值
     * },
     * {…}
     * ]
     * }
     */
    public void dataPointHandler(Long id, JSONObject data) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        JSONArray result = new JSONArray();
        MqttUtils.getHttpCachedThreadPool().execute(() -> {
            //解析数据
            try {
                Date date1 = new Date();
                String arrtime = sdf3.format(date1);

                //String topic = data.getString("device_id");
                //检查device_id
                JSONArray array = data.getJSONArray("datastreams");
                if ((array != null) && (id != null)) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject data_point = array.getJSONObject(i);
                        String dm_name = data_point.getString("dm_name");
                        //String time = data_point.getString("at");
                        //获取当前时间
                        //Date date = new Date();
                        //String time = sdf3.format(date);
                        String time = data_point.getString("at");
                        String value = data_point.getString("value");
                        JSONObject object = new JSONObject();
                        if ((dm_name != null) && (value != null)) {
                            object.put("dm_name", dm_name);
                            object.put("time", time);
                            object.put("value", Double.valueOf(value));
                            result.add(object);
                            //String revtime = sdf3.format(date);
                            //long m = sdf3.parse(revtime).getTime() - sdf3.parse(time).getTime();
                            //logger.debug("接收信息相差毫秒数： "+m);

                        } else {
                            logger.debug("数据格式错误，解析失败");
                        }
                    }
                } else {
                    logger.debug("数据格式错误，解析失败：datastreams不存在");
                }
                //存储数据
                if (!result.isEmpty()) {
                    logger.info("http数据解析结果为：" + data + "---开始保存数据");
                    //分析时间差
                    Date date2 = new Date();
                    String resulttime = sdf3.format(date2);
                    long m1 = sdf3.parse(resulttime).getTime() - sdf3.parse(arrtime).getTime();

                    dealWithData(id, result);

                    Date date3 = new Date();
                    String savetime = sdf3.format(date3);
                    long m2 = sdf3.parse(savetime).getTime() - sdf3.parse(resulttime).getTime();

                    /* TODO：事件判断
                   //触发判断
                    try {
                        triggerService.TriggerAlarm(id, result);
                        //分析时间差
                        Date date4 = new Date();
                        String triggertime = sdf3.format(date4);
                        long m3 = sdf3.parse(triggertime).getTime() - sdf3.parse(savetime).getTime();
                        //logger.debug("时间节点：1. "+arrtime+", 2. "+resulttime+", 3. "+savetime+", 4. "+triggertime);
                        logger.debug("处理信息相差毫秒数： "+m1+"，储存信息相差毫秒数： "+m2+", 触发信息相差毫秒数： "+m3);

                    } catch (InterruptedException e) {
                        logger.error("http实时数据触发失败");
                        e.printStackTrace();
                    }*/
                }
            } catch (Exception e) {
                logger.error("HTTP解析失败");
                e.printStackTrace();
            }
        });
    }
}
