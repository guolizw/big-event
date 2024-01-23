import request from '@/utils/request.js'
//提供调用注册接口的函数

export const useRegisterService = (registerData)=>{
    //数据将会转化为json格式 需要用函数将其转换为x-www-form格式
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key])
    }
    return request.post('/user/register',params)
}

//登录
export const useLoginService = (loginData)=>{
    const params = new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key]);
    }
    return request.post('/user/login',params)
}

//展示个人数据
export const useUserInfoService = ()=>{
    return request.get('/user/userInfo')
}

//更新个人数据
export const updateUserInfo = (userInfoData)=>{
    return request.put('/user/update',userInfoData)
}

export const updateUserAvatar = (url)=>{
    //可以直接拼接 也可以使用urlsearchparams
    const params = new URLSearchParams();
    params.append('avatarUrl',url)
    return request.patch('/user/updateAvatar',params)
}
//更新密码
export const updatePasswordService=(passwordData)=>{
    return request.patch('/user/updatePwd',passwordData)
}