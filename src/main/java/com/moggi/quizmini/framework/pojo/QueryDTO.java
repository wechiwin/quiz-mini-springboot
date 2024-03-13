package com.moggi.quizmini.framework.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;


/**
 * 查询类
 *
 * @param
 */
@Data
public class QueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 翻页参数
     */
    Page page;

    public Page getPage() {
        if (page == null) {
            page = new Page();
        }

        return page;
    }
}
