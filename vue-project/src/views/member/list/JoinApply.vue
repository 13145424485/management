<template>
  <SysDialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
      :visible="dialog.visible"
      @onConfirm="commit"
      @onClose="onClose"
  >
    <template v-slot:content>
      <div>请选择会员卡类型</div>
      <el-divider style="margin: 10px 0px" />
      <el-select
          style="width: 100%"
          v-model="parm.cardId"
          class="m-2"
          placeholder="请选择会员卡类型"
          size="large"
      >
        <el-option
            v-for="item in cardList"
            :key="item.cardId"
            :label="item.title"
            :value="item.cardId"
        />
      </el-select>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { MemberType, ApplyCard } from "@/api/member/MemberModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useJoin from "@/composables/member/useJoin";
import { reactive } from "vue";
import { ElMessage } from "element-plus";
import { applySaveApi } from "@/api/member/index";
const { cardList, getCardList } = useJoin();
//选择的卡类型
const parm = reactive<ApplyCard>({
  cardId: "",
  memberId: "",
});
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//显示弹
const show = (row: MemberType) => {
  parm.memberId = row.memberId;
  dialog.title = "为【" + row.name + "】办卡";
  dialog.height = 180;
  getCardList();
  onShow();
};
//暴露出去
defineExpose({
  show,
});
//注册事件
const emits = defineEmits(['refresh'])
//提交表单
const commit = async () => {
  if (!parm.cardId) {
    ElMessage.error("请选择会员卡类型");
    return;
  }
  let res = await applySaveApi(parm);
  if (res && res.code == 200) {
    ElMessage.success(res.msg);
    emits('refresh')
    onClose()
  }
};
</script>

<style scoped></style>
