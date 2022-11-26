<template>
    <div>
    <el-row>
      <label>
        <el-button style="margin-bottom: 10px"
            type="primary"
            size="small"
            icon="el-icon-refresh"
            @click="updateRistPlace()"
            >更新风险地区</el-button
          >
        </label>
        <label>
          风险地区同步时间：{{lastTime}}
        </label>
        </el-row>
      <el-switch 
          style="display: block;margin-bottom: 10px"
          v-model="autoApproval"
          active-color="#13ce66"
          @change="updateAutoApproval()"
          inactive-color="#ff4949"
          active-text="自动审批"
          inactive-text="手动审批">
        </el-switch>
        <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="place"
        label="风险地区">
      </el-table-column>
      <el-table-column
        :width="200"
        header-align="center"
        align="center"
        prop="type"
        label="风险类型">
        <template slot-scope="scope">
        <el-tag v-if="scope.row.type === 1"
          :type="'warning'"
          disable-transitions>
          低风险
        </el-tag>
        <el-tag v-else
          :type="'danger'"
          disable-transitions>
          高风险
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
    inject: ['reload'],
    data() {
      return {
        lastTime: '',
        autoApproval: true,
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
        .get('/setting/getSetting')
        .then((res) => {
          if(res.data.setting.checkType === 0) {
            this.autoApproval = true
          } else {
            this.autoApproval = false
          }
          this.lastTime = res.data.setting.riskPlaceUpdateTime
        })
        this.$http
        .get('/ristPlace/getRistPlace')
        .then((res) => {
          this.totalData = res.data.riskPlaces
          this.pagination.totalResult = this.totalData.length
          let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
          let end = this.pagination.currentPage * this.pagination.showCount
          this.tableData = this.totalData.slice(begin, end);
        })
      },
      updateAutoApproval() {
        console.log(this.autoApproval)
        let checkType = ''
        if(this.autoApproval === true) {
          checkType = 0
        } else {
          checkType = 1
        }
        const param = {
          checkType: checkType
        };
        this.$http
        .post('/setting/updateCheckType', param)
        .then((res) => {
            this.$message({
              message: "更新审批方式成功",
              type: 'success'
            });
           this.reload()
        })
      },
      updateRistPlace() {
        this.$http
        .get('/ristPlace/updateRistPlace')
        .then((res) => {
          if(res.data.result === 'success') {
            this.$message({
              message: "更新风险地区成功",
              type: 'success'
            });
           this.reload()
          } else {
            this.$message.error(res.data.message);
          }
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
      this.getList()
    }
  }
  
  </script>
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  </style>
  