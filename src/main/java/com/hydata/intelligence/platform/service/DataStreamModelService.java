package com.hydata.intelligence.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.*;
import com.hydata.intelligence.platform.model.DataHistory;
import com.hydata.intelligence.platform.model.DataStreamModel;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.*;
import com.hydata.intelligence.platform.utils.MongoDBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:50:28
 */
@Transactional
@Service
public class DataStreamModelService {
    @Autowired
    private DatastreamModelRepository datastreamModelRepository;

    @Autowired
    private UnitTypeRepository unitTypeRepository;

    @Autowired
    private UnitTypeService unit_type_Service;

    @Autowired
    private OperationLogsRepository operationLogsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeviceDatastreamRepository datastreamRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DataHistoryRepository dataHistoryRepository;

    @Autowired
    private DeviceDatastreamRepository deviceDatastreamRepository;

    private static MongoDBUtils mongoDBUtil = MongoDBUtils.getInstance();
    /*private static MongoClient meiyaClient = mongoDBUtil.getMongoConnect();
    private static MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient,"cell_link","device");
    */
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Logger logger = LogManager.getLogger(DataStreamModelService.class);

    /**
     * 添加数据流模板
     *
     * @param dsModel
     * @return
     */
    public JSONObject addDataStreamModel(DataStreamModel dsModel) {
        Optional<Product> productOptional = productRepository.findById(dsModel.getProduct_id());
        if (productOptional.isPresent()) {
            OperationLogs logs = new OperationLogs();
            logs.setUserId(productOptional.get().getUserId());
            logs.setOperationTypeId(7);
            String msg = "添加数据流模板：" + dsModel.getName();
            logs.setCreateTime(new Date());
            logger.debug("进入添加设备数据流模板");
            JSONObject checkResult = checkModel(dsModel);
            logger.debug(checkResult.toString());
            if ((Integer) checkResult.get("code") == 0) {
                UnitType unitTypeNew = new UnitType();
                UnitType unitTypeReturn = new UnitType();
                unitTypeReturn.setId(0);
                unitTypeNew.setName(dsModel.getUnit_name());
                unitTypeNew.setSymbol(dsModel.getUnit_symbol());
                JSONObject result = unit_type_Service.add(unitTypeNew);
                if ((Integer) result.get("code") == 0) {
                    unitTypeReturn = (UnitType) result.get("data");
                }
                DatastreamModel datastreamModel = new DatastreamModel();
                datastreamModel.setName(dsModel.getName());
                datastreamModel.setProductId(dsModel.getProduct_id());
                datastreamModel.setUnitTypeId(unitTypeReturn.getId());
                datastreamModel.setCreateTime(new Date());
                DatastreamModel result2 = datastreamModelRepository.save(datastreamModel);
                logger.debug("添加设备数据流模板结束");
                if (result2 != null) {
                    logger.debug("添加设备数据流模板结果：" + result2.toString());
                    logs.setMsg(msg + "成功");
                    operationLogsRepository.save(logs);
                    return RESCODE.SUCCESS.getJSONRES();
                }
            } else {
                logs.setMsg(msg + "失败");
                operationLogsRepository.save(logs);
                return RESCODE.DSM_REPEAT.getJSONRES();
            }

        }
        return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
    }

