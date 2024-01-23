import {defineStore} from 'pinia'
import { ref } from 'vue'

 const useUserInfoStore = defineStore('userInfo',()=>{
    const userInfo = ref({})

    const setUserInfo = (newInfo)=>{
        userInfo.value = newInfo
    }

    const removeInfo = ()=>{
        userInfo.value = ''
    }

    return {
        userInfo,
        setUserInfo,
        removeInfo
    }

},{
    persist:true
})

export default useUserInfoStore