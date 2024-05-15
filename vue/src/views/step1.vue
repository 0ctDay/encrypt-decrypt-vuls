<template>
    <div class="login-container">
      <el-form ref="form" :model="form" :rules="rules" class="login-page">
        <h2 class="title" style="margin-bottom: 20px">找回密码</h2>
        <el-form-item prop="username">
          <el-input v-model="form.username" clearable placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item v-if="accountNotExist" style="color: red;">
          账号不存在
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="validateUsername">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import request from "../utils/request";
  import {ElMessage} from "element-plus";
  export default {
    data() {
      return {
        form: {},
        rules: {
          username: [
            {
              required: true,
              message: '请输入用户名',
              trigger: 'blur'
            }
          ]
        },
        accountNotExist: false // 默认账号存在
      };
    },
    methods: {
      validateUsername() {
         // 模拟判断账号是否存在
         request.post('/forget/checkusername', { username: this.form.username })
        .then(response => {
          
          if (response.code == 0) {
            
            this.$router.push({ name: 'Forget2', params: { username: this.form.username }});
          } else {
            ElMessage.error(response.msg)
          }
        })
        
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
  