    /**
     * 通过id删除数据流模板
     *
     * @param id
     * @return
     */
    public JSONObject deleteByDSMId(long id) {
        /**
         *  1.数据流触发器删除（未加）
         *  2.设备数据流删除（未加）
         *  3.数据流模板删除
         */
        Optional<DatastreamModel> datastreamModel = datastreamModelRepository.findById(id);
        if (datastreamModel.isPresent()) {
            datastreamModelRepository.deleteById(id);
            Optional<Product> productOptional = productRepository.findById(datastreamModel.get().getProductId());
            if (productOptional.isPresent()) {
                OperationLogs logs = new OperationLogs();
                logs.setUserId(productOptional.get().getUserId());
                logs.setOperationTypeId(7);
                logs.setMsg("删除数据流模板：" + datastreamModel.get().getName());
                logs.setCreateTime(new Date());
                operationLogsRepository.save(logs);
            }
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.ID_NOT_EXIST.getJSONRES();
    }

    public JSONObject modifyDSM(DataStreamModel dsModel) {
        logger.debug("进入修改数据流模板，模板id:" + dsModel.getId());
        JSONObject checkResult = checkModel(dsModel);
        if ((Integer) checkResult.get("code") == 0) {
            UnitType unitTypeNew = new UnitType();
            UnitType unitTypeReturn = new UnitType();
            unitTypeReturn.setId(0);
            unitTypeNew.setName(dsModel.getUnit_name());
            unitTypeNew.setSymbol(dsModel.getUnit_symbol());
            JSONObject result = unit_type_Service.add(unitTypeNew);
            if ((Integer) result.get("code") == 0) {
                unitTypeReturn = (UnitType) result.get("data");
            }
            Optional<DatastreamModel> dsm_optional = datastreamModelRepository.findById(dsModel.getId());
            if (dsm_optional.isPresent()) {
                DatastreamModel dsm = dsm_optional.get();
                dsm.setName(dsModel.getName());
                dsm.setUnitTypeId(unitTypeReturn.getId());
                DatastreamModel dsmResult = datastreamModelRepository.save(dsm);

                Optional<Product> productOptional = productRepository.findById(dsm.getProductId());
                if (productOptional.isPresent()) {
                    OperationLogs logs = new OperationLogs();
                    logs.setUserId(productOptional.get().getUserId());
                    logs.setOperationTypeId(7);
                    logs.setMsg("修改数据流模板：" + dsModel.getName());
                    logs.setCreateTime(new Date());
                    operationLogsRepository.save(logs);
                }

                return RESCODE.SUCCESS.getJSONRES(dsmResult);
            }
            return RESCODE.ID_NOT_EXIST.getJSONRES();
        }
        return RESCODE.DSM_REPEAT.getJSONRES();

    }

    /**
     * 检查数据流模板
     *
     * @param dsModel
     * @return
     */
    public JSONObject checkModel(DataStreamModel dsModel) {
        logger.debug("检查数据流模板：" + dsModel.getName() + dsModel.getUnit_name() + dsModel.getUnit_symbol() + "是否重复");
        List<DatastreamModel> dsm = datastreamModelRepository.findByProductIdAndName(dsModel.getProduct_id(), dsModel.getName());
        boolean isRepeat = false;
        if (dsm != null) {
            logger.debug("产品：" + dsModel.getProduct_id() + "下数据流模板名称:" + dsModel.getName() + "存在");
            logger.debug("开始检查单位名称符号是否一致");
            logger.debug("进入循环检查");
            for (DatastreamModel datastreamModel : dsm) {
                logger.debug(datastreamModel.getUnitTypeId());
                Optional<UnitType> ut = unitTypeRepository.findById(datastreamModel.getUnitTypeId());
                logger.debug(ut.isPresent());
                if (ut.isPresent()) {
                    UnitType unitType = ut.get();
                    logger.debug(unitType.toString());
                    logger.debug("unitType.getName()" + unitType.getName());
                    logger.debug("dsModel.getName()" + dsModel.getUnit_name());
                    logger.debug(unitType.getName().equals(dsModel.getUnit_name()));
                    logger.debug("unitType.getSymbol()" + unitType.getSymbol());
                    logger.debug("dsModel.getUnit_symbol()" + dsModel.getUnit_symbol());
                    logger.debug(unitType.getSymbol().equals(dsModel.getUnit_symbol()));
                    if (unitType.getName().equals(dsModel.getUnit_name()) &&
                            unitType.getSymbol().equals(dsModel.getUnit_symbol())) {
                        logger.debug("产品：" + dsModel.getProduct_id() + "下数据流模板名称:" + dsModel.getName() + "存在，且单位名称符号一致");
                        isRepeat = true;
                        break;
                    }
                }
            }
        }
        logger.debug("数据流是否重复：" + isRepeat);
        if (isRepeat) {
            return RESCODE.DSM_REPEAT.getJSONRES();
        }
        return RESCODE.SUCCESS.getJSONRES();
    }

    @SuppressWarnings("deprecation")
    public Page<DatastreamModel> queryByProductId(Long productId, Integer page, Integer number) {
        Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC, "id");
        return datastreamModelRepository.queryByProductId(productId, pageable);
    }

