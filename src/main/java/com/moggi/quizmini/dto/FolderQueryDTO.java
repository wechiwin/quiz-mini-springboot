package com.moggi.quizmini.dto;

import com.moggi.quizmini.framework.pojo.QueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FolderQueryDTO extends QueryDTO {

    private static final long serialVersionUID = 1L;

    private Integer foPkid;

    private String foName;

    private String createTime;

    private String modifyTime;

}
