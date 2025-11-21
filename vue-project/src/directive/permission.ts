import { DirectiveBinding } from 'vue'

/**
 * 权限指令
 * @param el 
 * @param binding 
 */
function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    // 这里可以从store中获取用户权限列表
    // 暂时简化处理，允许所有权限
    const hasPermission = true
    
    if (value && value instanceof Array && value.length > 0) {
        if (!hasPermission) {
            el.style.display = 'none'
        }
    } else {
        throw new Error('权限指令需要传入权限数组')
    }
}

export default {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        checkPermission(el, binding)
    },
    updated(el: HTMLElement, binding: DirectiveBinding) {
        checkPermission(el, binding)
    }
}
