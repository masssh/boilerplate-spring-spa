module.exports = {
  devServer: {
    port: 8888,
    proxy: {
      '/api': {
        target: 'http://localhost:8080'
      },
      '/oauth2': {
        target: 'http://localhost:8080'
      }
    }
  }
}
