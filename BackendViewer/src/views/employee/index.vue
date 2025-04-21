<template>
  <div class="container">
    <div class="app-container">
      <div class="right">
        <el-row class="opeate-tools" type="flex" justify="end">
          <el-button size="mini" type="primary" @click="$router.push('/employee/detail')">添加员工</el-button>
        </el-row>
        <!-- 表格组件 -->
        <el-table :data="list">
          <el-table-column prop="username" label="手机号" sortable />
          <el-table-column prop="password" label="密码" />
          <el-table-column label="操作" width="280px">
            <template v-slot="{ row }">
              <!-- <el-button size="mini" type="text" @click="$router.push(`/employee/detail/${row.id}`)">查看</el-button> -->
              <el-button size="mini" type="text" @click="btnRole(row.id)">角色</el-button>
              <el-popconfirm
                title="确认删除该行数据吗？"
                @onConfirm="confirmDel(row.id)"
              >
                <el-button slot="reference" style="margin-left:10px" size="mini" type="text">删除</el-button>
              </el-popconfirm>

            </template>
          </el-table-column>

        </el-table>
        <!-- 分页 -->
        <el-row style="height: 60px" align="middle" type="flex" justify="end">
          <el-pagination
            layout="total,prev, pager, next"
            :total="total"
            :current-page="queryParams.page"
            :page-size="queryParams.pagesize"
            @current-change="changePage"
          />
        </el-row>
      </div>
    </div>
    <el-dialog :visible.sync="showRoleDialog" title="分配角色">
      <!-- 弹层内容 -->
      <!-- checkbox -->
      <el-checkbox-group v-if="roleIds && roleIds.length !== undefined" v-model="roleIds">
        <!-- 放置n个的checkbox  要执行checkbox的存储值 item.id-->
        <el-checkbox
          v-for="item in roleList"
          :key="item.id"
          :label="item.id"
        >{{ item.name }}</el-checkbox>
      </el-checkbox-group>
      <!-- 确定和取消按钮 -->
      <el-row slot="footer" type="flex" justify="center">
        <el-col :span="6">
          <el-button type="primary" size="mini" @click="btnRoleOK">确定</el-button>
          <el-button size="mini" @click="showRoleDialog = false">取消</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { getEmployeeList, delEmployee, getEnableRoleList, assignRole } from '@/api/employee'
import { getRoleByUserId } from '@/api/role'
export default {
  name: 'Employee',
  // components: {
  //   ImportExcel
  // },
  data() {
    return {
      defaultProps: {
        label: 'name',
        children: 'children'
      },
      // 存储查询参数
      queryParams: {
        // departmentId: null,
        page: 1, // 当前页码
        pagesize: 10
      },
      total: 0, // 记录员工的总数
      list: [], // 存储员工列表数据
      showRoleDialog: false, // 用来控制角色弹层的显示
      roleList: [], // 接收角色列表
      roleIds: [], // 用来双向绑定数据的
      currentUserId: null // 用来记录当前点击的用户id
    }
  },
  created() {
    // this.getDepartment()
    this.getEmployeeList()
  },
  methods: {
    // 获取员工列表的方法
    async getEmployeeList() {
      const { content, numberOfElements } = await getEmployeeList(this.queryParams)
      this.list = content
      this.total = numberOfElements
    },
    // 切换页码
    changePage(newPage) {
      this.queryParams.page = newPage // 赋值新页码
      this.getEmployeeList() // 查询数据
    },
    // 删除员工方法
    async confirmDel(id) {
      await delEmployee(id)
      if (this.list.length === 1 && this.queryParams.page > 1) this.queryParams.page--
      this.getEmployeeList()
      this.$message.success('删除员工成功')
    },
    // 点击角色按钮弹出层
    async btnRole(id) {
      debugger
      this.roleList = await getEnableRoleList()
      console.log(this.roleList)
      debugger
      // 记录当前点击的id 因为后边 确定取消要存取给对应的用户
      this.currentUserId = id
      const roles = await getRoleByUserId(id)
      this.roleIds = roles.map(item => item.id)
      this.showRoleDialog = true // 调整顺序
    },
    // 点击角色的确定
    async  btnRoleOK() {
      await assignRole({
        id: this.currentUserId,
        roleIds: this.roleIds
      })
      this.$message.success('分配用户角色成功')
      this.showRoleDialog = false
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  background: #fff;
  display: flex;
  .right {
    flex: 1;
    padding: 20px;
    .opeate-tools {
      margin:10px ;
    }
    // .username {
    //   height: 30px;
    //   width: 30px;
    //   line-height: 30px;
    //   text-align: center;
    //   border-radius: 50%;
    //   color: #fff;
    //   background: #04C9BE;
    //   font-size: 12px;
    //   display:inline-block;
    // }
  }
}

</style>
