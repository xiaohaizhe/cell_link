package com.hydata.intelligence.platform.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * @ClassName IdGenerator
 * @Description TODO
 * @Author pyt
 * @Date 2019/1/9 11:03
 * @Version
 */
public class IdGenerator implements Configurable, IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Long time = System.currentTimeMillis();
        Long ii = Math.round(Math.random() * 10000);
        String s = time.toString()+ii.toString();
        return Long.valueOf(s);
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {

    }
}