    public JSONObject findByName(Integer page, Integer number, String dsmName, Long productId) {
        JSONObject object = new JSONObject();
        @SuppressWarnings("deprecation")
        Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC, "id");
        Page<DatastreamModel> result = datastreamModelRepository.findAll(new Specification<DatastreamModel>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<DatastreamModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (productId != null && productId >= 0) {
                    predicateList.add(
                            criteriaBuilder.equal(
                                    root.get("productId").as(Long.class),
                                    productId));
                }
                if (dsmName != null && !dsmName.equals("")) {

                    predicateList.add(
                            //like：模糊匹配，跟SQL是一样的
                            criteriaBuilder.like(
                                    //user表里面有个String类型的name
                                    root.get("name").as(String.class),
                                    //映射规则
                                    "%" + dsmName + "%"));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        }, pageable);
        List<DatastreamModel> dms = result.getContent();
        JSONArray array = new JSONArray();
        for (DatastreamModel dm : dms) {
            JSONObject dmObject = new JSONObject();
            dmObject.put("id", dm.getId());
            dmObject.put("name", dm.getName());
            if (unitTypeRepository.findById(dm.getUnitTypeId()).isPresent()) {
                dmObject.put("unit_name", unitTypeRepository.findById(dm.getUnitTypeId()).get().getName());
                dmObject.put("unit_symbol", unitTypeRepository.findById(dm.getUnitTypeId()).get().getSymbol());
            }
            array.add(dmObject);
        }

