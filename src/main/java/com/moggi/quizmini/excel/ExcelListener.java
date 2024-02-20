package com.moggi.quizmini.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.moggi.quizmini.framework.dao.EnhanceMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 从Excel文件流中分批导入数据到库中
 * EasyExcel参考文档：https://easyexcel.opensource.alibaba.com/docs/current/quickstart/read
 *
 * @param <T>
 * @author wangrubin
 * @date 2022-08-02
 */
@Slf4j
public abstract class ExcelListener<T> implements ReadListener<T> {

    // @Autowired
    // private EnhanceMapper<T> enhanceMapper;

    /**
     * 缓存大小
     */
    private static final int BATCH_SIZE = 900;

    /**
     * 缓存数据
     */
    private List<T> cacheList = new ArrayList<>(BATCH_SIZE);

    @Override
    public void invoke(T po, AnalysisContext analysisContext) {
        cacheList.add(po);
        if (cacheList.size() >= BATCH_SIZE) {
            log.info("完成一批Excel记录的导入，条数为：{}", cacheList.size());
            getMapper().insertBatch(cacheList);
            cacheList = new ArrayList<>(BATCH_SIZE);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        getMapper().insertBatch(cacheList);
        log.info("完成最后一批Excel记录的导入，条数为：{}", cacheList.size());
    }

    /**
     * 获取批量插入的Mapper
     *
     * @return 批量插入的Mapper
     */
    protected abstract EnhanceMapper<T> getMapper();
}