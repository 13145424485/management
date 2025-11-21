import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue'
import { IEditorConfig } from '@wangeditor/editor'
export default function useEditor(){
    type InsertFnType = (url: string, alt?: string, href?: string) => void
    const editorRef = shallowRef()
    const valueHtml = ref('')
    const toolbarConfig = {}
    const editorConfig: Partial<IEditorConfig> = { MENU_CONF: {}, placeholder: '请输入内容...' }

    //上传图片的配置
    editorConfig.MENU_CONF!['uploadImage'] ={
        fieldName: 'file',
        server: 'http://localhost:8089/api/upload/uploadImage',
        customInsert(res: any, insertFn: InsertFnType){
            console.log('图片上传的路径是:',res.data.msg)
            insertFn(res.data.msg) // res.data.msg 获取上传图片的url
        }
    }

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
        const editor = editorRef.value
        if (editor == null) return
        editor.destroy()
    })

    const handleCreated = (editor:any) => {
        editorRef.value = editor // 记录 editor 实例，重要！
    }

    return {
        editorRef,
        valueHtml,
        mode: 'default', // 或 'simple'
        toolbarConfig,
        editorConfig,
        handleCreated
    }
}