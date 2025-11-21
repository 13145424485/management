package com.doc.web.login.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doc.jwt.JwtUtils;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.login.entity.InfoParam;
import com.doc.web.login.entity.LoginParm;
import com.doc.web.login.entity.LoginResult;
import com.doc.web.login.entity.UserInfo;
import com.doc.web.member.entity.Member;
import com.doc.web.member.service.MemberService;
import com.doc.web.sys_menu.entity.MakeMenuTree;
import com.doc.web.sys_menu.entity.RouterVo;
import com.doc.web.sys_menu.entity.SysMenu;
import com.doc.web.sys_menu.service.SysMenuService;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.service.SysUserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login/")

public class LoginController {



    @Autowired
    private DefaultKaptcha defaultKaptcha;

    //生成验证码
    @PostMapping("/image")
    public ResultVo imgaeCode(HttpServletRequest request) throws Exception {
        //获取验证码字符
        String text = defaultKaptcha.createText();
        //将验证码存入session
        HttpSession session = request.getSession();
        session.setAttribute("code", text);
        //将验证码图片返回给前端
        BufferedImage bufferedImage = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStram = null;
        try {
            outputStram = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStram);
            String base64 = Base64.getEncoder().encodeToString(outputStram.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
           ResultVo resultVo = new ResultVo("生成成功", 200, captchaBase64);
           return  resultVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
           try {
               if (outputStram != null) {
                   outputStram.close();
               }
           } catch (IOException e) {
                e.printStackTrace();
           }
        }
        return null;
    }
    @Autowired
    MemberService memberService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    JwtUtils jwtUtils;

