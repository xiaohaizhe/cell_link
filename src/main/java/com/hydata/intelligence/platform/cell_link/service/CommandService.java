package com.hydata.intelligence.platform.cell_link.service;

/**
 * @author: Jasmine
 * @createTime: 2019-09-24 15:13
 * @description:
 * @modified:
 */
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.*;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.CmdLogsRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.DeviceRepository;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.ExcelUtils;
import com.hydata.intelligence.platform.cell_link.utils.MqttUtils;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import com.hydata.intelligence.platform.cell_link.utils.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class CommandService {
    @Autowired
    private DeviceGroupService deviceGroupService;
    @Autowired
    private MqttInitService mqttInitService;
    @Autowired
    private MqttHandlerService mqttHandler;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private CmdLogsRepository cmdLogsRepository;
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LogManager.getLogger(CommandService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //@Resource
    //private MqttPahoMessageHandler mqttHandler;

    /**
     * 根据设备名称/场景/设备组查询命令日志分页
     *
     * @param user_id     用户id 必填
     * @param cmd         命令内容
     * @param page        页码 必填
     * @param number      每页显示数量 必填
     * @param sorts       排序条件
     * @param scenario_id 场景id
     * @param dg_id       设备组id
     * @param start       命令创建开始时间
     * @param end         命令创建结束时间
     * @param status      设备状态
     * @return 结果
     */
    @Cacheable(cacheNames = "log", keyGenerator = "myKeyGenerator")
    public JSONObject findByCmd(Long user_id, String cmd, Integer page, Integer number, String sorts,
                                       Long scenario_id, Long dg_id, String start, String end, Integer status) {

        Pageable pageable = PageUtils.getPage(page, number, sorts);
        Page<CmdLogs> cmdPage = null;
        cmdPage = cmdLogsRepository.findAll(getSpecification(user_id,cmd,scenario_id,dg_id,start,end,status), pageable);
        List<JSONObject> cmdList = new ArrayList<>();
        for (CmdLogs cmdLog : cmdPage.getContent()) {
            cmdList.add(getCmdLogs(cmdLog));
        }
        return RESCODE.SUCCESS.getJSONRES(cmdList, cmdPage.getTotalPages(), cmdPage.getTotalElements());
    }

    private Specification<CmdLogs> getSpecification(Long userId, String cmd, Long scenarioId, Long dgId, String start, String end, Integer status) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (userId != null && userId >= 0) {
                logger.info("userId:" + userId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("userId").as(Long.class),
                                userId));
            }
            if (cmd != null && !cmd.equals("")) {
                logger.info("cmd:" + cmd);
                predicateList.add(
                        //like：模糊匹配，跟SQL是一样的
                        criteriaBuilder.like(
                                //user表里面有个String类型的name
                                root.get("cmd").as(String.class),
                                //映射规则
                                "%" + cmd + "%"));
            }
            if (scenarioId != null && scenarioId >= 0) {
                logger.info("scenario_id:" + scenarioId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("scenarioId").as(Long.class),
                                scenarioId));
            }
            if (dgId != null && dgId >= 0) {
                logger.info("dg_id:" + dgId);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("deviceGroup").as(DeviceGroup.class),
                                dgId));
            }
            if (status != null && status >= 0) {
                logger.info("status:" + status);
                predicateList.add(
                        criteriaBuilder.equal(
                                root.get("status").as(Integer.class),
                                status));
            }
            if (start != null && !start.equals("") && end != null && !end.equals("")) {
                logger.info("start:" + start);
                logger.info("end:" + end);
                Date s = StringUtil.getDate(start);
                Date e = StringUtil.getDate(end);
                if (s != null && e != null) {
                    predicateList.add(
                            criteriaBuilder.between(
                                    root.get("sendTime").as(Date.class), s, e));
                }
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
    }

    @Cacheable(cacheNames = "log",keyGenerator = "myKeyGenerator")
    public JSONObject getCmdLogsList(Long userId){
        List<CmdLogs> cmdLogsList = cmdLogsRepository.findByUserId(userId);
        List<JSONObject> cmdLogs = new ArrayList<>();
        for (CmdLogs cmdLog: cmdLogsList){
            cmdLogs.add(getCmdLogs(cmdLog));
        }
        return RESCODE.SUCCESS.getJSONRES(cmdLogs);
    }
    @Cacheable(cacheNames = "log",keyGenerator = "myKeyGenerator")
    public JSONObject getcmdLogPage(Long userId,Integer page,Integer number,String sorts){
        Pageable pageable = PageUtils.getPage(page, number, sorts);
        Page<CmdLogs> cmdLogsPage = cmdLogsRepository.findByUserId(userId,pageable);
        List<JSONObject> cmdLogs = new ArrayList<>();
        for (CmdLogs cmdLog: cmdLogsPage){
            cmdLogs.add(getCmdLogs(cmdLog));
        }
        return RESCODE.SUCCESS.getJSONRES(cmdLogs,cmdLogsPage.getTotalPages(),cmdLogsPage.getTotalElements());
    }

    public JSONObject getCmdLogs(CmdLogs cmdLogs){
        JSONObject object = new JSONObject();
        object.put("id",cmdLogs.getId());
        object.put("cmd",cmdLogs.getCmd());
        object.put("created",cmdLogs.getSendTime());
        object.put("rec_code",cmdLogs.getRes_code());
        object.put("rec_msg",cmdLogs.getRes_msg());
        return object;
    }

    /**
     * 根据设备鉴权码显示命令日志
     *
     * @param device_id
     * @param page
     * @param number
     * @return
     */
    public JSONObject getCmdLogs(Integer page, Integer number, long device_id) {
        Optional<Device> deviceOptional = deviceRepository.findById(device_id);
        if (deviceOptional.isPresent()) {
            Pageable pageable = new PageRequest(page - 1, number, Sort.Direction.DESC, "id");
            Page<CmdLogs> cmdPage = cmdLogsRepository.findByDeviceId(device_id, pageable);
            return RESCODE.SUCCESS.getJSONRES(cmdPage.getContent(), cmdPage.getTotalPages(), Long.parseLong(String.valueOf(cmdPage.getTotalElements())));
        } else {
            logger.debug("设备" + device_id + "不存在");
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES(null, 0, 0L);
        }
    }


    /**
     * 导出日志
     *
     * @param device_id
     * @param request
     * @param response
     */
    public void exportCmdLogs(Long device_id, HttpServletRequest request, HttpServletResponse response) {
        List<CmdLogs> cmdLogs = cmdLogsRepository.findByDeviceId(device_id);
        JSONArray array = new JSONArray();
        for (CmdLogs cmdLog : cmdLogs) {
            JSONObject object = new JSONObject();
            object.put("id", cmdLog.getId());
            object.put("device_id", device_id);
            object.put("msg", cmdLog.getMsg());
            object.put("sendTime", cmdLog.getSendTime());
            object.put("res_code", cmdLog.getRes_code());
            object.put("res_msg", cmdLog.getRes_msg());
            array.add(object);
        }
        ExcelUtils.exportCmdLogs(array, request, response);
    }

    /**
     * MQTT 下发命令的外部接口
     *
     * @param device_id:             设备鉴权信息
     * @param content：               命令内容
     * @param type：命令类型：0为字符串，1为十六进制
     * @return
     */
    public JSONObject externalSend(long device_id, String content, int type) {
        JSONObject object = new JSONObject();
        Optional<Device> deviceOptional = deviceRepository.findById(device_id);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            DeviceGroup dg = device.getDeviceGroup();
            if (dg.getProtocol().getProtocolId().equals(1)) {
                return send(device_id, content, type);
            } else {
                logger.info("场景不存在");
                return RESCODE.PROTOCOL_NOT_MATCH.getJSONRES();
            }
        }
        logger.info("设备不存在");
        return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
    }


    /**
     * MQTT的下发命令
     *
     * @param topic：设备id
     * @param content：命令信息
     * @param type：命令类型：0为字符串，1为十六进制
     * @return
     */

    public JSONObject send(long topic, String content, int type) {
        Date date = new Date();
        boolean isMqtt = false;
        logger.info("尝试向设备："+topic+"发送命令："+content);
        //查找device对应的productID
        /*Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("device_sn", topic);
        JSONArray array = new JSONArray();
        FindIterable<Document> documents = mongoDBUtil.queryDocument(collection, conditions, null, null, null, null, null, null);*/
        String cmd = content;
        if (content == null || content.equals("")) {
            logger.debug("命令为空，未发送");
            CmdLogs cmdLog = new CmdLogs();
            cmdLog.setId(System.currentTimeMillis());
            cmdLog.setDeviceId(topic);
            cmdLog.setMsg(content);
            Optional<Device> deviceOptional = deviceRepository.findById(topic);
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setUserId(device.getUserId());
            } else {
                cmdLog.setScenarioId(0L);
            }
            cmdLog.setSendTime(date);
            cmdLog.setRes_code(1);
            cmdLog.setRes_msg("命令为空，消息未发送");
            cmdLogsRepository.save(cmdLog);
            logger.info("命令发送日志: "+cmdLog);
            return RESCODE.FAILURE.getJSONRES();
        }

        //判断设备所在产品是否为mqtt格式
        Optional<Device> deviceOptional = deviceRepository.findById(topic);
        Device device = new Device();
        if (deviceOptional.isPresent()) {
            device = deviceOptional.get();
            DeviceGroup dg = device.getDeviceGroup();
            logger.info("设备组id为"+dg.getDgId());
            if (dg.getProtocol().getProtocolId().equals(2)) {
                isMqtt = true;
            } else {
                logger.info("数据组协议不支持命令下发");
                return RESCODE.NO_CHANGES.getJSONRES();
            }
        }else {
            logger.error("设备信息未找到" + topic + "，命令发送失败");
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
        }

        logger.info("检查设备组协议是否为MQTT======"+isMqtt);

        //将16进制的content转换为字符串格式
        if (type == 1) {
            content = content.replace(" ", "");
            byte[] baKeyword = new byte[content.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(content.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    logger.error("16进制命令格式错误，转换失败"+e);
                    CmdLogs cmdLog = new CmdLogs();
                    cmdLog.setId(System.currentTimeMillis());
                    cmdLog.setDeviceId(topic);
                    cmdLog.setMsg(cmd);
                    cmdLog.setScenarioId(device.getScenarioId());
                    cmdLog.setDeviceGroup(device.getDeviceGroup());
                    cmdLog.setSendTime(date);
                    cmdLog.setUserId(device.getUserId());
                    cmdLog.setRes_code(1);
                    cmdLog.setRes_msg("命令格式错误，转换失败，未发往设备");
                    cmdLogsRepository.save(cmdLog);
                    logger.info("命令发送日志: "+cmdLog);
                    return RESCODE.PARAM_MISSING.getJSONRES();
                }
            }
            try {
                content = new String(baKeyword, StandardCharsets.UTF_8);
            } catch (Exception e1) {
                logger.error("16进制命令格式错误，转换失败"+e1);
                CmdLogs cmdLog = new CmdLogs();
                cmdLog.setId(System.currentTimeMillis());
                cmdLog.setDeviceId(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(device.getUserId());
                cmdLog.setRes_code(1);
                cmdLog.setRes_msg("命令格式错误，转换失败，未发往设备");
                cmdLogsRepository.save(cmdLog);
                logger.info("命令发送日志: "+cmdLog);
                return RESCODE.PARAM_MISSING.getJSONRES();
            }
        }

        if (isMqtt) {
            try {
                //下发命令
                // 创建命令消息
                MqttMessage message = new MqttMessage(content.getBytes());
                // 设置消息的服务质量
                logger.info("准备发送命令， MQTT连接情况：" + MqttUtils.getInstance().isConnected());
                // 发布消息
                MqttUtils.getInstance().publish(topic + "/cmd", content.getBytes(), 2, false);
                logger.info("尝试向设备："+topic+"发送命令："+content);

                /**
                 * haizhe
                 * (1) 存入指令log，
                 * 此处不需要disconnect
                 */
                //储存命令下发日志
                CmdLogs cmdLog = new CmdLogs();
                cmdLog.setId(System.currentTimeMillis());
                cmdLog.setDeviceId(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(device.getUserId());
                cmdLog.setRes_code(0);
                cmdLog.setRes_msg("命令已发往设备");
                cmdLogsRepository.save(cmdLog);
                logger.info("命令发送日志: "+cmdLog);
                deviceRepository.save(device);
                //device.setStatus(0);
                //logger.info("命令日志已保存："+ cmdLog.toString());
                // 断开连接
                //MqttReceiveConfig.sendClient.disconnect();
                // 关闭客户端
                //sampleClient.close();
                //System.exit(0);
            } catch (Exception e) {
                //System.err.println("reason " + me.getReasonCode());
                //System.err.println("msg " + me.getMessage());
                //System.err.println("loc " + me.getLocalizedMessage());
                //System.err.println("cause " + me.getCause());
                //System.err.println("excep " + me);
                //me.printStackTrace();
                logger.error("向设备：" + topic + "下发命令失败");
                //logger.debug("reason " + me.getReasonCode());
                logger.debug("msg " + e.getMessage());
                logger.debug("loc " + e.getLocalizedMessage());
                logger.debug("cause " + e.getCause());
                logger.debug("excep " + e);
                CmdLogs cmdLog = new CmdLogs();
                cmdLog.setId(System.currentTimeMillis());
                cmdLog.setDeviceId(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(device.getUserId());
                cmdLog.setRes_code(1);
                cmdLog.setRes_msg("设备离线，命令未发送");
                cmdLogsRepository.save(cmdLog);
                logger.info("命令发送日志: "+cmdLog);
                //device.setStatus(0);
                deviceRepository.save(device);
                return RESCODE.SUCCESS.getJSONRES();
            }
        }
/*             } else {
                 logger.error("产品id未找到,向设备："+topic+"下发命令失败");
                 return RESCODE.DEVICE_ID_NOT_EXIST.getJSONRES();
             }*/


        //存储命令日志

        //接受回执信息
//      // 构建消息
//      Message<String> messages = MessageBuilder.withPayload(content).setHeader(MqttHeaders.TOPIC, topic).build();
//      // 发送消息
//      mqttHandler.handleMessage(messages);


        logger.info("向设备" + topic + "成功发送了命令：" + content+"("+cmd+")");
        return RESCODE.SUCCESS.getJSONRES();
    }

}