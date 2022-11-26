<template>
  <div id="app">
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
          prop="approvalUserName"
          label="审批人">
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
    data () {
      return {
        totalData: [],
        tableData: [],
        pagination: {
          currentPage: 1,
          totalResult: 0,
          showCount: 10
        },
      }
    },
 
    methods: {
      getList() {
        this.$http
          .get('/apply/getOwnerApplyInfo')
            .then((res) => {
              this.totalData = res.data.applyList
              this.pagination.totalResult = this.totalData.length
              let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
              let end = this.pagination.currentPage * this.pagination.showCount
              this.tableData = this.totalData.slice(begin, end);
          })
      },
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
    },
    mounted() {
      this.getList();
    }
  }
</script>
<style scoped>
</style>
