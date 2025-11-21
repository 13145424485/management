import {onMounted,ref} from 'vue'
import { getImageApi } from '@/api/login'
export default function useImage(){
    //定义图片的路径
    const imgSrc= ref()
    //获取图片的验证码
    const getImage =  async ()=>{
        let res = await getImageApi()
        if(res&& res.code == 200){
            console.log("path:",res.data)
            imgSrc.value = res.data
        }
    }

    onMounted(()=>{
        getImage()
    })

    return {
        imgSrc,
        getImage
    }
}