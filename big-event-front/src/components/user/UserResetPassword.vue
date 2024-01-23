<script  setup>
import { reactive, ref } from 'vue'

import {updatePasswordService} from '@/api/user.js'
import { ElMessage ,ElMessageBox} from 'element-plus';
import { useTokenStore } from '@/stores/token';
import useUserInfoStore from '@/stores/userInfo';
import { useRouter } from 'vue-router'
import router from '@/router';

const useUserInfo = useUserInfoStore()
const useToken = useTokenStore();
const checkPasswordData = ref({
  old_pwd: '',
  new_pwd: '',
  re_pwd: '',
})

//自定义表单验证规则
const checkAgain = (rule,value,callback)=>{
    if(value==""){
        callback(new Error('请再次输入一次密码'))
    }else if(value != checkPasswordData.value.new_pwd){
        callback(new Error('两次密码不一致!'))
    }
}
//设置表单校验规则
const rules = {
    new_pwd:[{required:true,message:"请输入密码",trigger:'blur'},
            {min:5,max:16,message:'长度为5-16位非空字符',trigger:'blur'}],
    re_pwd:[{validator:checkAgain,trigger:'blur'}]
}




//更新密码
const updatePassword = async()=>{
  ElMessageBox.confirm(
    '您确定要修改密码嘛？',
    '温馨提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async() => {
        let result = await updatePasswordService(checkPasswordData.value);
        //确定则需要删除pinia中的个人数据和token
        useToken.removeToken()
        useUserInfo.removeInfo()

        //跳转到登陆页面
       router.push('/')
        ElMessage({
        type: 'success',
        message: '修改成功',
      })
    }).catch(() => {
      ElMessage({
        type: 'info',
        message: '取消成功',
      })
    })
    
    
}
</script>

<template>
    <el-card class="page-container">
      <template #header>
        <div class="card-header">
          <span>更改密码</span>
        </div>
      </template>
      <el-form
            :label-position="labelPosition"
            label-width="100px"
            :model="checkPasswordData"
            style="max-width: 460px"
            :rules="rules"
        >
            <el-form-item label="原密码" >
                <el-input type="password" v-model="checkPasswordData.old_pwd" />
            </el-form-item>
            <el-form-item label="新密码" prop="new_pwd">
                <el-input type="password" v-model="checkPasswordData.new_pwd" />
            </el-form-item>
            <el-form-item label="确认新密码" prop="re_pwd">
                <el-input type="password" v-model="checkPasswordData.re_pwd" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="updatePassword">确认提交</el-button>
            </el-form-item>
            
        </el-form>
      
    </el-card>
  </template>
  
  <style>
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  
  .box-card {
    width: 480px;
  }
  </style>