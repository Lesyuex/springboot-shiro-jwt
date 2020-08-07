package com.jobeth.perm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jobeth.perm.common.Constant;
import com.jobeth.perm.common.enums.ResultEnum;
import com.jobeth.perm.common.utils.TreeUtil;
import com.jobeth.perm.po.Permission;
import com.jobeth.perm.service.PermissionService;
import com.jobeth.perm.vo.MenuVO;
import com.jobeth.perm.vo.JsonResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    public JsonResultVO<List<MenuVO>> list() {
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.lambda().eq(Permission::getType, 1);
        List<Permission> list = permissionService.list(permissionQueryWrapper);
        List<MenuVO> menuVOList = new ArrayList<>();
        for (Permission permission : list) {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(permission, menuVO);
            menuVOList.add(menuVO);
        }
        List<MenuVO> treeVOList = TreeUtil.generateTree(menuVOList,Constant.ROOT_MENU);
        return new JsonResultVO<>(treeVOList);
    }
}
