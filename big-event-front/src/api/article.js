import request from '@/utils/request.js';

export const categoryListService = ()=>{
    return request.get('/category')
}
export const addCategoryService =(categoryData)=>{
    //json数据格式不需要转化
    return request.post('/category',categoryData)
}

export const updateCategoryService = (categoryData)=>{
    return request.put('/category',categoryData)
}

export const deleteCategoryService = (id)=>{
    return request.delete('/category?id='+id)
}


export const showArticleService = (params)=>{
    return request.get('/article',{params:params})
}

//添加文章
export const addArticleService = (articleData)=>{
    return request.post('/article',articleData)
}
//更新文章
export const updateArticleService = (articleData)=>{
    return request.put('/article',articleData)
}

//删除文章
export const deleteArticleService = (id)=>{
    return request.delete('/article?id='+id)
}