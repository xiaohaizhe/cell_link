package com.hydata.intelligence.platform.cell_link.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
