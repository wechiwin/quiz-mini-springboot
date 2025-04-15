package com.moggi.quizmini.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moggi.quizmini.dto.FolderDTO;
import com.moggi.quizmini.dto.FolderQueryDTO;
import com.moggi.quizmini.entity.Folder;

import java.util.List;

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

    List<FolderDTO> searchList(FolderQueryDTO query);

}
