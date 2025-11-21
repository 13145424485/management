package com.doc.web.sys_menu.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述路由信息，根据前端路由要求进行封装
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
// ③ Jackson 注解；序列化成 JSON 时，
//    如果字段是 null 或空 List，就直接跳过，省流量。
public class RouterVo {
    private String path;
    private String component;
    private String name;
    private String redirect;//重定向地址，比如 /index 重定向到 /dashboard
    private Meta meta;//前端路由信息没有，直接定义，// ⑨ 把“标题、图标、角色”打包成一个对象，前端直接 route.meta.xxx 用

    @Data
    @AllArgsConstructor
    public class Meta{
        private String title;
        private String icon;
        private Object[] roles;//能访问该菜单的角色数组
    }

    // ⑪ 子路由：把自己再嵌套一层，形成树形菜单
    private List<RouterVo> children  = new ArrayList();
}
