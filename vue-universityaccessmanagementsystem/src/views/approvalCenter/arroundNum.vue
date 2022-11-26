<template>
    <div>
      <el-row class="block"> 请选择时间范围：
        <el-date-picker
          v-model="date"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
        <label>
          <el-button icon="el-icon-search" @click="sum()" type="primary">统计</el-button>
        </label>
      </el-row >
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
    data() {
      return {
        totalData: [],
        tableData: [],
        pagination: {
          currentPage: 1,
          totalResult: 0,
          showCount: 10
        },
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        date: '',
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
        this.$http
          .post('/apply/getRoundApplyInfoNum')
          .then((res) => {
            this.totalData = res.data.applyList
            this.pagination.totalResult = this.totalData.length
              let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
              let end = this.pagination.currentPage * this.pagination.showCount
              this.tableData = this.totalData.slice(begin, end);
          })
      },
      sum() {
        if(this.date === '') {
          this.$message({
            message: '请选择时间范围',
            type: 'warning'
          });
          this.reload()
          return;
        }
        const param = {
          startDate: this.date[0],
          endDate: this.date[1]
        };
        this.$http
          .post('/apply/getRoundApplyInfoNum', param)
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
      this.getList()
    }
  }
  
  </script>
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  </style>
  