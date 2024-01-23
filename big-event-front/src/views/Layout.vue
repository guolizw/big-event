<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import useUserInfoStore from '@/stores/userInfo';
import { useTokenStore } from '@/stores/token';
import {useUserInfoService} from '@/api/user.js'
import { onMounted } from 'vue';
import { ElMessage,ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
const useUserInfo = useUserInfoStore();
const useToken = useTokenStore();
const router = useRouter();
//获取用户资料 挂载渲染
const getUserInfo = async ()=>{
    let result = await useUserInfoService();
    useUserInfo.userInfo = result.data
}
//下拉框command事件函数
const hanleCommand = (command)=>{
    if(command == 'logout'){
        ElMessageBox.confirm(
    '您确定要退出登录嘛？',
    '温馨提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
        //确定则需要删除pinia中的个人数据和token
        useToken.removeToken()
        useUserInfo.removeInfo()

        //跳转到登陆页面
       router.push('/')
      ElMessage({
        type: 'success',
        message: '退出成功',
      })
    }).catch(() => {
      ElMessage({
        type: 'info',
        message: '取消成功',
      })
    })
    }else{
        router.push('/user/'+command)
    }
}

onMounted(()=>{
    getUserInfo()
})

</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff"
                router>
                <el-menu-item index="/article/category">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-sub-menu >
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/userInfo">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>用户  <strong>{{useUserInfo.userInfo.nickname? useUserInfo.userInfo.nickname :useUserInfo.userInfo.username}}</strong></div>


                <!-- command:条目被点击后被触发，在事件函数上申明一个参数，接受条目对应的指令-->
                <el-dropdown placement="bottom-end" @command="hanleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="useUserInfo.userInfo.userPic?useUserInfo.userInfo.userPic:avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="userInfo" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>大事件 ©2023 Created by zw</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>