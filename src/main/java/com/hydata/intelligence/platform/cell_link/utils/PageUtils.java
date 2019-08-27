package com.hydata.intelligence.platform.cell_link.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PageUtils
 * @Description TODO
 * @Author pyt
 * @Date 2019/8/27 15:52
 * @Version
 */
public class PageUtils {
    public static Pageable getPage(Integer page, Integer number, String sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sorts.trim().equals("")) {
            return new PageRequest(page - 1, number);
        } else {
            for (String sort : sorts.split(",")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, sort));
            }
            return new PageRequest(page - 1, number, new Sort(orders));
        }
    }
}
