<template>
  <SysDialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
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
            <el-form-item prop="courseName" label="课程名称">
              <el-input v-model="addModel.courseName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="courseHour" label="课程课时">
              <el-input v-model="addModel.courseHour"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="coursePrice" label="课程价格">
              <el-input v-model="addModel.coursePrice"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="teacherName" label="教练">
              <el-select
                  style="width: 100%"
                  v-model="addModel.teacherName"
                  class="m-2"
                  placeholder="请选择教练"
                  size="default"
                  @change="selectTeach"
              >
                <el-option
                    v-for="item in teacherData.list"
                    :key="item.label"
                    :label="item.label"
                    :value="{value:item.value,label:item.label}"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="image" label="课程图片">
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
        <el-form-item prop="courseDetails" label="课程详情">
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
import { CourseType } from "@/api/course/CourseModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, FormInstance } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import useUpload from "@/composables/courses/useUpload";
import useEditor from "@/composables/courses/userEditor";
import useSelectTeacher from "@/composables/courses/useSelectTeacher";
import { EditType, Title } from "@/type/BaseEnum";
import { addApi, editApi } from "@/api/course/index";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
//表单的ref属性
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onShow, onConfirm } = useDialog();
//教练选择
const { teacherData, listTeacher } = useSelectTeacher();
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
//显示弹框
const show = async (type: string, row?: CourseType) => {
  //清空图片数据
  fileList.value = [];
  //获取教练数据列表
  await listTeacher();
  //弹框属性
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
  addModel.image = "";
  if (type == EditType.ADD) {
    const editor = editorRef.value;
    if (editor) {
      editor.clear();
    }
  }
  addModel.courseDetails = "";
  //编辑数据回显
  if (type == EditType.EDIT) {
    nextTick(() => {
      //把要编辑的数据复制到表单绑定的对象
      global.$objCoppy(row, addModel);
      //文本编辑器的回显
      valueHtml.value = addModel.courseDetails;
      //图片回显
      if (row?.image) {
        //图片回显
        let img = {
          name: "",
          url: "",
        };
        imgurl.value = addModel.image;
        img.url = addModel.image;
        fileList.value.push(img);
      }
    });
  }
  if (row && row.courseDetails) {
    //文本编辑器的回显
    valueHtml.value = row.courseDetails;
  }
  onShow();
  //表单清空
  addFormRef.value?.resetFields();
  addModel.type = type;
};
//暴露出去，给外部组件使用
defineExpose({
  show,
});
//表单绑定的数据对象
const addModel = reactive<CourseType>({
  type: "", //区分 新增 编辑
  courseId: "",
  courseName: "",
  image: "",
  teacherName: "",
  teacherId: "",
  courseHour: 0,
  courseDetails: "",
  coursePrice: 0,
});
const validateCourseHour = (rule: any, value: any, callback: any) => {
  if (value === 0 || value < 0) {
    callback(new Error("请填写课程课时"));
  } else {
    callback();
  }
};
const validateCoursePrice = (rule: any, value: any, callback: any) => {
  if (value === 0 || value < 0) {
    callback(new Error("请填写课程价格"));
  } else {
    callback();
  }
};
//表单验证规则
const rules = reactive({
  courseName: [
    {
      required: true,
      trigger: "blur",
      message: "请输入课程名称",
    },
  ],
  image: [
    {
      required: true,
      trigger: "blur",
      message: "请上传课程图片",
    },
  ],
  teacherName: [
    {
      required: true,
      trigger: "blur",
      message: "请选择课程教练",
    },
  ],
  courseDetails: [
    {
      required: true,
      trigger: "blur",
      message: "请填写课程详情",
    },
  ],
  coursePrice: [
    {
      required: true,
      validator: validateCoursePrice,
      trigger: "blur",
    },
  ],
  courseHour: [
    {
      required: true,
      validator: validateCourseHour,
      trigger: "blur",
    },
  ],
});
//注册事件
const emits = defineEmits(["reFresh"]);
//表单提交
const commit = () => {
  //封面图地址
  addModel.image = imgurl.value;
  //课程详情
  addModel.courseDetails = valueHtml.value;
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
//选择教练的change
const selectTeach = (val:any)=>{
  console.log('obj:',val)
  addModel.teacherId = val.value;
  addModel.teacherName = val.label;
}
</script>

<style scoped></style>