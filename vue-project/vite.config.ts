import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'


// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host:'0.0.0.0',// Allow access from any IP address
    port: 8080,// Set the port to 8080
    hmr: true,//启动热加载
    open: true // 项目启动时自动打开浏览器
  },
  resolve: {
    alias:
    [ {
     find:'@',
      replacement: resolve(__dirname, 'src')
    }
  ]
  }
})
