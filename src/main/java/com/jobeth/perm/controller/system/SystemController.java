package com.jobeth.perm.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jobeth.perm.common.enums.ResultEnum;
import com.jobeth.perm.common.exception.ServerException;
import com.jobeth.perm.common.utils.JsonUtil;
import com.jobeth.perm.common.utils.JwtUtil;

import com.jobeth.perm.config.CommonConfigProperties;
import com.jobeth.perm.controller.BaseController;
import com.jobeth.perm.dto.UserDTO;
import com.jobeth.perm.po.User;
import com.jobeth.perm.service.UserService;
import com.jobeth.perm.service.impl.RedisService;
import com.jobeth.perm.vo.JsonResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/6/30 16:05
 */
@RestController
@RequestMapping("/sys")
@Slf4j
public class SystemController extends BaseController {

    private final UserService userService;
    private final RedisService redisService;
    @Autowired
    private CommonConfigProperties properties;

    public SystemController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public JsonResultVO<String> login(@RequestBody UserDTO userDTO) {
        log.info("用户登录-{}", JsonUtil.objectToJson(userDTO));
        User user = userService.getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        //用户不存在或密码不一致
        if (user == null || !user.getPassword().equals(userDTO.getPassword())) {
            throw new ServerException(ResultEnum.USER_LOGIN_FAIL);
        }
        log.info("用户校验成功，开始生成用户Token......");
        //生成JwtToken
        String token = JwtUtil.generateToken(user.getId().toString());
        if (token == null) {
            throw new ServerException(ResultEnum.INTERNAL_SERVER_ERROR);
        }
        //存入Redis
        String redisKey = properties.getJwtTokenPrefix() + user.getId();
        String redisToken = properties.getJwtTokenPrefix() + token;
        try {
            redisService.setExpire(redisKey, redisToken, properties.getJwtExpiration());
        } catch (Exception e) {
            log.error("用户登录-服务器内部错误：{}", e.getMessage());
            throw new ServerException(ResultEnum.INTERNAL_SERVER_ERROR);
        }
        return new JsonResultVO<>(redisToken);
    }

    @PostMapping("/test/get/{id}")
    public JsonResultVO<Integer> test(@PathVariable Integer id) {
        return new JsonResultVO<>(ResultEnum.NO_ERROR, id);
    }
}
