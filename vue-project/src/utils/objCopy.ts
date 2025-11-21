/**
 * @description: 对象的拷贝
 * @param {any} obj1
 * @param {any} obj2
 * @return {*}
 */

// 对象的拷贝
export default function objCopy(obj1:any,obj2:any){
    Object.keys(obj2).forEach(key =>{
        obj2[key] = obj1[key]
    })
}