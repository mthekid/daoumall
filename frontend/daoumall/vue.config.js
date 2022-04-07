const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
})

module.exports = {
  devServer: {
      headers: { "Access-Control-Allow-Origin": "*" },
      port : 8082,
      host: "localhost",
      open: true,
      proxy: 'http://localhost:8084'
  }
}