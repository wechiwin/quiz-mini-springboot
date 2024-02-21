package com.moggi.quizmini.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelReadListener<T> implements ReadListener<T> {
    private List<T> cacheList = new ArrayList<>();

    @Override
    public void invoke(T po, AnalysisContext analysisContext) {
        cacheList.add(po);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    public List<T> getCacheList() {
        return cacheList;
    }
}