//封装弹框属性
export type DialogModel = {
    title: string,
    visible: boolean,
    height: number,
    width: number
}

// 定义通用函数类型
export type FuncList = () => any