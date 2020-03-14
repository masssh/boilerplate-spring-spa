import Vue from 'vue'
import axios from 'axios'

const _axios = axios.create({
  withCredentials: true
})
Vue.prototype.$axios = _axios
