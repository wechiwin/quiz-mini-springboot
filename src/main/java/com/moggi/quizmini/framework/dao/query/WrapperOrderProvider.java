// package com.moggi.quizmini.framework.dao.query;
//
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//
// public class WrapperOrderProvider implements OrderProvider {
//
//     QueryWrapper wrapper;
//
//     public WrapperOrderProvider(QueryWrapper wrapper) {
//         this.wrapper = wrapper;
//     }
//
//     @Override
//     public void orderByAsc(String field) {
//         wrapper.orderByAsc(field);
//     }
//
//     @Override
//     public void orderByDesc(String field) {
//         wrapper.orderByDesc(field);
//     }
//
//     @Override
//     public boolean supportMultiField() {
//         return true;
//     }
// }