<template>
  <div class="container">
    <div class="app-container">
      <!-- 操作栏 -->
      <div class="mongo-operate">
        <el-button size="mini" type="primary" @click="showDialog = true">添加记录</el-button>
      </div>

      <!-- MongoDB 数据表格 -->
      <el-table :data="list">
        <el-table-column prop="_id" align="center" width="250" label="MongoDB ID">
          <template v-slot="{ row }">
            <span>{{ row._id }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" align="center" label="名称">
          <template v-slot="{ row }">
            <el-input v-if="row.isEdit" v-model="row.editRow.name" size="mini" />
            <span v-else>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="value" align="center" label="值">
          <template v-slot="{ row }">
            <el-input v-if="row.isEdit" v-model="row.editRow.value" size="mini" />
            <span v-else>{{ row.value }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template v-slot="{ row }">
            <template v-if="row.isEdit">
              <el-button type="primary" size="mini" @click="btnEditOK(row)">确定</el-button>
              <el-button size="mini" @click="row.isEdit = false">取消</el-button>
            </template>
            <template v-else>
              <el-button size="mini" type="text" @click="btnEditRow(row)">编辑</el-button>
              <el-popconfirm title="确定删除该记录吗？" @onConfirm="confirmDel(row._id)">
                <el-button slot="reference" style="margin-left:10px" size="mini" type="text">删除</el-button>
              </el-popconfirm>
            </template>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-row type="flex" style="height:60px" align="middle" justify="end">
        <el-pagination
          :page-size="pageParams.pagesize"
          :current-page="pageParams.page"
          :total="pageParams.total"
          layout="prev, pager, next"
          @current-change="changePage"
        />
      </el-row>
    </div>

    <!-- 添加数据弹窗 -->
    <el-dialog width="500px" title="新增 Mongo 记录" :visible.sync="showDialog" @close="btnCancel">
      <el-form ref="mongoForm" :model="mongoForm" :rules="rules" label-width="120px">
        <el-form-item prop="name" label="名称">
          <el-input v-model="mongoForm.name" style="width:300px" size="mini" />
        </el-form-item>
        <el-form-item prop="value" label="值">
          <el-input v-model="mongoForm.value" style="width:300px" size="mini" />
        </el-form-item>
        <el-form-item>
          <el-row type="flex" justify="center">
            <el-col :span="12">
              <el-button type="primary" size="mini" @click="btnOK">确定</el-button>
              <el-button size="mini" @click="btnCancel">取消</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getMongoList, addMongoData, delMongoData, updateMongoData } from '@/api/mongo' // 引入API

export default {
  name: 'MongoPage',
  data() {
    return {
      list: [], // MongoDB 数据列表
      showDialog: false, // 控制添加数据弹窗
      pageParams: {
        page: 1, // 当前页码
        pagesize: 5, // 每页数量
        total: 0 // 总条数
      },
      mongoForm: {
        name: '',
        value: ''
      },
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '值不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getMongoList()
  },
  methods: {
    async getMongoList() {
      const { data, total } = await getMongoList(this.pageParams)
      this.list = data // 更新数据
      this.pageParams.total = total

      // 给每个数据项添加 isEdit 标记，用于编辑状态控制
      this.list.forEach(item => {
        this.$set(item, 'isEdit', false)
        this.$set(item, 'editRow', { name: item.name, value: item.value })
      })
    },
    changePage(newPage) {
      this.pageParams.page = newPage
      this.getMongoList()
    },
    btnOK() {
      this.$refs.mongoForm.validate(async isOK => {
        if (isOK) {
          await addMongoData(this.mongoForm)
          this.$message.success('新增 Mongo 记录成功')
          this.getMongoList()
          this.btnCancel()
        }
      })
    },
    btnCancel() {
      this.$refs.mongoForm.resetFields()
      this.showDialog = false
    },
    async confirmDel(id) {
      await delMongoData(id)
      this.$message.success('删除 Mongo 记录成功')
      if (this.list.length === 1) this.pageParams.page--
      this.getMongoList()
    },
    btnEditRow(row) {
      row.isEdit = true
      row.editRow.name = row.name
      row.editRow.value = row.value
    },
    async btnEditOK(row) {
      if (row.editRow.name && row.editRow.value) {
        await updateMongoData({ _id: row._id, name: row.editRow.name, value: row.editRow.value })
        this.$message.success('更新 Mongo 记录成功')
        Object.assign(row, { name: row.editRow.name, value: row.editRow.value, isEdit: false })
      } else {
        this.$message.warning('名称和值不能为空')
      }
    }
  }
}
</script>

  <style scoped>
  .mongo-operate {
    padding: 10px;
  }
  </style>
