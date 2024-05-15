<template>
    <div class="login-container">
      <el-form ref="form" :model="form" :rules="rules" class="login-page">
        <h2 class="title" style="margin-bottom: 20px">请输入验证码</h2>
        <el-form-item prop="code">
          <el-input v-model="form.code" clearable placeholder="请输入验证码"></el-input>
        </el-form-item>
        <el-form-item v-if="codeNotExist" style="color: red;">
          验证码错误
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="validatePhonecode">下一步</el-button>
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
              message: '请输入验证码',
              trigger: 'blur'
            }
          ]
        },
        codeNotExist: false,
      };
    },
    methods: {
      validatePhonecode() {
        request.get('/forget/checksms', {
          params: {
            code: this.form.code
          }
        })
        .then(response => {
          // 在这里处理请求成功的情况
       
          if (response.code == "0") {
            this.$router.push({ name: 'Forget4', params: { username:this.$route.params.username  }});
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
  