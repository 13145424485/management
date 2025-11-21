package com.doc.config.springSecurity;

import com.doc.web.member.entity.Member;
import com.doc.web.member.service.MemberService;
import com.doc.web.sys_menu.entity.SysMenu;
import com.doc.web.sys_menu.service.SysMenuService;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("customizedUserDetailService")
public class CustomizedUserDetailService implements UserDetailsService {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    //loadUserByUsername ：UserDetailsService接口的核心方法，Spring Security调用此方法进行用户认证
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //认证
        int index  = s.indexOf(":");
        String username = s.substring(0,index);
        String userType= s.substring(index+1);
        //预期格式 用户名:用户类型
        //认证登录
        if(userType.equals("1")) {
            Member user = memberService.loadUser(username);
            if(user == null){
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            //授权:把该用户拥有的按钮权限,交给spring secuity进行管理
            //获取会员的菜单按钮权限
            List<SysMenu> menuList = sysMenuService.getMenuByMemberId(user.getMemberId());
            //取出code字段
            //item ：Lambda参数，代表集合中的每个元素
            List<String> collect = menuList.
                    stream().
                    //类型转换，将 SysMenu 对象映射为它的code属性
                    map(item -> item.getCode()).
                    filter(item -> item != null&& StringUtils.isNotEmpty(item)).
                    collect(Collectors.toList());
            String[] strings = collect.toArray(new String[collect.size()]);
            //createAuthorityList() ：静态方法，用于创建权限列表
            //AuthorityUtils.createAuthorityList 是 Spring Security 中的一个方法，用于将权限字符串转换为 GrantedAuthority 对象的列表。
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
            //授权
            user.setAuthorities(authorityList);
            return user;
        }else if(userType.equals("2")){
            SysUser user = sysUserService.loadUser(username);
            if(user == null){
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            //授权:把该用户拥有的按钮权限,交给spring secuity进行管理
            List<SysMenu> menuList = null;
            if(StringUtils.isNotEmpty(user.getIsAdmin())&& user.getIsAdmin().equals("1")) {
                //如果是管理员，查询所有菜单
                menuList = sysMenuService.list();
            }else {
                menuList = sysMenuService.getMenuByUserId(user.getUserId());
            }
            //取出code字段
            List<String> collect = menuList
                    .stream()
                    .map(item -> item.getCode())
                    .filter(item -> item != null && StringUtils.isNotEmpty(item))
                    .collect(Collectors.toList());
            String[] strings = collect.toArray(new String[collect.size()]);
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
            //授权
            user.setAuthorities(authorityList);
            return user;
        }else{
            throw new UsernameNotFoundException("用户类型错误");
        }

    }
}
