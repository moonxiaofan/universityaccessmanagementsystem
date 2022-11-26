<template>
  <div id="app">
    <el-row>
      <label  for="学生名称">
        学生名称：<el-input id="学生名称" style="width: 150px" placeholder="请输入学生名称" v-model="name"></el-input>
      </label>
      <label>
        <el-button icon="el-icon-search" @click="search()" type="primary">查询</el-button>
      </label>
  </el-row>
    <el-table
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          fixed
          prop="name"
          label="姓名">
        </el-table-column>
        <el-table-column
          prop="academyName"
          label="学院">
        </el-table-column>
        <el-table-column
          prop="toPlace"
          label="出访地">
        </el-table-column>
        <el-table-column
          prop="type"
          label="出入类型">
          <template slot-scope="scope">
        <el-tag v-if="scope.row.type === 1"
          :type="'success'"
          disable-transitions>
          入校
        </el-tag>
        <el-tag v-else
          :type="'danger'"
          disable-transitions>
          出校
        </el-tag>
        </template>
      </el-table-column>
        <el-table-column
          prop="startDate"
          label="起始时间">
          <template slot-scope="scope">
            <span>{{ scope.row.startDate | FormatDate('yyyy-MM-dd HH:mm:ss') }}</span>
        </template>
        </el-table-column>
        <el-table-column
          prop="endDate"
          label="结束时间">
          <template slot-scope="scope">
            <span>{{ scope.row.endDate | FormatDate('yyyy-MM-dd HH:mm:ss') }}</span>
        </template>
        </el-table-column>
        <el-table-column
          prop="reason"
          label="原因">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态">
          <template slot-scope="scope">
        <el-tag v-if="scope.row.status === 0"
          :type="'warning'"
          disable-transitions>
          未审批
        </el-tag>
        <el-tag v-else-if="scope.row.status === 1"
          :type="'danger'"
          disable-transitions>
          审批未通过
        </el-tag>
        <el-tag v-else
          :type="'success'"
          disable-transitions>
          已通过
        </el-tag>
        </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="160">
          <template slot-scope="scope">
            <div v-if="scope.row.status === 0">
              <el-button
              size="mini"
              type="success"
              @click="approvalSuccess(scope.$index, scope.row)">通过</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="approvalFail(scope.$index, scope.row)">不通过</el-button>
            </div>
            
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
      <el-pagination
        v-model:currentPage="pagination.currentPage"
        layout="total,sizes,prev,pager,next,jumper"
        :total="pagination.totalResult"
        :page-sizes="[10, 20, 30, 40, 50, 60]"
        v-model:page-size="pagination.showCount"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      ></el-pagination>
    </div>
  </div>
</template>
 
<script>
  export default {
    inject: ['reload'],
    data () {
      return {
        totalData: [],
        tableData: [],
        pagination: {
          currentPage: 1,
          totalResult: 0,
          showCount: 10
        },
        name: '',
      }
    },
 
    methods: {
      handleSizeChange(val) {
        this.pagination.showCount = val;    //动态改变
        let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
        let end = this.pagination.currentPage * this.pagination.showCount
        this.tableData = this.totalData.slice(begin, end);
      },
      handleCurrentChange(val) {
        this.pagination.currentPage = val;    //动态改变
        let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
        let end = this.pagination.currentPage * this.pagination.showCount
        this.tableData = this.totalData.slice(begin, end);
      },
      getList() {
        const param = {
          id: 4
        };
        this.$http
          .post('/approval/getApprovalUserInfo', param)
          .then((res) => {
            this.totalData = res.data.applyList
            this.pagination.totalResult = this.totalData.length
              let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
              let end = this.pagination.currentPage * this.pagination.showCount
              this.tableData = this.totalData.slice(begin, end);
          })
      },
      approvalSuccess(index, row) {
        const param = {
          id: row.id,
          status: 2
        };
        this.$http
          .post('/approval/updateApprovalInfo', param)
          .then((res) => {
            if(res.data.result === "success") {
              this.$message({
                message: res.data.message,
                type: 'success'
              });
              this.reload()
            } else {
              this.$message.error(res.data.message);
            }
          })
      },
      approvalFail(index, row) {
        const param = {
          id: row.id,
          status: 1
        };
        this.$http
          .post('/approval/updateApprovalInfo', param)
          .then((res) => {
            if(res.data.result === "success") {
              this.$message({
                message: res.data.message,
                type: 'success'
              });
              this.reload()
            } else {
              this.$message.error(res.data.message);
            }
          })
      },
      search() {
        const param = {
          id: 4,
          name: this.name
        };
        this.$http
          .post('/approval/getApprovalUserInfo', param)
          .then((res) => {
            this.totalData = res.data.applyList
            this.pagination.totalResult = this.totalData.length
              let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
              let end = this.pagination.currentPage * this.pagination.showCount
              this.tableData = this.totalData.slice(begin, end);
          })
      }
    },
    mounted() {
      this.getList();
    }
  }
</script>
<style scoped>
</style>
