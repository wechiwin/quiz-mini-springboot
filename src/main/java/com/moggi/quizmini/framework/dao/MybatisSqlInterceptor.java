package com.moggi.quizmini.framework.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 在MyBatis中可被拦截的类型有四种(按照拦截顺序)
 * Executor：拦截执行器的方法。
 * ParameterHandler：拦截参数的处理。
 * ResultHandler：拦截结果集的处理。
 * StatementHandler：拦截Sql语法构建的处理，绝大部分我们是在这里设置我们的拦截器
 *
 * @Intercepts({
 * @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
 * @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
 * @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
 * })
 * @Intercepts：标识该类是一个拦截器
 * @Signature：指明自定义拦截器需要拦截哪一个类型，哪一个方法； type：对应四种类型中的一种；
 * method：对应接口中的哪类方法（因为可能存在重载方法）；
 * args：对应哪一个方法；
 * <p>
 * https://juejin.cn/post/7027633293684113421
 * https://cloud.tencent.com/developer/article/1830883
 * https://www.kuangstudy.com/bbs/1399332546449350657
 */
@Slf4j
@Component
@Intercepts(@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MybatisSqlInterceptor implements Interceptor {

    /**
     * 执行拦截逻辑的方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截sql
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        // 这里的参数应该不止一个把？debug看一下
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        // // 注解拦截 如果带了QuerySort注解，则判断queryDto下的sort的是否有值
        // List<OrderByParam> orderByParams = new ArrayList<>();
        // Class<?> paramClass = parameterObject.getClass();
        // for (Field f : paramClass.getDeclaredFields()) {
        //     // 判断这个字段是否有MyField注解
        //     if (f.isAnnotationPresent(OrderBy.class)) {
        //         OrderBy a = f.getAnnotation(OrderBy.class);
        //         orderByParams.add(new OrderByParam()
        //                 .setName(f.getName())
        //                 .setDescription(a.description())
        //                 .setPriority(a.priority())
        //                 .setSort(a.sort())
        //         );
        //     }
        // }
        // // 重新设置sql 将sort组合起来，拼在sql 后面
        // if (orderByParams.size() > 0 && StringUtils.isNoneBlank(sql)) {
        //     int limit = sql.toUpperCase().lastIndexOf("LIMIT");
        //     String OrderBy = OrderByParam.renderOrderBy(orderByParams);
        //     if (limit != -1) {
        //         sql = sql.substring(0, limit) + OrderBy + sql.substring(limit, sql.length());
        //     } else {
        //         sql += OrderBy;
        //     }
        //     BoundSql newBoundSql = new BoundSql(statement.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        //     MappedStatement newMs = copyFromMappedStatement(statement, new BoundSqlSqlSource(newBoundSql));
        //     for (ParameterMapping mapping : boundSql.getParameterMappings()) {
        //         String prop = mapping.getProperty();
        //         if (boundSql.hasAdditionalParameter(prop)) {
        //             newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
        //         }
        //     }
        //     args[0] = newMs;
        // }
        return invocation.proceed();
    }

    /**
     * 决定是否触发 intercept方法
     *
     * @param obj
     * @return
     */
    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties arg0) {
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}