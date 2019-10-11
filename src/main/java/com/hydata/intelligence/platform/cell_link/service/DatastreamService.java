package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Datapoint;
import com.hydata.intelligence.platform.cell_link.entity.Datastream;
import com.hydata.intelligence.platform.cell_link.entity.Device;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DatapointRepository;
import com.hydata.intelligence.platform.cell_link.repository.DatastreamRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName DatastreamService
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/4 18:09
 * @Version
 */
@Service
@Transactional
public class DatastreamService {
    @Autowired
    private DatastreamRepository datastreamRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DatapointRepository datapointRepository;
    private Logger logger = LogManager.getLogger(DatastreamService.class);

    public JSONObject add(Datapoint datapoint){
        if (datapoint.getDatastreamId()!=null){
            Optional<Datastream> datastreamOptional = datastreamRepository.findById(datapoint.getDatastreamId());
            if (datastreamOptional.isPresent()){
                Datastream datastream = datastreamOptional.get();
                Datapoint datapointNew = new Datapoint();
                datapointNew.setDatastream(datastream);
                datapointNew.setDatastreamId(datastream.getDatastreamId());
                datapointNew.setDatastreamName(datastream.getDatastreamName());
                datapointNew.setDeviceId(datastream.getDevice().getDeviceId());
                datapointNew.setCreated(new Date());
                datapointNew.setValue(datapoint.getValue());
                datapointNew.setStatus(datapoint.getStatus());
                datapointRepository.save(datapointNew);
                return RESCODE.SUCCESS.getJSONRES();
            }
        }return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES();
    }

    public JSONObject add(Datastream datastream) {
        if (datastream.getDevice() != null && datastream.getDevice().getDeviceId() != 0) {
            Optional<Device> deviceOptional = deviceRepository.findById(datastream.getDevice().getDeviceId());
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();
                datastream.setUserId(device.getUserId());
                datastream.setScenarioId(device.getScenarioId());
                datastream.setDgId(device.getDeviceGroup().getDgId());
                datastream.setDeviceName(device.getDeviceName());
                datastreamRepository.save(datastream);
                return RESCODE.SUCCESS.getJSONRES();
            }
        }
        return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    private JSONObject getDatastream(Datastream datastream) {
        JSONObject object = new JSONObject();
        object.put("datastreamId", datastream.getDatastreamId());
        object.put("datastreamName", datastream.getDatastreamName());
        object.put("created", datastream.getCreated());
        object.put("updateTime", datastream.getUpdateTime());
        return object;
    }

    private JSONObject getDatapoint(Datapoint datapoint) {
        JSONObject object = new JSONObject();
        object.put("datastreamName", datapoint.getDatastreamName());
        object.put("created", datapoint.getCreated());
        object.put("value", datapoint.getValue());
        object.put("status",datapoint.getStatus());
        return object;
    }

    @Cacheable(cacheNames = "datastream",keyGenerator = "myKeyGenerator")
    public JSONObject findByDevice(Long deivceId, String datastreamName, Integer page, Integer number, String sorts) {
        if (deviceRepository.existsById(deivceId)) {
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<Datastream> datastreamPage = datastreamRepository.findByDeviceAndDatastreamName(
                    deivceId, datastreamName, pageable);
            List<JSONObject> datastreamList = new ArrayList<>();
            for (Datastream datastream : datastreamPage.getContent()) {
                datastreamList.add(getDatastream(datastream));
            }
            return RESCODE.SUCCESS.getJSONRES(
                    datastreamList, datastreamPage.getTotalPages(), datastreamPage.getTotalElements());
        }
        return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    @Cacheable(cacheNames = "datastream",keyGenerator = "myKeyGenerator")
    public JSONObject findByDatastream(Long datastreamId, String start, String end) {
        if (datastreamRepository.existsById(datastreamId)) {
            List<Datapoint> datapoints = datapointRepository.findByDatastreamIdAndCreatedBetween(datastreamId, StringUtil.getDate(start), StringUtil.getDate(end));
            List<JSONObject> datapointList = new ArrayList<>();
            for (Datapoint datapoint : datapoints) {
                datapointList.add(getDatapoint(datapoint));
            }
            return RESCODE.SUCCESS.getJSONRES(datapointList);
        }
        return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES();
    }

    @Cacheable(cacheNames = "datastream",keyGenerator = "myKeyGenerator")
    public JSONObject findByDeviceId(Long deviceId){
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()){
            List<Datastream> datastreamList = datastreamRepository.findListByDevice(deviceId);
            JSONArray array = new JSONArray();
            for (Datastream datastream:
                 datastreamList) {
                array.add(getDatastream(datastream));
            }
            return RESCODE.SUCCESS.getJSONRES(array);
        }return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }

    /**
     * 判断近100个数据点异常情况
     *
     * @param ds_id: data_history_id
     * @return object: 返回数据流
     */
    public JSONObject checkStatus(Long ds_id) {
        int num = 100;
        Pageable pageable = new PageRequest(0, num, Sort.Direction.DESC, "created");
        Page<Datapoint> data_historyPage = datapointRepository.findByDatastreamId(ds_id, pageable);

/*
        logger.info(datapointRepository.findByDatastreamId(Long.parseLong("1570695189094")));
        logger.info(data_historyPage.getContent());
        logger.info(data_historyPage.getTotalElements());
        logger.info(datapointRepository.findAll().isEmpty());

*/
        if (data_historyPage != null) {
            logger.info("开始判断最近100条数据流的异常情况");
            List<Datapoint> data_histories = data_historyPage.getContent();
            //logger.info("数据点如下："+data_histories);
            if (data_histories.size() < num) {
                return RESCODE.INSUFFICIENT_DATA.getJSONRES();
            }
            Date last = data_histories.get(0).getCreated();
            Date curr;
            long total = 0;
            for (int i = 0; i < num; i++) {
                Datapoint data_history = data_histories.get(i);
                curr = data_history.getCreated();
                long delta = last.getTime() - curr.getTime();
                last = curr;
                total += delta;
            }
            long freq = total / num;
            //logger.info("最近100条数据流的平均频率是" + freq + "毫秒");
            last = data_histories.get(0).getCreated();
            for (int i = 0; i < num; i++) {
                Datapoint data_history = data_histories.get(i);
                curr = data_history.getCreated();
                long delta = last.getTime() - curr.getTime();
                if (delta < freq * 0.5) {
                    data_history.setStatus(1);
                } else if (delta > freq * 1.5) {
                    data_history.setStatus(2);
                } else {
                    data_history.setStatus(0);
                }
                datapointRepository.save(data_history);
                last = curr;
            }
            data_historyPage = datapointRepository.findByDatastreamId(ds_id, pageable);
            //logger.info("数据流诊断结果：" + data_historyPage.getContent());
            return RESCODE.SUCCESS.getJSONRES(data_historyPage.getContent());

        }
        return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES(ds_id);

    }

    /**
     * 根据数据流id显示诊断详情
     *
     * @param ds_id： 数据流id
     * @return
     */
    public JSONObject getStatusLog(Long ds_id) {
        List<Datapoint> data_histories = datapointRepository.findByDatastreamId(ds_id);
        if (!data_histories.isEmpty()) {
            return RESCODE.SUCCESS.getJSONRES(data_histories);
        } else {
            logger.debug("数据流" + ds_id + "不存在");
            return RESCODE.DATASTREAM_NOT_EXIST.getJSONRES(null, 0, 0L);
        }
    }

}
