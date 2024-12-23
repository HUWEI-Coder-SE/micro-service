package com.store.cloud.search.canal;

import cn.throwx.canal.gule.model.ModelTable;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import cn.throwx.canal.gule.support.processor.CanalBinlogEventProcessorFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author FrozenWatermelon
 */
public class StorecloudCanalBinlogEventProcessorFactory implements CanalBinlogEventProcessorFactory {

    private final ConcurrentMap<ModelTable, List<BaseCanalBinlogEventProcessor<?>>> cache = new ConcurrentHashMap<>(16);

    @Override
    public void register(ModelTable modelTable, BaseCanalBinlogEventProcessor<?> processor) {
        synchronized(this.cache) {
            this.cache.putIfAbsent(modelTable, new LinkedList<>());
            this.cache.get(modelTable).add(processor);
        }
    }

    @Override
    public List<BaseCanalBinlogEventProcessor<?>> get(ModelTable modelTable) {
        return this.cache.get(modelTable);
    }

    private StorecloudCanalBinlogEventProcessorFactory() {
    }

    public static StorecloudCanalBinlogEventProcessorFactory of() {
        return new StorecloudCanalBinlogEventProcessorFactory();
    }
}
