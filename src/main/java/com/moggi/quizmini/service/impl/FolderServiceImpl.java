package com.moggi.quizmini.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.mapper.FolderMapper;
import com.moggi.quizmini.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
    @Autowired
    private FolderMapper mapper;

    @Override
    public boolean add(String folderName) {
        Folder folder = new Folder();
        folder.setFoName(folderName);
        return mapper.insert(folder) > 0;
    }
}
