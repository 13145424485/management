<template>
  <SysDialog
      :title="dialog.title"
      :width="dialog.width"
      :height="dialog.height"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="commit"
  >
    <template v-slot:content>
      <el-form
          :model="addModel"
          ref="addFormRef"
          :rules="rules"
          label-width="80px"
          size="default"
      >
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="name" label="商品名称">
              <el-input v-model="addModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="price" label="商品价格">
              <el-input v-model="addModel.price"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="unit" label="单位">
              <el-input v-model="addModel.unit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="specs" label="规格">
              <el-input v-model="addModel.specs"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="store" label="库存">
              <el-input v-model="addModel.store"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="image" label="商品图片">
          <el-upload
              ref="uploadRef"
              action="#"
              :on-change="uploadFile"
              list-type="picture-card"
              :auto-upload="false"
              :file-list="fileList"
              :limit="1"
          >
            <el-icon><Plus /></el-icon>

            <template #file="{ file }">
              <div>
                <img
                    class="el-upload-list__item-thumbnail"
                    :src="file.url"
                    alt=""
                />
                <span class="el-upload-list__item-actions">
                  <span
                      class="el-upload-list__item-preview"
                      @click="handlePictureCardPreview(file)"
                  >
                    <el-icon><zoom-in /></el-icon>
                  </span>
                  <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file)"
                  >
                    <el-icon><Delete /></el-icon>
                  </span>
                </span>
              </div>
            </template>
          </el-upload>

          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
          </el-dialog>
        </el-form-item>
        <el-form-item prop="details" label="商品详情">
          <div style="border: 1px solid #ccc">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
            />
            <Editor
                style="height: 300px; overflow-y: hidden"
                v-model="valueHtml"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { GoodsType } from "@/api/goods/GoodsModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, FormInstance, UploadFile } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import useUpload from "@/composables/courses/useUpload";
import useEditor from "@/composables/courses/userEditor";
import { addApi, editApi } from "@/api/goods/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
//图片上传
const {
  dialogImageUrl,
  dialogVisible,
  disabled,
  handleRemove,
  handlePictureCardPreview,
  uploadFile,
  uploadRef,
  imgurl,
  fileList,
} = useUpload();
//文本编辑器
const {
  editorRef,
  handleCreated,
  mode,
  editorConfig,
  valueHtml,
  toolbarConfig,
} = useEditor();
//表单ref属性
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//定义外部使用的方法
const show = (type: string, row?: GoodsType) => {
  //清空图片数据
  fileList.value = [];
  //设置弹框属性
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  dialog.width = 900;
  dialog.height = 500;
  //清空图片和文本编辑器
  const upload = uploadRef.value;
  if (upload) {
    upload.clearFiles();
  }
  // addModel.image = "";
  if (type == EditType.ADD) {
    const editor = editorRef.value;
    if (editor) {
      editor.clear();
    }
  }
  // addModel.details = "";
  //编辑数据回显
  if (type == EditType.EDIT) {
    nextTick(() => {
      global.$objCoppy(row, addModel);
      //图片回显
      if (row?.image) {
        //图片回显
        let img: UploadFile = {
          name: "",
          url: addModel.image,
        };
        imgurl.value = addModel.image
        fileList.value.push(img);
      }
    });
  }
  if (row && row.details) {
    //文本编辑器的回显
    valueHtml.value = row.details;
  }
  onShow();
  addFormRef.value?.resetFields()
  addModel.type = type;
};
//暴露出去
defineExpose({
  show,
});
//表单绑定的对象
const addModel = reactive<GoodsType>({
  type: "",
  goodsId: "",
  name: "",
  image: "",
  details: "",
  unit: "",
  specs: "",
  price: 0,
  store: 0,
});
const validatePrice = (rule: any, value: any, callback: any) => {
  if (value === 0 || value < 0) {
    callback(new Error("请填写商品价格"));
  } else {
    callback();
  }
};
const validateStore = (rule: any, value: any, callback: any) => {
  if (value === 0 || value < 0) {
    callback(new Error("请填写商品库存"));
  } else {
    callback();
  }
};
//表单验证规则
const rules = reactive({
  name: [
    {
      required: true,
      trigger: "blur",
      message: "请输入商品名称",
    },
  ],
  image: [
    {
      required: true,
      trigger: "blur",
      message: "请上传商品图片",
    },
  ],
  unit: [
    {
      required: true,
      trigger: "blur",
      message: "请填写单位",
    },
  ],
  details: [
    {
      required: true,
      trigger: "blur",
      message: "请填写商品详情",
    },
  ],
  specs: [
    {
      required: true,
      trigger: "blur",
      message: "请填写商品规格",
    },
  ],
  price: [
    {
      required: true,
      validator: validatePrice,
      trigger: "blur",
    },
  ],
  store: [
    {
      required: true,
      validator: validateStore,
      trigger: "blur",
    },
  ],
});
//注册事件
const emits = defineEmits(["reFresh"]);
//提交表单
const commit = () => {
  // if(addModel.type == EditType.ADD){

  // }
  //封面图地址
  addModel.image = imgurl.value;
  //商品详情
  addModel.details = valueHtml.value;
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (addModel.type == EditType.ADD) {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        emits("reFresh");
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>