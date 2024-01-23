import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue';
import LayoutVue from '@/views/Layout.vue';
import ArticleCategoryVue from '@/components/article/ArticleCategory.vue';
import ArticleManageVue from '@/components/article/ArticleManage.vue';
import UserAvatarVue from '@/components/user/UserAvatar.vue';
import UserInfoVue from '@/components/user/UserInfo.vue';
import UserResetPasswordVue from '@/components/user/UserResetPassword.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:'/',
      component:Login
    },
    {
      path:'/layout',
      component:LayoutVue,
      //重定向
      redirect:'/article/manage',
      children:[
        {
          path:'/article/manage',
          component:ArticleManageVue
        },
        {
          path:'/article/category',
          component:ArticleCategoryVue
        },
        {
          path:'/user/avatar',
          component:UserAvatarVue
        },
        {
          path:'/user/userInfo',
          component:UserInfoVue
        },
        {
          path:'/user/resetpassword',
          component:UserResetPasswordVue
        }
      ]
    }
   
  ]
})

export default router
