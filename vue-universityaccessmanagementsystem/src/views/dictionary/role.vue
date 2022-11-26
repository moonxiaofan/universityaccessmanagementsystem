<template>
  <div>
    <el-row>
      <label>
        <el-button style="margin-bottom;: 10px"
          type="primary"
          icon="el-icon-edit"
          @click="addButton()"
          >新增</el-button
        >
      </label>
  </el-row>
  <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="角色编号">
      </el-table-column>
      <el-table-column
        prop="roleName"
        label="角色名称">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
    <el-dialog :title="this.dialogTitle" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="角色名称" :label-width="formLabelWidth">
          <el-input v-model="form.roleName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole()">确 定</el-button>
      </div>
    </el-dialog>
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
      form: {
        id: '',
        roleName: '',
      },
      dialogTitle: '1',
      formLabelWidth: '120px',
      dialogFormVisible: false,
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
      .get('/role/getAllList')
      .then((res) => {
        this.totalData = res.data.roleList
        this.pagination.totalResult = this.totalData.length
        let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
        let end = this.pagination.currentPage * this.pagination.showCount
        this.tableData = this.totalData.slice(begin, end);
      })
    },
    handleEdit(index, row) {
      this.form = row
      this.dialogTitle = '修改角色'
      this.dialogFormVisible = true
    },
    handleDelete(index, row) {
      this.form = row
      this.$confirm('此操作将删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
        id: this.form.id,
        roleName: this.form.roleName,
        status: -1
      };
      this.$http
        .post('/role/edit', param)
        .then((res) => {
          if(res.data.result === "success") {
            this.$message({
              message: "删除成功",
              type: 'success'
            });
            this.reload()
          } else {
            this.$message.error(res.data.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    addButton() {
      this.form = []
      this.dialogFormVisible = true;
      this.dialogTitle='新增角色'
    },
    editRole() {
      const param = {
        id: this.form.id,
        roleName: this.form.roleName,
      };
      this.$http
        .post('/role/edit', param)
        .then((res) => {
          if(res.data.result === "success") {
            this.$message({
              message: res.data.message,
              type: 'success'
            });
            this.dialogFormVisible = false;
            this.reload()
          } else {
            this.$message.error(res.data.message);
          }
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
