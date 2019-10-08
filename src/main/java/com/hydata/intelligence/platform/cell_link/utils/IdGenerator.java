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
        long time = System.currentTimeMillis();
        long ii = Math.round(Math.random() * 1000);
        long jj = Math.round(Math.random() * 1000);
        String s = time + Long.toString(ii) + jj;
        return Long.valueOf(s);
    }


    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {

    }
}
