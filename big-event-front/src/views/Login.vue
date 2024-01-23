<script  setup >
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import {useRegisterService,useLoginService} from '@/api/user.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import {useTokenStore} from '@/stores/token.js'
import { ElLoading } from 'element-plus'
// import type { Action } from 'element-plus'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
//注册功能数据模型
const registerData = ref({
    username:'',
    password:'',
    rePassword:''
})
//自定义表单验证规则
const checkAgain = (rule,value,callback)=>{
    if(value==""){
        callback(new Error('请再次输入一次密码'))
    }else if(value != registerData.value.password){
        callback(new Error('两次密码不一致!'))
    }
}

//设置表单校验规则
const rules = {
    username:[{required:true,message:"请输入用户名",trigger:'blur'},
                {min:5,max:16,message:'长度为5-16位非空字符',trigger:'blur'}
        ],
    password:[{required:true,message:"请输入密码",trigger:'blur'},
            {min:5,max:16,message:'长度为5-16位非空字符',trigger:'blur'}],
    rePassword:[{validator:checkAgain,trigger:'blur'}]
}
//注册
const register = async ()=>{
    let result = await useRegisterService(registerData.value)

        ElMessage.success(result.msg?result.msg:"注册成功")

        isRegister.value =false
    
    // }else{
    //     ElMessageBox.alert('注册失败', '注意', {
    //     confirmButtonText: '知道了',
    //     })
    // }
}
//登录 复用register数据模型
const useToken = useTokenStore();
const router = useRouter();
const login = async ()=>{
    let result = await useLoginService(registerData.value);
    if(result.code===0){
        // alert(result.msg?result.msg:"登陆成功")
        ElMessage.success(result.msg?result.msg:"登陆成功")
        useToken.setToken(result.data)
        //pinia定义的响应式数据都不需要加.value
        // console.log(useToken.token)
        router.push('/layout')
    }else{
        ElMessageBox.alert('登陆失败', '注意', {
        confirmButtonText: '知道了',
        })
    }
}
//复用同一个数据模型 在切换时需要清除痕迹
const clearData = ()=>{
    register.value={
        username:'',
        password:'',
        rePassword:''
    }
}
const fullscreenLoading = ref(false)
const openFullScreen = () => {
  fullscreenLoading.value = true
  setTimeout(() => {
    fullscreenLoading.value = false
  }, 2000)
}

</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form"> 
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username" ></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码" v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearData()" >
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>
            <!-- 登录表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space v-loading.fullscreen.lock="fullscreenLoading" @click="login();openFullScreen()">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true;clearData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
            url('@/assets/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>