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
        path: 'signIn',
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
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('pages/Dashboard.vue')
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
