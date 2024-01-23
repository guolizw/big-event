import {defineStore} from 'pinia'
import { ref } from 'vue'
/*

第一个参数是名字 唯一性
第二个参数是个函数 函数内部来定义状态的所有内容
*/

export const useTokenStore = defineStore('token',()=>{
    const token = ref('')

    const setToken = (newToken)=>{
        token.value=newToken;
    }

    const removeToken = ()=>{
        token.value='';
    }

    //一定要返回
    return {
        token,
        setToken,
        removeToken
    }
},
{
    persist:true
}
)