'use strict'

module.exports = {
	devServer: {
		host: 'localhost',
		port: 8070,
		proxy: {
			'/api/': {
				target: 'http://localhost:8080/',
				secure: false,
			},
			'/oauth2/authorization/': {
				target: 'http://localhost:8080/',
				secure: false,
			},
			'/login/oauth2/code/google': {
				target: 'http://localhost:8080/',
				secure: false,
			},
		},
  },
	css: {
		loaderOptions: {
			css: {
				sourceMap: true
			},
			postcss: {
				sourceMap: true
			}
		}
	},
	productionSourceMap: false
}
