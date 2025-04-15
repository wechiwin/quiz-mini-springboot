package com.moggi.quizmini.dto;

import com.moggi.quizmini.framework.dao.query.QueryOp;
import com.moggi.quizmini.framework.dao.query.QueryType;
import com.moggi.quizmini.framework.pojo.QueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FolderQueryDTO extends QueryDTO {

    private static final long serialVersionUID = 1L;

    private Integer foPkid;

    @QueryType(QueryOp.like)
    private String foName;

    private String createTime;

    private String modifyTime;

}
