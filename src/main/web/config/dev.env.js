'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  LOGIN_SERVER_URL: '/dev',   //   '/dev/cell_link'    puyuting
})
