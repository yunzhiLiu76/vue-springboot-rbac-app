<template>
  <div class="container">
    <div class="app-container">
      <!-- 操作栏 -->
      <div class="mysql-operate">
        <el-button size="mini" type="primary" @click="showDialog = true">添加数据</el-button>
      </div>

      <!-- MySQL 数据表格 -->
      <el-table :data="list">
        <el-table-column prop="id" align="center" width="100" label="ID">
          <template v-slot="{ row }">
            <span>{{ row.id }}</span>
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
              <el-popconfirm title="确定删除该记录吗？" @onConfirm="confirmDel(row.id)">
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
    <el-dialog width="500px" title="新增 MySQL 数据" :visible.sync="showDialog" @close="btnCancel">
      <el-form ref="mysqlForm" :model="mysqlForm" :rules="rules" label-width="120px">
        <el-form-item prop="name" label="名称">
          <el-input v-model="mysqlForm.name" style="width:300px" size="mini" />
        </el-form-item>
        <el-form-item prop="value" label="值">
          <el-input v-model="mysqlForm.value" style="width:300px" size="mini" />
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
import { getMysqlList, addMysqlData, delMysqlData, updateMysqlData } from '@/api/mysql'

export default {
  name: 'MysqlPage',
  data() {
    return {
      list: [], // MySQL 数据列表
      showDialog: false, // 控制添加数据弹窗
      pageParams: {
        page: 1, // 当前页码
        pagesize: 5, // 每页数量
        total: 0 // 总条数
      },
      mysqlForm: {
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
    this.getMysqlList()
  },
  methods: {
    async getMysqlList() {
      const { data, total } = await getMysqlList(this.pageParams)
      this.list = data
      this.pageParams.total = total

      this.list.forEach(item => {
        this.$set(item, 'isEdit', false)
        this.$set(item, 'editRow', { name: item.name, value: item.value })
      })
    },
    changePage(newPage) {
      this.pageParams.page = newPage
      this.getMysqlList()
    },
    btnOK() {
      this.$refs.mysqlForm.validate(async isOK => {
        if (isOK) {
          await addMysqlData(this.mysqlForm)
          this.$message.success('新增 MySQL 数据成功')
          this.getMysqlList()
          this.btnCancel()
        }
      })
    },
    btnCancel() {
      this.$refs.mysqlForm.resetFields()
      this.showDialog = false
    },
    async confirmDel(id) {
      await delMysqlData(id)
      this.$message.success('删除 MySQL 数据成功')
      if (this.list.length === 1) this.pageParams.page--
      this.getMysqlList()
    },
    btnEditRow(row) {
      row.isEdit = true
      row.editRow.name = row.name
      row.editRow.value = row.value
    },
    async btnEditOK(row) {
      if (row.editRow.name && row.editRow.value) {
        await updateMysqlData({ id: row.id, name: row.editRow.name, value: row.editRow.value })
        this.$message.success('更新 MySQL 数据成功')
        Object.assign(row, { name: row.editRow.name, value: row.editRow.value, isEdit: false })
      } else {
        this.$message.warning('名称和值不能为空')
      }
    }
  }
}
</script>

  <style scoped>
  .mysql-operate {
    padding: 10px;
  }
  </style>
