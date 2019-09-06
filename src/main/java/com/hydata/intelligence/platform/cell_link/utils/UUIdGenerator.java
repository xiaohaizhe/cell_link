package com.hydata.intelligence.platform.cell_link.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * @ClassName IdGenerator
 * @Description TODO
 * @Author pyt
 * @Date 2019/1/9 11:03
 * @Version
 */
public class UUIdGenerator implements Configurable, IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }


    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {

    }
}
