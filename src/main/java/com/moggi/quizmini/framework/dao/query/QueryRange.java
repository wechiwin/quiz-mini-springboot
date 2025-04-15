package com.moggi.quizmini.framework.dao.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRange<T> implements Serializable {

    public QueryRange() {

    }

    public QueryRange(T start, T end) {
        this.start = start;
        this.end = end;
    }

    T start;

    T end;
}
