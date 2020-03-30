const routes = [
  {
    path: '/',
    name: '',
    component: () => import('layouts/MainLayout.vue'),
    meta: {
      public: true
    },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('pages/Home.vue'),
        meta: {
          public: true
        }
      },
      {
        path: 'login',
        name: 'Sign In',
        component: () => import('pages/SignIn.vue'),
        meta: {
          public: true
        }
      },
      {
        path: 'signUp',
        name: 'Sign Up',
        component: () => import('pages/SignUp.vue'),
        meta: {
          public: true
        }
      },
      {
        path: 'password/reset',
        name: 'Reset Password',
        component: () => import('pages/ResetPassword.vue'),
        meta: {
          public: true
        }
      },
      {
        path: 'password/forgot',
        name: 'Forgot Password',
        component: () => import('pages/ForgotPassword.vue'),
        meta: {
          public: true
        }
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('pages/Dashboard.vue')
      },
      {
        path: 'user',
        name: 'User Profile',
        component: () => import('pages/UserProfile.vue')
      },
      {
        path: 'user/profile/edit',
        name: 'User Profile Edit',
        component: () => import('pages/UserProfileEdit.vue')
      },
      {
        path: 'user/password/edit',
        name: 'User Password Edit',
        component: () => import('pages/UserPasswordEdit.vue')
      }
    ]
  }
]

// Always leave this as last one
if (process.env.MODE !== 'ssr') {
  routes.push({
    path: '*',
    component: () => import('pages/Error404.vue')
  })
}

export default routes
