<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { onMounted, ref } from 'vue'
import {categoryListService,addCategoryService,updateCategoryService,deleteCategoryService} from '@/api/article.js'
import { ElMessage,ElMessageBox } from 'element-plus'

const categorys = ref([
    
])

//调用展示文章分类的接口函数
const categoryList = async ()=>{
    let result = await categoryListService();
    categorys.value = result.data;
}

//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}
//添加分类函数
const addCategory = async ()=>{
    await addCategoryService(categoryModel.value);
    ElMessage.success('添加成功')
    //并将dialog设置为false
    dialogVisible.value=false
    //并且重新刷新一下页面
    categoryList();
}
//复用弹窗 展示弹窗不同的功能标题
const title = ref('')

//编辑分类函数
const editCategory = (row)=>{
    title.value = '编辑分类'
    dialogVisible.value = true
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    //扩展数据模型的属性 为了之后调用接口实现后端更新功能
    categoryModel.value.id = row.id

}

//编辑分类确认函数调用接口
const updateCategory = async()=>{
    await updateCategoryService(categoryModel.value)
    ElMessage.success('更新成功')
    dialogVisible.value=false

    categoryList();
}

//添加分类弹窗保持空
const clearData = ()=>{
    categoryModel.value.categoryName=''
    categoryModel.value.categoryAlias=''
}

//删除弹窗
const deleteCategory = (row)=>{
    ElMessageBox.confirm(
    '确定要删除该分类吗？',
    '温馨提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then( async () => {

        await deleteCategoryService(row.id);
      ElMessage({
        type: 'success',
        message: '删除成功',
      })
      //刷新
      categoryList()
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消成功',
      })
    })
}

onMounted(()=>categoryList())

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true; title='添加分类';clearData()">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{row}">
                    <el-button :icon="Edit"  circle plain type="primary" @click=editCategory(row)></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click=deleteCategory(row)></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 添加分类弹窗 -->
<el-dialog v-model="dialogVisible" :title=title width="30%" >
    <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
        <el-form-item label="分类名称" prop="categoryName" >
            <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="分类别名" prop="categoryAlias">
            <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
        </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="title=='添加分类'? addCategory():updateCategory()"> 确认 </el-button>
        </span>
    </template>
</el-dialog>
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
}
</style>