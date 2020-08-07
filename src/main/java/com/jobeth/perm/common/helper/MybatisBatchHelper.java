package com.jobeth.perm.common.helper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/7/27 9:25
 */
@Component
@Slf4j
public class MybatisBatchHelper {

    private final static SqlSessionFactory SQL_SESSION_FACTORY = SpringContextHelper.getBean(SqlSessionFactory.class);

    private MybatisBatchHelper() {
    }

    /**
     * 获取批量更新的SqlSession
     *
     * @return SqlSession
     */
    public static SqlSession openSession() {
        return SQL_SESSION_FACTORY.openSession(ExecutorType.BATCH, false);
    }

    /**
     * 获取Mapper
     *
     * @param sqlSession sqlSession
     * @param clazz      clazz
     * @param <M>        mapperType
     * @return mapper of mapperType
     */
    public static <M> M getMapper(SqlSession sqlSession, Class<M> clazz) {
        return sqlSession.getMapper(clazz);
    }

    /**
     * 批量插入数据
     *
     * @param dataList dataList
     */
    public static <M extends BaseMapper<T>, T> void batchInsert(SqlSession sqlSession, Class<M> clazz, List<T> dataList) {
        M mapper = sqlSession.getMapper(clazz);
        batchInsert(mapper, dataList);
    }

    /**
     * 批量插入数据
     *
     * @param dataList dataList
     */
    public static <M extends BaseMapper<T>, T> void batchInsert(M mapper, List<T> dataList) {
        for (T entity : dataList) {
            mapper.insert(entity);
        }
    }

    /**
     * 事务提交
     */
    public static void commit(SqlSession sqlSession) {
        sqlSession.commit();
        sqlSession.clearCache();
    }

    /**
     * 事务回滚
     */
    public static void rollback(SqlSession sqlSession) {
        try {
            sqlSession.rollback();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 会话关闭
     */
    public static void close(SqlSession sqlSession) {
        sqlSession.close();
    }

}
