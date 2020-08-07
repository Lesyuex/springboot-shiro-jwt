package com.jobeth.perm.common.utils;


import com.jobeth.perm.vo.TreeVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 树状数据工具类
 *
 * @author Jobeth
 * @since 2020/4/24 21:27
 */
@Slf4j
public class TreeUtil {

    /**
     * 生成树状数据
     *
     * @return 树状数据
     */
    public static <T extends TreeVO<T>> List<T> generateTree(List<T> allList, String rootId) {
        //存放树状菜单
        ArrayList<T> treeList = new ArrayList<>();
        //找到根节点
        for (T t : allList) {
            if (t.getParentId().toString().equals(rootId)) {
                treeList.add(t);
            }
        }
        //找到根节点的孩子节点
        for (T t : treeList) {
            List<T> childrenList = getChildrenList(allList, t.getId().toString());
            t.setChildren(childrenList);
        }
        return treeList;
    }

    /**
     * 生成某个菜单的子菜单
     *
     * @param parentId 父菜单Id
     * @return 子菜单集合
     */
    public static <T extends TreeVO<T>> List<T> getChildrenList(List<T> allList, String parentId) {
        //存放次级菜单
        ArrayList<T> children = new ArrayList<>();
        //找到次级菜单
        for (T t : allList) {
            if (t.getParentId().toString().equals(parentId)) {
                children.add(t);
            }
        }
        //未找到次级菜单,直接返回
        if (children.isEmpty()) {
            return new ArrayList<>();
        }
        //找到次级菜单的次级菜单
        for (T t : children) {
            List<T> childrenList = getChildrenList(allList, t.getId().toString());
            t.setChildren(childrenList);
        }
        return children;
    }
}
