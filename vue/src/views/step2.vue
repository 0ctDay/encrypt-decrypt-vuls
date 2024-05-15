<template>
    <div class="login-container">
      <el-form ref="form" :model="form" :rules="rules" class="login-page">
        <h2 class="title" style="margin-bottom: 20px">请输入手机号</h2>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" clearable placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item v-if="phoneNotExist" style="color: red;">
          手机号输入错误
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="validatePhoneNumber">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import request from "../utils/request";
  export default {
    created() {
    // 从路由参数中获取用户名
      this.username = this.$route.params.username;
    },
    data() {
      return {
        form: {},
        rules: {
          phone: [
            {
              required: true,
              message: '请输入手机号',
              trigger: 'blur'
            }
          ]
        },
        phoneNotExist: false,
        username: ''
      };
    },
    
    methods: {
      validatePhoneNumber() {
        
         // 模拟判断手机号是否正确
         request.post('/forget/sendsms', { username: this.username, phone: this.form.phone })
        .then(response => {
         
          if (response.code == 0) {
            
            this.$router.push({ name: 'Forget3', params: { phone: this.form.phone, username: this.username }});
          } else {
            this.phoneNotExist = true;
          }
        })
        .catch(error => {
          console.error('Error validating username:', error);
        });
        
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
  