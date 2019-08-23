## 1.安装 node.js
## 2.安装 vue-cli
`npm install -g vue-cli`
## 3.生成项目
`vue init webpack Vue-Project`
`cd 项目文件夹`
`npm run dev` 运行项目

## 插件安装
### 1.echarts
`npm install echarts --save`

### 2.vuex
`npm install vuex --save`
`import Vuex from 'vuex'`

### 3.babel-polyfill
main.js文件引入
`import 'babel-polyfill'`
webpack.base.conf.js文件修改
`entry: {
    app: ["babel-polyfill",'./src/main.js']
},`

### 4.element-ui
`npm i element-ui -S`

main.js文件引入
```import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);
```