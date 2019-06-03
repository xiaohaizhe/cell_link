package com.hydata.intelligence.platform.controller.externalInterface;

import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.dto.User;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.repositories.UserRepository;
import com.hydata.intelligence.platform.service.DeviceService;
import com.hydata.intelligence.platform.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @ClassName ProductExternalController
 * @Description TODO
 * @Author pyt
 * @Date 2019/6/3 16:25
 * @Version
 */
@RestController
@RequestMapping("/api/external/product")
public class ProductExternalController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private HttpService httpSevice;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/{product_id}/devicelist",method= RequestMethod.GET)
    public JSONObject getDeviceList(@PathVariable Long product_id, HttpServletRequest request){
        String api_key = httpSevice.resolveHttpHeader(request);
        Optional<Product> productOptional = productRepository.findById(product_id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            Optional<User> userOptional =userRepository.findById(product.getUserId());
            if(userOptional.isPresent()){
                if(userOptional.get().getDefaultKey().equals(api_key)){
                    return deviceService.getByProductId(product_id);
                }
            }
            return RESCODE.API_KEY_ERROR.getJSONRES();
        }else{
            return RESCODE.PRODUCT_ID_NOT_EXIST.getJSONRES();
        }
    }
}
