<template>
    <div class="login-container">
      <el-form ref="form" :model="form" :rules="rules" class="login-page">
        <h2 class="title" style="margin-bottom: 20px">请输入新密码</h2>
        <el-form-item prop="newPassword">
          <el-input type="password"  v-model="form.newPassword" clearable placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" clearable placeholder="请再次输入新密码"></el-input>
        </el-form-item>
        <el-form-item v-if="passNotMatch" style="color: red;">
          密码不一致
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="resetPassword">完成</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import request from "../utils/request";
  export default {
    data() {
      return {
        form: {},
        rules: {
          newPassword: [
            {
              required: true,
              message: '请输入新密码',
              trigger: 'blur',
            }
          ],
          confirmPassword: '',
        },
        passNotMatch: false,
        username: this.$route.params.username
      };
    },
    methods: {
      resetPassword() {
        // 处理修改密码的逻辑
        // this.$router.push('/login'); // 或跳转到登录页面
        request.post('/forget/resetpassword', {
          
            username: this.username,
            password: this.form.newPassword
          
        })
        .then(response => {
          // 在这里处理请求成功的情况
    
          if (response.code == "0") {
            this.$message.success('密码修改成功');

            // 等待一段时间后，跳转到个人资料页面
            setTimeout(() => {
              this.$router.push('/login');
            }, 3000);
          } else {
            this.codeNotExist = true;
          }
          // 根据后端返回的数据进行相应处理
        })
        .catch(error => {
          // 在这里处理请求失败的情况
          console.error(error);
        });
      }
    },
    watch: {
    // 监听确认密码的变化，确保与新密码一致
    'form.confirmPassword'(newValue) {
      if (newValue !== this.form.newPassword) {
        // 如果确认密码与新密码不一致，可以在这里添加一些提示或者处理逻辑
        this.passNotMatch = true;
      }
     }
    }
  };
  </script>
  
  <style scoped>
  /* 样式与原页面保持一致 */
  .login-container {
    position: fixed;
    width: 100%;
    height: 100vh;
    background: url('../img/bg2.svg');
    background-size: contain;
    overflow: hidden;
  }
  .login-page {
    border-radius: 5px;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  </style>
  