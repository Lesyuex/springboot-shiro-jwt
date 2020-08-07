package com.jobeth.perm.service.impl;

import com.jobeth.perm.po.Blog;
import com.jobeth.perm.mapper.BlogMapper;
import com.jobeth.perm.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author Jobeth
 * @since 2020-06-30
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
