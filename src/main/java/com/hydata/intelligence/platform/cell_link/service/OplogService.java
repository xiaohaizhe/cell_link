package com.hydata.intelligence.platform.cell_link.service;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.entity.Oplog;
import com.hydata.intelligence.platform.cell_link.entity.User;
import com.hydata.intelligence.platform.cell_link.entity.dict.OplogType;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import com.hydata.intelligence.platform.cell_link.repository.OplogRepository;
import com.hydata.intelligence.platform.cell_link.repository.OplogTypeRepository;
import com.hydata.intelligence.platform.cell_link.repository.UserRepository;
import com.hydata.intelligence.platform.cell_link.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName OplogService
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/9 10:58
 * @Version
 */
@Service
@Transactional
public class OplogService {
    @Autowired
    private OplogRepository oplogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OplogTypeRepository oplogTypeRepository;

    public void addOplog(Long userId, Integer otId, String msg) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<OplogType> oplogTypeOptional = oplogTypeRepository.findById(otId);
        if (userOptional.isPresent() && oplogTypeOptional.isPresent()) {
            User user = userOptional.get();
            OplogType oplogType = oplogTypeOptional.get();
            Oplog oplog = new Oplog();
            oplog.setUser(user);
            oplog.setOplogType(oplogType);
            oplog.setOtType(oplogType.getType());
            oplog.setMsg(msg);
            oplogRepository.save(oplog);
        }
    }

    public void login(Long userId, String msg) {
        addOplog(userId, 1, msg);
    }

    public void logout(Long userId, String msg) {
        addOplog(userId, 2, msg);
    }

    public void user(Long userId, String msg) {
        addOplog(userId, 3, msg);
    }

    public void scenario(Long userId, String msg) {
        addOplog(userId, 4, msg);
    }

    public void deviceGroup(Long userId, String msg) {
        addOplog(userId, 5, msg);
    }

    public void device(Long userId, String msg) {
        addOplog(userId, 6, msg);
    }

    public void app(Long userId, String msg) {
        addOplog(userId, 7, msg);
    }

    public void event(Long userId, String msg) {
        addOplog(userId, 9, msg);
    }

    public JSONObject getOplog(Oplog oplog){
        JSONObject object = new JSONObject();
        object.put("oplogType",oplog.getOtType());
        object.put("msg",oplog.getMsg());
        object.put("created",oplog.getCreated());
        return object;
    }

    @Cacheable(cacheNames = "log",keyGenerator = "myKeyGenerator")
    public JSONObject getOplogList(Long userId){
        List<Oplog> oplogList = oplogRepository.findByUser(userId);
        List<JSONObject> oplogs = new ArrayList<>();
        for (Oplog oplog : oplogList){
            oplogs.add(getOplog(oplog));
        }
        return RESCODE.SUCCESS.getJSONRES(oplogs);
    }

    @Cacheable(cacheNames = "log",keyGenerator = "myKeyGenerator")
    public JSONObject getOplogPage(Long userId,Integer page,Integer number,String sorts){
        Pageable pageable = PageUtils.getPage(page, number, sorts);
        Page<Oplog> oplogPage = oplogRepository.findByUser(userId,pageable);
        List<JSONObject> oplogs = new ArrayList<>();
        for (Oplog oplog:oplogPage.getContent()){
            oplogs.add(getOplog(oplog));
        }
        return RESCODE.SUCCESS.getJSONRES(oplogs,oplogPage.getTotalPages(),oplogPage.getTotalElements());
    }
}