    //登录
    @PostMapping("/login")
    public ResultVo login(HttpServletRequest request,@RequestBody LoginParm param) {
        //参数校验
        if (StringUtils.isEmpty(param.getUsername()) ||  StringUtils.isEmpty(param.getPassword()) ||  StringUtils.isEmpty(param.getUserType()) ||  StringUtils.isEmpty(param.getCode())) {
            return ResultUtils.error("用户名、密码、验证码或用户类型不能为 空!");
        }
        
        //获取session
        HttpSession session = request.getSession();
        //获取验证码
        String code = (String) session.getAttribute("code");
        //判断验证码是否存在
        if (code == null) {
            return ResultUtils.error("验证码已过期，请刷新验证码");
        }
        //判断验证码是否正确
        if (!code.equals(param.getCode())) {
            return ResultUtils.error("验证码错误");
        }
        
        //获取用户登录时输入的原始密码（字符串），通过 getBytes() 转为字节数组（MD5 加密的输入是字节流）。
        //DigestUtils 是 Apache Commons Codec 工具类（项目已引入该依赖），提供便捷的加密方法。
        String password = DigestUtils.md5DigestAsHex(param.getPassword().getBytes());

        //用户类型判断
        String token = null;
        if ("1".equals(param.getUserType())) { //会员
            //构造查询条件
            QueryWrapper<Member> query = new QueryWrapper<>();
            query.lambda().eq(Member::getUsername, param.getUsername()).eq(Member::getPassword, param.getPassword());
            Member one = memberService.getOne(query);
            //通过 MyBatis-Plus 的 getOne 方法，根据构建的查询条件（query）从数据库中查询唯一一条会员记录
            if (one == null) {
                return ResultUtils.error("用户或密码错误");
            }
            //生成token
            //生成用户身份令牌（JWT Token,并将用户关键信息嵌入令牌中，作为用户后续访问系统的 "身份凭证"
            //作为用户登录成功后的 "通行证"，前端会存储这个令牌，后续请求系统接口时携带令牌，证明 "我是已登录的合法用户
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(one.getMemberId()));
            map.put("username", one.getUsername());
            map.put("userType", param.getUserType());
            //调用 JwtUtils 工具类的 generateToken 方法，传入上面准备的用户信息 map，生成一个完整的 JWT 令牌字符串
            token = jwtUtils.generateToken(map);
            System.out.println("token: " + token);
            //返回登录成功信息
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(one.getMemberId());
            result.setUername(one.getName());
            result.setUserType(param.getUserType());
            return ResultUtils.success("登陆成功", result);

        } else if ("2".equals(param.getUserType())) {
            //员工
            QueryWrapper<SysUser> query = new QueryWrapper<>();
            query.lambda().eq(SysUser::getUsername, param.getUsername()).eq(SysUser::getPassword, password);
            SysUser one = sysUserService.getOne(query);
            if (one == null) {
                return ResultUtils.error("用户名或密码错误!");
            }
            //生成token
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(one.getUserId()));
            map.put("username", one.getUsername());
            map.put("userType", param.getUserType());
            token = jwtUtils.generateToken(map);
            System.out.println("token: " + token);
            //返回登录信息
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(one.getUserId());
            result.setUername(one.getNickName());
            result.setUserType(param.getUserType());
            return ResultUtils.success("登陆成功", result);

        }
        else{
            return ResultUtils.error("用户类型错误！");
        }
    }

    @Autowired
    private SysMenuService sysMenuService;
    //用户信息查询接口（/getInfo） 的实现
    // 主要功能是根据用户类型（会员 / 员工）查询对应的用户基本信息和权限标识（菜单 code）
    // 为前端进行权限控制（如按钮级权限判断）提供数据支持

    //查询用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(InfoParam param){
        //参数校验
        if (param == null || param.getUserId() == null || param.getUserType() == null) {
            return ResultUtils.error("参数不完整，请检查userId和userType");
        }
        
        UserInfo userInfo = new UserInfo();
        if("1".equals(param.getUserType())){//会员
            //根据会员id查询权限字段
            List<SysMenu> menuList = sysMenuService.getMenuByMemberId(param.getUserId());
            //获取去全部的code字段
            List<String> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                    .stream()
                    // 是 Java 8 Stream 流中的映射操作，核心作用是将流中的每个 SysMenu 对象转换为其 code
                    //现从 “菜单对象” 到 “权限标识字符串” 的转换
                    .map(item -> item.getCode())
                    .filter(item -> item != null)
                    //collect(Collectors.toList()) 是终端操作，核心作用是将流（Stream）中的元素收集并转换为一个 List 集合，是流处理的 “收尾” 步骤。
                    //Collectors.toList()：java.util.stream.Collectors 工具类提供的静态方法，返回一个收集器（Collector），
                    // 该收集器的作用是将流中的元素按顺序存入一个 List 集合中
                    .collect(Collectors.toList());
            //转为数组
            String[] strings = collect.toArray(new String[collect.size()]);
            //查询用户信息
            Member member = memberService.getById(param.getUserId());
            //设计返回信息
            userInfo.setName(member.getName());
            userInfo.setUserId(member.getMemberId());
            userInfo.setPermissions(strings);
            return  ResultUtils.success("查询成功",userInfo);
            }else if("2".equals(param.getUserType())){ //员工
            //查询员工信息
            SysUser user = sysUserService.getById(param.getUserId());
            List<SysMenu> menuList = null;
            if(StringUtils.isNotEmpty(user.getIsAdmin())&& user.getIsAdmin().equals("1")){//超级管理员
                menuList = sysMenuService.list();
            }else {
                menuList = sysMenuService.getMenuByMemberId(user.getUserId());
            }
            //获取全部的code字段)
            List<String> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                    .stream()
                    .map(item ->item.getCode())
                    .filter(item->item != null)
                    .collect(Collectors.toList());
            String[] strings = collect.toArray(new String[collect.size()]);
            userInfo.setName(user.getNickName());
            userInfo.setUserId(user.getUserId());
            userInfo.setPermissions(strings);
            return ResultUtils.success("查询成功",userInfo);

        }
        else {
            return ResultUtils.error("用户类型错误!");
        }
    }

    //添加获取菜单信息的方法
    @GetMapping("/getMenuList")
    public ResultVo getMenu(InfoParam parm){
        //参数校验
        if (parm == null || parm.getUserId() == null || parm.getUserType() == null) {
            return ResultUtils.error("参数不完整，请检查userId和userType");
        }

        if ("1".equals(parm.getUserType())){//会员
            List<SysMenu> menuList = sysMenuService.getMenuByMemberId(parm.getUserId());
            //获取菜单和目录
            List<SysMenu> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item !=null && !item.getType().equals("2"))
                    .collect(Collectors.toList());
            List<RouterVo> ruror = MakeMenuTree.makeRouter(collect,0L);
            return ResultUtils.success("查询成功",ruror);
        }else if ("2".equals(parm.getUserType())){//员工
            SysUser user = sysUserService.getById(parm.getUserId());
            if (user == null) {
                return ResultUtils.error("用户不存在");
            }
            List<SysMenu> menuList = null;
            if(StringUtils.isNotEmpty(user.getIsAdmin())&&user.getIsAdmin().equals("1")){
                menuList = sysMenuService.list();
            }else {
                menuList = sysMenuService.getMenuByUserId(parm.getUserId());
            }
                //获取菜单和目录
                List<SysMenu> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null && !item.getType().equals("2"))
                        .collect(Collectors.toList());
                List<RouterVo> ruror = MakeMenuTree.makeRouter(collect, 0L);
                return ResultUtils.success("查询成功", ruror);
            }else {
            return ResultUtils.error("用户类型错误！");
            }
        }
    }
