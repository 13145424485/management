package com.doc.web.sys_menu.entity;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 构造菜单和路由数据的实体类
 */
public class MakeMenuTree {

    //构造菜单树
    //扁平菜单列表变成树形结构
    public static List<SysMenu> makeTree(List<SysMenu> menuList, long pid){
        //static：工具方法，直接 MenuTreeUtil.makeTree(...) 调用，无需 new 对象。
       // menuList：从数据库查出来的全部菜单（扁平）。
        //pid：当前要查找的父节点 ID，第一次调用传 0 或 1（代表根节点）。
        List<SysMenu> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId().equals(pid))
                //防止外面把 null 传进来，自动退化成空流，避免 NPE。
                //只保留 parentId == pid 的记录——即“找爸爸等于 pid 的所有儿子”。
                .forEach(item ->{
                    SysMenu menu = new SysMenu(); // 新对象，避免污染原始数据
                    BeanUtils.copyProperties(item,menu);// Spring 工具，把属性拷贝过去
                    //递归查找下级，自己调用自己
                    List<SysMenu> children = makeTree(menuList, item.getMenuId());
                    menu.setChildren(children);// 把儿子们挂到当前节点
                    list.add(menu); // 放入本层结果
                });
        return list;
    }

    //构造路由数据
    public static  List<RouterVo> makeRouter(List<SysMenu> menuList, Long pid){
        //构建存放路由数据的容器
        List<RouterVo> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item ->item != null && item.getParentId().equals(pid))
                .forEach(item ->{
                    RouterVo router = new RouterVo();
                    router.setName(item.getName());
                    router.setPath(item.getPath());
                    //设置children 递归调用，自己调用自己

                    List<RouterVo> children = makeRouter(menuList, item.getMenuId());
                    router.setChildren(children);
                    if(item.getParentId() == 0L){
                        router.setComponent("Layout");
                        //判断该数据是否是菜单类型
                        if(item.getType().equals("1")){
                            router.setRedirect(item.getPath());
                            //菜单也需要设置children
                            List<RouterVo> listChild = new ArrayList<>();
                            RouterVo child = new RouterVo();
                            child.setName(item.getName());
                            child.setPath(item.getPath());
                            child.setComponent(item.getUrl());
                            child.setMeta(child.new Meta(
                                    item.getTitle(),
                                    item.getIcon(),
                                    item.getCode().split(",")
                            ));
                            //add(child) | 把刚才手动 new 的“重定向子路由”装进去
                            listChild.add(child);
                            router.setChildren(listChild);
                            //setChildren(List<RouterVo> children) 是 RouterVo 的 setter | 把上一步的 listChild 挂到当前路由的 children 属性 |
                            //| 注意：这里覆盖了递归生成的 children，所以目录下如果还有子目录会被“埋掉
                            router.setPath(item.getPath());
                            router.setName(item.getName() + "parent");//让目录节点的 name 与真正页面节点区分开
                        }
                    }else {
                        router.setComponent(item.getUrl());
                        //非一级菜单（parentId ≠ 0）走 else | 直接拿数据库里存的 url 字段当组件路径
                    }
                    router.setMeta(
                            router.new Meta(
                                    item.getTitle(),
                                    item.getIcon(),
                                    item.getCode().split(",")
                            )
                    );
                    list.add(router);
                });
        return list;
    }
}
