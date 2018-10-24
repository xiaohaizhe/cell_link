package com.hydata.intelligence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydata.intelligence.platform.dto.Product;

/**
 * @author pyt
 * @createTime 2018年10月24日下午2:19:09
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

}

