import axios from 'axios'
import { useTokenStore } from '@/stores/token.js'
import { ElMessage, ElMessageBox } from 'element-plus';
//创建axios对象

const Instance = axios.create({
    baseURL:'/api'
})
//设置请求拦截器
Instance.interceptors.request.use(
    config=>{
        const useToken = useTokenStore();
        if(useToken.token){
            config.headers.Authorization = useToken.token;
        }
        return config;
    },
    err=>{
        ElMessage.error('服务异常')
        return Promise.reject(err);
    }
)

//添加响应拦截器
import router from '@/router'
Instance.interceptors.response.use(
    //成功
    result=>{
        if(result.data.code===0){
            return result.data
            
        }else{
            ElMessage.error('响应异常')
        }
    },
    //失败
    err=>{
        //判断响应状态码，如果为401则提示需要登录并且强制跳转到登陆页面 （未登陆前的统一设置）
        if(err.response.status===401){
            ElMessageBox.alert('请先登录','注意',{
                confirmButtonText:'知道了'
    
            })
            router.push('/')
        }else{
            ElMessage.error('服务异常')
        }
        return Promise.reject(err); //将异步状态转化为失败状态，被catch捕获
    }
)


export default Instance;