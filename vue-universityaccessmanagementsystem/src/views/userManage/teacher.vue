<template>
  <div>
    <el-row>
      <label  for="教师名称">
        教师名称：<el-input id="教师名称" style="width: 150px" placeholder="请输入教师名称" v-model="name"></el-input>
      </label>
      <label>所属学院：
        <el-select v-model="academyId" filterable clearable  placeholder="请选择">
        <el-option
          v-for="item in academys"
          :key="item.id"
          :label="item.academyName"
          :value="item.id">
        </el-option>
      </el-select>  
      </label> 
      <label>
        <el-button icon="el-icon-search" @click="search()" type="primary">查询</el-button>
      </label>
      <label>
        <el-button style="margin: 10px"
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
        prop="username"
        label="用户名">
      </el-table-column>
      <el-table-column
        prop="name"
        label="名字">
      </el-table-column>
      <el-table-column
        prop="beizhu"
        label="备注">
      </el-table-column>
      <el-table-column
        prop="email"
        label="邮箱">
      </el-table-column>
      <el-table-column
        prop="phone"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="academyName"
        label="所属学院">
      </el-table-column>
      <el-table-column label="操作" fixed="right">
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
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="名字" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.beizhu" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属学院" :label-width="formLabelWidth">
        <el-select v-model="form.academyId" placeholder="请选择">
          <el-option
            v-for="item in academys"
            :key="item.id"
            :label="item.academyName"
            :value="item.id">
          </el-option>
        </el-select>  
      </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editAcademy()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ['reload'],
  data() {
    return {
      name: '',
      academys: [],
      academyId: '',
      totalData: [],
      tableData: [],
      pagination: {
        currentPage: 1,
        totalResult: 0,
        showCount: 10
      },
      form: {
        id: '',
        username: '',
        name: '',
        beizhu: '',
        email: '',
        phone: '',
        academyId: '',
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
    search() {
      const param = {
        name: this.name,
        academyId: this.academyId
      };
      this.$http
      .post('/user/getAllTeacherList', param)
      .then((res) => {
        this.totalData = res.data.approverList
        this.pagination.totalResult = this.totalData.length
        let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
        let end = this.pagination.currentPage * this.pagination.showCount
        this.tableData = this.totalData.slice(begin, end);
      })
    },
    getList() {
      this.$http
      .get('/academy/getAllList')
      .then((res) => {
        this.academys = res.data.academyList
      })
      this.$http
      .post('/user/getAllTeacherList')
      .then((res) => {
        this.totalData = res.data.approverList
        this.pagination.totalResult = this.totalData.length
        let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
        let end = this.pagination.currentPage * this.pagination.showCount
        this.tableData = this.totalData.slice(begin, end);
      })

    },
    handleEdit(index, row) {
      this.form = row
      this.dialogTitle = '修改教师人员'
      this.dialogFormVisible = true
    },
    handleDelete(index, row) {
      this.form = row
      this.$confirm('此操作将删除该教师人员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
        id: this.form.id,
        username: this.form.username,
        name: this.form.name,
        beizhu: this.form.beizhu,
        email: this.form.email,
        phone: this.form.phone,
        academyName: this.form.academyName,
        academyId: this.form.academyId,
        status: -1
      };
      this.$http
        .post('/user/editTeacherUser', param)
        .then((res) => {
          if(res.data.result === "success") {
            this.$message({
              message: "删除教师人员成功",
              type: 'success'
            });
            this.dialogFormVisible = false;
            this.reload()
          } else {
            this.$message.error("删除失败");
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
      this.dialogFormVisible = true;
      this.dialogTitle='新增教师人员'
    },
    editAcademy() {
      this.$http
        .post('/user/editTeacherUser', this.form)
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
