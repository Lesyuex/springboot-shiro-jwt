package com.jobeth.perm.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 所有
 *
 * @author Jobeth
 * @since 2020/7/12 1:17
 */
public interface TreeVO<T> extends Serializable {
    /**
     * 获取主键
     *
     * @return 主键
     */
    Object getId();

    /**
     * 获取父节点
     *
     * @return 父节点
     */
    Object getParentId();

    /**
     * 设置子元素
     *
     * @param children children
     */
    void setChildren(List<T> children);
}
