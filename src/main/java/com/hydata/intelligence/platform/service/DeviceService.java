package com.hydata.intelligence.platform.service;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.DeviceRepository;
import com.hydata.intelligence.platform.repositories.ProductRepository;

/**
 * @author pyt
 * @createTime 2018年10月31日上午11:39:02
 */
@Transactional
@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private static Logger logger = LogManager.getLogger(DeviceService.class);
	
	@SuppressWarnings("deprecation")
	public Page<Device> showAllByProductId(Integer product_id,Integer page,Integer number){
		Pageable pageable = new PageRequest(page, number, Sort.Direction.DESC,"id");
		return deviceRepository.queryByProductId(product_id, pageable);
	}
	/**
	 * 添加设备
	 * @param device
	 * @return
	 */
	public Map<String, Object> addDevice(Device device){
		Optional<Product> productOptional = productRepository.findById(device.getProductId());
		logger.debug("检查添加设备的产品id是否存在");
		if(productOptional.isPresent()) {
			logger.debug("产品id存在");
			Optional<Device> deviceOptional = deviceRepository.findByDevice_sn(device.getProductId(), device.getDevice_sn());
			logger.debug("检查添加设备的鉴权信息是否重复");
			if(deviceOptional.isPresent()) {
				return RESCODE.AUTH_INFO_EXIST.getJSONRES();
			}
			logger.debug("产品:"+device.getProductId()+"下鉴权信息："+ device.getDevice_sn()+"不重复");
			device.setProtocolId(productOptional.get().getProtocolId());
			device.setCreateTime(new Date());
			Device deviceReturn= deviceRepository.save(device);
			return RESCODE.SUCCESS.getJSONRES(deviceReturn);
		}
		logger.debug("产品id不存在");
		return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 在产品中根据设备id或设备名称查询
	 * @param product_id
	 * @param page
	 * @param number
	 * @param device_snOrName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Page<Device> queryByDevice_snOrName(Integer product_id,Integer page,Integer number,String device_idOrName){
		Pageable pageable = new PageRequest(page-1, number, Sort.Direction.DESC,"id");
		return deviceRepository.findByDevice_idOrName(product_id,device_idOrName,pageable);
	}
	/**
	 * 修改设备信息（设备名称、设备鉴权码、icon）
	 * @param device
	 * @return
	 */
	public Map<String, Object> modifyDevice(Device device){
		Optional<Device> devOptional = deviceRepository.findById(device.getId());
		if(devOptional.isPresent()) {
			if(devOptional.get().getDevice_sn().equals(device.getDevice_sn())==false) {
				Optional<Device> deviceOptional = deviceRepository.findByDevice_sn(device.getProductId(), device.getDevice_sn());
				if(deviceOptional.isPresent()) {
					return RESCODE.AUTH_INFO_EXIST.getJSONRES();
				}else {
					devOptional.get().setDevice_sn(device.getDevice_sn());
				}
			}
			devOptional.get().setName(device.getName());
			devOptional.get().setModifyTime(new Date());
			Device deviceReturn= deviceRepository.save(devOptional.get());
			return RESCODE.SUCCESS.getJSONRES(deviceReturn);			
		}
		return RESCODE.ID_NOT_EXIST.getJSONRES();
	}
	/**
	 * 删除设备
	 * 1.数据流触发器（未完成）
	 * 2.设备（完成）
	 * @param id
	 * @return
	 */
	public Map<String, Object> deleteDevice(Integer id){
		deviceRepository.deleteById(id);
		return null;
	}
}

