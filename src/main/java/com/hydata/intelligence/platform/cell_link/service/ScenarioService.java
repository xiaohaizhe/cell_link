package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.DeviceGroup;
import com.hydata.intelligence.platform.cell_link.entity.Scenario;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.DeviceGroupRepository;
import com.hydata.intelligence.platform.cell_link.repository.ScenarioRepository;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.Constants;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName ScenarioService
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/27 16:17
 * @Version
 */
@Service
@Transactional
public class ScenarioService {
    @Autowired
    private ScenarioRepository scenarioRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OplogService oplogService;

    private static Logger logger = LogManager.getLogger(ScenarioService.class);

    public JSONObject getScenario(Scenario scenario) {
        JSONObject object = new JSONObject();
        object.put("scenarioId", scenario.getScenarioId());
        object.put("scenarioName", scenario.getScenarioName());
        object.put("description", scenario.getDescription());
        return object;
    }

    /**
     * 新建场景
     *
     * @param scenario 场景
     * @param br       验证
     * @return 结果
     */
    @CacheEvict(cacheNames = {"scenario","log"}, allEntries = true)
    public JSONObject add(Scenario scenario, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            logger.info(scenario);
            if (scenario.getUser() != null && scenario.getUser().getUserId() != null) {
                Optional<User> userOptional = userRepository.findById(scenario.getUser().getUserId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    List<Scenario> scenarioList = scenarioRepository.findByScenarioNameAndUser(scenario.getScenarioName(),
                            user.getUserId());
                    if (scenarioList.size() > 0) {
                        return RESCODE.SCENARIO_EXIST.getJSONRES();
                    }
                    Scenario scenario1 = scenarioRepository.save(scenario);
                    oplogService.scenario(user.getUserId(), "添加场景:" + scenario1.getScenarioName());
                    return RESCODE.SUCCESS.getJSONRES(scenario1);
                }
            }
            return RESCODE.USER_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    /**
     * 修改场景
     *
     * @param scenario 场景
     * @param br       验证
     * @return 结果
     */
    @CacheEvict(cacheNames = {"scenario", "datastream","log"}, allEntries = true)
    public JSONObject update(Scenario scenario, BindingResult br) {
        JSONObject object = BindingResultService.dealWithBindingResult(br);
        if ((Integer) object.get(Constants.RESPONSE_CODE_KEY) == 0) {
            if (scenario.getScenarioId() != null) {
                Optional<Scenario> scenarioOptional = scenarioRepository.findById(scenario.getScenarioId());
                if (scenarioOptional.isPresent()) {
                    Scenario scenarioOld = scenarioOptional.get();
                    scenarioOld.setScenarioName(scenario.getScenarioName());
                    scenarioOld.setDescription(scenario.getDescription());
                    Scenario scenarioNew = scenarioRepository.saveAndFlush(scenarioOld);
                    oplogService.scenario(scenarioOld.getUser().getUserId(), "修改场景:" + scenarioNew.getScenarioName());
                    return RESCODE.SUCCESS.getJSONRES(getScenario(scenarioNew));
                }
            }
            return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
        }
        return object;
    }

    /**
     * 删除场景
     *
     * @param scenarioId 场景iid
     * @return 结果
     */
    @Transactional
    @CacheEvict(cacheNames = {"scenario", "datastream","log"}, allEntries = true)
    public JSONObject delete(Long scenarioId) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findById(scenarioId);
        if (scenarioOptional.isPresent()) {
            Scenario scenario = scenarioOptional.get();
            List<DeviceGroup> deviceGroupList = deviceGroupRepository.findByScenario(scenarioId);
            for (DeviceGroup deviceGroup : deviceGroupList) {
                deviceGroupRepository.delete(deviceGroup);
            }
            scenarioRepository.deleteByScenarioId(scenarioId);
            oplogService.scenario(scenario.getUser().getUserId(), "成功删除场景:" + scenario.getScenarioName());
            return RESCODE.SUCCESS.getJSONRES();
        }
        return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
    }

    /**
     * 根据场景id获取场景详情
     *
     * @param scenarioId 场景id
     * @return 结果
     */
    @Cacheable(cacheNames = "scenario", key = "#p0")
    public JSONObject findById(Long scenarioId) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findById(scenarioId);
        if (scenarioOptional.isPresent()) {
            Scenario scenario = scenarioOptional.get();
            oplogService.scenario(scenario.getUser().getUserId(), "根据id查看场景" + scenario.getScenarioName() + "详情");
            return RESCODE.SUCCESS.getJSONRES(getScenario(scenario));
        }
        return RESCODE.SCENARIO_NOT_EXIST.getJSONRES();
    }

    /**
     * 获取用户下场景列表
     *
     * @param userId 用户id
     * @return 结果
     */
    @Cacheable(cacheNames = "scenario", keyGenerator = "myKeyGenerator")
    public JSONObject findListByUser(Long userId) {
        List<Scenario> scenarioList = scenarioRepository.findByUser(userId);
        List<JSONObject> scenarios = new ArrayList<>();
        for (Scenario scenario : scenarioList) {
            scenarios.add(getScenario(scenario));
        }
        return RESCODE.SUCCESS.getJSONRES(scenarios);
    }

    /**
     * 根据用户id和场景名模糊查询
     * 场景分页数据
     *
     * @param userId
     * @param page
     * @param number
     * @param sorts
     * @param scenarioName
     * @return
     */
    @Cacheable(cacheNames = "scenario", keyGenerator = "myKeyGenerator")
    public JSONObject findPageByUser(Long userId, Integer page, Integer number, String sorts, String scenarioName) {
        if (userRepository.existsById(userId)) {
            Pageable pageable = PageUtils.getPage(page, number, sorts);
            Page<Scenario> scenarioPage = scenarioRepository.findByScenarioNameAndUser(scenarioName, userId, pageable);
            List<JSONObject> scenarioList = new ArrayList<>();
            for (Scenario scenario : scenarioPage.getContent()) {
                scenarioList.add(getScenario(scenario));
            }
            return RESCODE.SUCCESS.getJSONRES(scenarioList, scenarioPage.getTotalPages(), scenarioPage.getTotalElements());
        }
        return RESCODE.USER_NOT_EXIST.getJSONRES();
    }
}
