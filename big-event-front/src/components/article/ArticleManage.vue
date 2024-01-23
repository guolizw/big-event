<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import {categoryListService,showArticleService,addArticleService,updateArticleService,deleteArticleService} from '@/api/article.js';
import { onMounted, ref, watchEffect } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { useTokenStore } from '@/stores/token';

import {Plus} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
//文章分类数据模型
const categorys = ref([
    
])
//查找分类列表
const categoryList = async ()=>{
    let result = await categoryListService();
    categorys.value = result.data;
}

//用户搜索时选中的分类id
const categoryId=ref('')

//用户搜索时选中的发布状态
const state=ref('')

//文章列表数据模型
const articles = ref([
    
])


//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    //刷新
    showArticle();
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    //刷新
    showArticle();
}
//调用接口查询所有文章
const showArticle = async ()=>{
    //查询文章所需要传的数据
    let params = {
        pageNum:pageNum.value,
        pageSize:pageSize.value,
        categoryId:categoryId.value ? categoryId.value:null,
        state:state.value ? state.value:null
        
    }
    let result = await showArticleService(params);
    
    // console.log(articles.value)

    //进行视图渲染
    total.value = result.data.total;
    articles.value = result.data.items;

    //遍历 将categoryId转换为categoryName
    for(let i in articles.value){
        let article = articles.value[i]
        for(let j in categorys.value){
            let category = categorys.value[j]
            if(article.categoryId === category.id){
                //扩展属性
                
                article.categoryName = category.categoryName;
               
            }
        }
    }
    
}

//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content:'',
    state:''
})

//上传图片需要用到请求头
const useToken = useTokenStore();

//上传成功之后的回调
const successUpload = (result)=>{
    articleModel.value.coverImg = result.data
}
//添加文章函数
const addArticle = async(clickState)=>{
    articleModel.value.state = clickState
    await addArticleService(articleModel.value);
    
    ElMessage.success('添加成功')
    
    visibleDrawer.value=false

    showArticle();
}

//使用title进行对抽屉的复用
const title = ref('')
//强制刷新key
const key = ref(0)
//清除数据
const clearData = ()=>{
    articleModel.value.title = ''
    articleModel.value.categoryId = ''
    articleModel.value.state = ''
    articleModel.value.coverImg = ''
    articleModel.value.content = ''
    console.log(articleModel.value.content)
    //当key发生变化时 强制刷新绑定的dom元素
    key.value = new Date;
    
}

//编辑文章函数
const editArticle = (row)=>{
    title.value = '更新文章'
    visibleDrawer.value = true

    articleModel.value.title = row.title
    articleModel.value.categoryId = row.categoryId
    articleModel.value.content = row.content
    articleModel.value.coverImg = row.coverImg
    articleModel.value.state = row.state
    //扩展属性 方便更新
    articleModel.value.id = row.id

}

//更新文章接口函数
const updateArticle = async()=>{
     //调用更新接口函数
     await updateArticleService(articleModel.value)
    ElMessage.success('更新成功')

    //刷新
    showArticle();

    visibleDrawer.value=false
}
//删除文章
const deleteArticle = async(row)=>{
    ElMessageBox.confirm(
    '确定要删除该文章吗？',
    '温馨提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then( async () => {

        await deleteArticleService(row.id);
      ElMessage({
        type: 'success',
        message: '删除成功',
      })
      //刷新
      showArticle()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消成功',
      })
    })
}
onMounted(()=>{
    //注意先后顺序
    categoryList(),
    showArticle()
})
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary"  @click="visibleDrawer=true ; title='添加文章';clearData()" >添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" class="category">
                    <el-option 
                        v-for="c in categorys" 
                        :key="c.id" 
                        :label="c.categoryName"
                        :value="c.id" >
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：" >
                <el-select placeholder="请选择" v-model="state" class="status">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="showArticle">搜索</el-button>
                <el-button @click="categoryId='';state=''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click=editArticle(row)></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5 ,10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title=title direction="rtl" size="50%" :key=key>
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px" >
                <el-form-item label="文章标题" >
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">

                    <!-- 
                        auto-upload 设置是否自动上传
                        Action 设置服务器接口路径
                        name 设置文件上传命名 和后端一致
                        on-success 成功之后的回调函数
                        headers 设置上传的请求头

                     -->
                    

                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                            action="/api/upload" :headers="{'Authorization':useToken.token}"
                            name="file" :on-success="successUpload"
                    >
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html"></quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="title=='添加文章'? addArticle('已发布'):updateArticle()">发布</el-button>
                    <el-button type="info" @click="title=='添加文章'? addArticle('草稿'):updateArticle()">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    .category{
        width:180px;
    }
    .status{
        width: 180px;
    }
}
/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>