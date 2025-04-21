<template>
  <div class="dashboard-container">
    <div class="app-container">
      <div class="edit-form">
        <el-form ref="userForm" :model="userInfo" :rules="rules" label-width="220px">
          <!--手机  -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="手机" prop="username">
                <el-input
                  v-model="userInfo.username"
                  :disabled="!!$route.params.id"
                  size="mini"
                  class="inputW"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 密码 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="密码" prop="password">
                <el-input v-model="userInfo.password" size="mini" class="inputW" />
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 保存个人信息 -->
          <el-row type="flex">
            <el-col :span="12" style="margin-left:220px">
              <el-button size="mini" type="primary" @click="saveData">保存更新</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>

    </div>
  </div>
</template>

<script>

import { addEmployee, getEmployeeDetail, updateEmployee } from '@/api/employee'
export default {
  // components: { SelectTree, ImageUpload },
  data() {
    return {
      userInfo: {
        username: '', // 手机号
        password: ''// 密码
      },
      rules: {
        username: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }

    }
  },
  created() {
    // 如何获取路由参数的中id
    // if (this.$route.params.id) { this.getEmployeeDetail() }
    this.$route.params.id && this.getEmployeeDetail()
  },
  methods: {
    async getEmployeeDetail() {
      this.userInfo = await getEmployeeDetail(this.$route.params.id)
    },
    saveData() {
      this.$refs.userForm.validate(async isOK => {
        if (isOK) {
          // 编辑模式
          if (this.$route.params.id) {
            // 编辑模式
            await updateEmployee(this.userInfo)
            this.$message.success('更新员工成功')
          } else {
            // 新增模式
            // 校验通过
            await addEmployee(this.userInfo)
            this.$message.success('新增员工成功')
          }
          this.$router.push('/employee')
        }
      })
    }
  }
}
</script>

  <style scoped lang="scss">
  .edit-form {
    background: #fff;
    padding: 20px;
    .inputW {
      width: 380px
    }
  }

  </style>

