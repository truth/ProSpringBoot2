package com.example.demo.js;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

public class JsContextFactory extends BasePooledObjectFactory<JsContext> {

    private AtomicInteger idCount = new AtomicInteger(1);
    @Override
    public JsContext create() throws Exception {
        return new JsContext();
    }

    @Override
    public PooledObject<JsContext> wrap(JsContext conn) {
        //包装实际对象
        return new DefaultPooledObject<>(conn);
    }
}
