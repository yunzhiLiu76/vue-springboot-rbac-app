<template>
  <div class="container">
    <div class="app-container">
      <!-- 操作栏 -->
      <div class="redis-operate">
        <el-button size="mini" type="primary" @click="showDialog = true">添加数据</el-button>
      </div>
      <!-- Redis 数据表格 -->
      <el-table :data="list">
        <el-table-column prop="key" align="center" width="250" label="Redis Key">
          <template v-slot="{ row }">
            <span>{{ row.key }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="value" align="center" label="Redis Value">
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
              <el-popconfirm title="确定删除该数据吗？" @confirm="confirmDel(row.key)">
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
    <el-dialog width="500px" title="新增 Redis 数据" :visible.sync="showDialog" @close="btnCancel">
      <el-form ref="redisForm" :model="redisForm" :rules="rules" label-width="120px">
        <el-form-item prop="key" label="Redis Key">
          <el-input v-model="redisForm.key" style="width:300px" size="mini" />
        </el-form-item>
        <el-form-item prop="value" label="Redis Value">
          <el-input v-model="redisForm.value" style="width:300px" size="mini" />
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
import { getRedisList, addRedisData, delRedisData } from '@/api/redis' // 引入API
export default {
  name: 'RedisPage',
  data() {
    return {
      list: [], // Redis 数据列表
      showDialog: false, // 控制添加数据弹窗
      pageParams: {
        page: 1, // 当前页码
        pagesize: 5, // 每页数量
        total: 0 // 总条数
      },
      redisForm: {
        key: '',
        value: ''
      },
      rules: {
        key: [{ required: true, message: 'Redis Key 不能为空', trigger: 'blur' }],
        value: [{ required: true, message: 'Redis Value 不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getRedisList()
  },
  methods: {
    async getRedisList() {
      const { data, total } = await getRedisList(this.pageParams)
      this.list = Object.keys(data).map((key) => ({
        key,
        value: data[key]
      }))
      // this.list = data // 更新数据
      this.pageParams.total = total
    },
    changePage(newPage) {
      this.pageParams.page = newPage
      this.getRedisList()
    },
    btnOK() {
      this.$refs.redisForm.validate(async isOK => {
        if (isOK) {
          await addRedisData(this.redisForm)
          this.$message.success('新增 Redis 数据成功')
          this.getRedisList()
          this.btnCancel()
        }
      })
    },
    btnCancel() {
      this.$refs.redisForm.resetFields()
      this.showDialog = false
    },
    async confirmDel(key) {
      await delRedisData(key)
      this.$message.success('删除 Redis 数据成功')
      if (this.list.length === 1) this.pageParams.page--
      this.getRedisList()
    }
  }
}
</script>
  <style scoped>
  .redis-operate {
    padding: 10px;
  }
  </style>