        return RESCODE.SUCCESS.getJSONRES(array, result.getTotalPages(), Integer.valueOf(String.valueOf(result.getTotalElements())));
    }

    public JSONObject getIncrement(Long product_id, Date start, Date end) throws ParseException {

        long getIncrementstart =System.currentTimeMillis();
        //获取产品下全部设备
        List<Device> devices = deviceRepository.findByProductId(product_id);
        //循环获取设备下全部数据流数据点
//        List<Data_history> dataHistories = new ArrayList<>();
        logger.info("设备下产品长度：" + devices.size());
        List<Long> deviceids = new ArrayList<>();
        for (Device device : devices) {
            deviceids.add(device.getId());
        }
        List<Long> datastreamids = datastreamRepository.findByDevice_idIn(deviceids);
        logger.info(datastreamids);
        List<Data_history> dataHistortList = dataHistoryRepository.findByDd_idInAndCreate_timeBetween(datastreamids, start, end);
       /* for (Data_history dataHistory : dataHistortList) {
            dataHistories.add(dataHistory);
        }*/
        long getIncrementend1 = System.currentTimeMillis();
        logger.info("从数据库获取数据总时间："+(getIncrementend1-getIncrementstart)+"ms");
        logger.info("数据点长度为：" + dataHistortList.size());
        logger.info(sdf.format(start));
        logger.info(sdf1.parse(sdf1.format(start)).getTime());
        logger.info(sdf.format(new Date()));
        logger.info(new Date().getTime());
        logger.info(sdf.format(end));
        logger.info(end.getTime());
        JSONArray statistics = new JSONArray();
        int len = 0;
        if (end.getTime() > new Date().getTime()) {
            logger.debug("结束时间比当前时间晚");
            len = new Long((new Date().getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
            logger.debug(len);
        } else {
            logger.debug("结束时间早于当前时间");
            len = new Long((sdf1.parse(sdf1.format(end)).getTime() - sdf1.parse(sdf1.format(start)).getTime()) / 1000 / 60 / 60 / 24).intValue();
        }
        logger.debug("共需循环" + (len + 1) + "次");
        long getIncrementend2 = System.currentTimeMillis();
        logger.info("计算分点："+(getIncrementend2-getIncrementend1)+"ms");
        try {
            Date temp = sdf.parse(sdf.format(start));
            for (int i = 0; i <= len; i++) {
                logger.debug("第" + (i + 1) + "次");
                Date ss = sdf1.parse(sdf1.format(temp));
                temp.setDate(temp.getDate() + 1);
                Date ee = sdf1.parse(sdf1.format(temp));
                int sum = 0;
                for (Data_history dh : dataHistortList) {
                    if (dh.getCreate_time().getTime() >= ss.getTime() && dh.getCreate_time().getTime() < ee.getTime())
                        sum++;
                }
                JSONObject r = new JSONObject();
                r.put("time", sdf1.format(ss));
                r.put("value", sum);
                statistics.add(r);
            }
            long getIncrementend3 = System.currentTimeMillis();
            logger.info("处理数据点："+(getIncrementend3-getIncrementend2)+"ms");
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }

        logger.info(statistics);
/*
		for(Data_history dh:dataHistories){
			*//*String d = sdf1.format(dh.getCreate_time());
			if(statistics.get(d) != null) {
				statistics.put(d, (Integer)statistics.get(d)+1);
			}else {
				statistics.put(d, 1);
			}*//*
		}*/
		/*JSONArray array = new JSONArray();
		for (Entry<String, Object> entry : statistics.entrySet()) {
			JSONObject sum = new JSONObject();
			sum.put("time", entry.getKey());
			sum.put("value", entry.getValue());
			array.add(sum);
		}*/

        return RESCODE.SUCCESS.getJSONRES(statistics);
    }

    public JSONObject queryByDsNameOrDeviceName(Long product_id,int type,String dsNameOrDeviceName,String start,String end, Integer page,Integer number){
        Date s;
        Date e;
        try{
            s = sdf2.parse(start);
            e = sdf2.parse(end);
        }catch (ParseException pe){
            logger.error(pe.getMessage());
            return RESCODE.TIME_PARSE_ERROR.getJSONRES();
        }
        //获取条件下设备id
        List<Device> deviceList0 = deviceRepository.findByProductIdAndNameLikeAndCreate_timeBetween(product_id,dsNameOrDeviceName,s,e);
        List<Long> deviceIds0 = new ArrayList<>();
        for (Device device:
                deviceList0) {
            deviceIds0.add(device.getId());
        }
        System.out.println(deviceList0.size());
        List<Device> deviceList1 = deviceRepository.findByProductIdAndCreate_timeBetween(product_id,s,e);
        List<Long> deviceIds1 = new ArrayList<>();
        for (Device device:
                deviceList1) {
            deviceIds1.add(device.getId());
        }
        System.out.println(deviceList1.size());
        Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC, "id");
        List<com.hydata.intelligence.platform.model.DeviceDatastream> deviceDatastreamList = new ArrayList<>();
        Long total_elements = 0L;
        Integer total_pages = 0;
        System.out.println(deviceDatastreamList);
        System.out.println(total_elements);
        System.out.println(total_pages);
        switch (type){
            case 0://全部
                Page<DeviceDatastream> deviceDatastreamPage0 = null;
                if(deviceIds0.size()>0 && deviceIds1.size()>0){
//                    System.out.println("进入复杂运算");
                    deviceDatastreamPage0=deviceDatastreamRepository.
                            findByDevice_idInAndAndDm_nameLike(deviceIds1,dsNameOrDeviceName,deviceIds0,pageable);
                }else if (deviceIds1.size()>0){
                    deviceDatastreamPage0=deviceDatastreamRepository.
                            findByDevice_idInAndAndDm_nameLike(deviceIds1,dsNameOrDeviceName,pageable);
                }
                if (deviceDatastreamPage0 != null) {
                    List<DeviceDatastream> deviceDatastreams0 = deviceDatastreamPage0.getContent();
                    for (DeviceDatastream deviceDatastream : deviceDatastreams0) {
                        com.hydata.intelligence.platform.model.DeviceDatastream d = getDdDetail(deviceDatastream);
                        deviceDatastreamList.add(d);
                    }
                    total_elements = deviceDatastreamPage0.getTotalElements();
                    total_pages = deviceDatastreamPage0.getTotalPages();
                }
//                System.out.println("0：结束");
                break;
            case 1://设备名称
                if(deviceIds0.size()>0){
                    Page<DeviceDatastream> deviceDatastreamPage1=deviceDatastreamRepository.
                            findByDevice_idIn(deviceIds0,pageable);
                    List<DeviceDatastream> deviceDatastreams1 =deviceDatastreamPage1.getContent();
                    for(DeviceDatastream deviceDatastream:deviceDatastreams1){
                        com.hydata.intelligence.platform.model.DeviceDatastream d = getDdDetail(deviceDatastream);
                        deviceDatastreamList.add(d);
                    }
                    total_elements = deviceDatastreamPage1.getTotalElements();
                    total_pages = deviceDatastreamPage1.getTotalPages();
                }
                break;
            case 2://数据流名称
                if(deviceIds1.size()>0){
                    Page<DeviceDatastream> deviceDatastreamPage2=deviceDatastreamRepository.
                            findByDevice_idInAndAndDm_nameLike(deviceIds1,dsNameOrDeviceName,pageable);
                    List<DeviceDatastream> deviceDatastreams2 =deviceDatastreamPage2.getContent();
                    for(DeviceDatastream deviceDatastream:deviceDatastreams2){
                        com.hydata.intelligence.platform.model.DeviceDatastream d = getDdDetail(deviceDatastream);
                        deviceDatastreamList.add(d);
                    }
                    total_elements = deviceDatastreamPage2.getTotalElements();
                    total_pages = deviceDatastreamPage2.getTotalPages();
                }
                break;
        }
        return RESCODE.SUCCESS.getJSONRES(deviceDatastreamList,total_pages,Integer.valueOf(String.valueOf(total_elements)));
    }

    //数据流详情
    public com.hydata.intelligence.platform.model.DeviceDatastream getDdDetail(DeviceDatastream dd){
        com.hydata.intelligence.platform.model.DeviceDatastream deviceDatastream = new com.hydata.intelligence.platform.model.DeviceDatastream();
        deviceDatastream.setDd_id(dd.getId());
        deviceDatastream.setName(dd.getDm_name());
        Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC, "id");
        Page<Data_history> data_historyPage = dataHistoryRepository.findByDd_id(dd.getId(), pageable);
        Long sum = data_historyPage.getTotalElements();
        if(sum <= 0){
            deviceDatastream.setDate(null);
        }else{
            List<Data_history> data_histories = data_historyPage.getContent();
            deviceDatastream.setDate(data_histories.get(0).getCreate_time());
        }
        Optional<Device> deviceOptional=deviceRepository.findById(dd.getDevice_id());
        if (deviceOptional.isPresent()){
            deviceDatastream.setDevice_name(deviceOptional.get().getName());
            deviceDatastream.setDevice_id(dd.getDevice_id());
        }else{
            deviceDatastream.setDevice_name(null);
        }
        return deviceDatastream;
    }

    public JSONObject getDeviceDatastreamStatus(Long dd_id){
        String today = sdf1.format(new Date());
        Date start;
        try{
            start = sdf2.parse(today+" 00:00:00");
        }catch (ParseException e){
            logger.error(e.getMessage());
            return RESCODE.TIME_PARSE_ERROR.getJSONRES();
        }
        int sum = 0;
        int sum_new = 0;
        int normal_sum = 0;
        int abnormal_sum = 0;
         List<Data_history> data_histories = new ArrayList();
        sum = dataHistoryRepository.findByDd_id(dd_id).size();
        sum_new = dataHistoryRepository.findByDd_idAndCreate_timeBetween(dd_id,start,new Date()).size();
       //最近六小时内数据点异常/正常情况
//        Date six_hours_ago = new Date();
//        six_hours_ago.setHours(six_hours_ago.getHours()-6);
//        List<Data_history> data_histories = dataHistoryRepository.findByDd_idAndCreate_timeBetween(dd_id,six_hours_ago,new Date());
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "create_time");
        Page<Data_history> data_historyPage = dataHistoryRepository.findByDd_id(dd_id, pageable);
        if(data_historyPage.getTotalElements()>0){
            data_histories = data_historyPage.getContent();
            for (Data_history data_history:
                    data_histories) {
                if (data_history.getStatus()!=0){
                    abnormal_sum++;
                }else{
                    normal_sum++;
                }
            }
        }

        JSONObject result = new JSONObject();
        result.put("sum",sum);
        result.put("sum_new",sum_new);
        result.put("normal_sum",normal_sum);
        result.put("abnormal_sum",abnormal_sum);
        result.put("six_hours_data",data_histories);
        return RESCODE.SUCCESS.getJSONRES(result);
    }

}

