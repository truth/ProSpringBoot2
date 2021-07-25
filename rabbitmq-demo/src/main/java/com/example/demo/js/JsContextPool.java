package com.example.demo.js;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author lgp
 */
public class JsContextPool extends GenericObjectPool<JsContext>{

    public JsContextPool(PooledObjectFactory<JsContext> factory) {
        super(factory);
    }
    public JsContextPool(PooledObjectFactory<JsContext> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    public JsContextPool(PooledObjectFactory<JsContext> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
