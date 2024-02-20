package com.moggi.quizmini.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moggi.quizmini.entity.Folder;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wechiwin
 * @since 2024-02-07
 */
public interface FolderService extends IService<Folder> {

    boolean add(String folderName);

}
