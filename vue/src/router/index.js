import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    redirect:"user",
    component: Layout,
    children:[
      {
        path:'user',
        name:'user',
        component:() => import("@/views/User")
      },
      {
        path: 'book',
        name: 'book',
        component: () => import("@/views/Book")
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("@/views/Person")
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import("@/views/Password")
      },
      {
        path: 'lendrecord',
        name: 'LendRecord',
        component: () => import("@/views/LendRecord")
      },
      {
        path:'dashboard',
        name:'Dashboard',
        component:() => import("@/views/Dashboard")
      },
      {
        path: 'bookwithuser',
        name: 'BookWithUser',
        component: () => import("@/views/BookWithUser")
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register")
  },
  {
    path: '/forgetstep1',
    name: 'Forget1',
    component: () => import("@/views/step1")
  },
  {
    path: '/forgetstep2/:username',
    name: 'Forget2',
    component: () => import("@/views/step2")
  },
  {
    path: '/forgetstep3/:username/:phone',
    name: 'Forget3',
    component: () => import("@/views/step3")
  },
  {
    path: '/forgetstep4/:username',
    name: 'Forget4',
    component: () => import("@/views/step4")
  },

]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
  mode: 'hash'
})

export default router
