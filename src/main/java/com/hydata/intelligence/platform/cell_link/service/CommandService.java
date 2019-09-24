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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class CommandService {
    @Autowired
    private DeviceGroupService deviceGroupService;
    @Autowired
    private ScenarioService scenarioService;
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
     * @param api_key
     * @return
     */
    public JSONObject externalSend(long device_id, String content, int type, String api_key) {
        JSONObject object = new JSONObject();
        Optional<Device> deviceOptional = deviceRepository.findById(device_id);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            DeviceGroup dg = device.getDeviceGroup();
            if (dg.getProtocol().getProtocolId().equals(1)) {
                //TODO:鉴权？
                return send(device_id, content, type, Long.parseLong(api_key));
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
     * @param userid:用户id：用于记录日志
     * @return
     */

    public JSONObject send(long topic, String content, int type,long userid) {
        Date date = new Date();
        boolean isMqtt = false;

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
            cmdLog.setDevice_id(topic);
            cmdLog.setMsg(content);
            Optional<Device> deviceOptional = deviceRepository.findById(topic);
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();
                cmdLog.setScenarioId(device.getScenarioId());
            } else {
                cmdLog.setScenarioId(0);
            }
            cmdLog.setSendTime(date);
            cmdLog.setUserId(userid);
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
            if (dg.getProtocol().getProtocolId().equals(1)) {
                isMqtt = true;
            } else {
                logger.info("产品协议不支持命令下发");
                return RESCODE.NO_CHANGES.getJSONRES();
            }
        }else {
            logger.error("产品信息未找到" + topic + "，命令发送失败");
            return RESCODE.DEVICE_NOT_EXIST.getJSONRES();
        }

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
                    cmdLog.setDevice_id(topic);
                    cmdLog.setMsg(cmd);
                    cmdLog.setScenarioId(device.getScenarioId());
                    cmdLog.setDeviceGroup(device.getDeviceGroup());
                    cmdLog.setSendTime(date);
                    cmdLog.setUserId(userid);
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
                cmdLog.setDevice_id(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(userid);
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

                /**
                 * haizhe
                 * (1) 存入指令log，
                 * 此处不需要disconnect
                 */
                //储存命令下发日志
                CmdLogs cmdLog = new CmdLogs();
                cmdLog.setId(System.currentTimeMillis());
                cmdLog.setDevice_id(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(userid);
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
                cmdLog.setDevice_id(topic);
                cmdLog.setMsg(cmd);
                cmdLog.setScenarioId(device.getScenarioId());
                cmdLog.setDeviceGroup(device.getDeviceGroup());
                cmdLog.setSendTime(date);
                cmdLog.setUserId(userid);
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