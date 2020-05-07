const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const lodash = require('lodash')

function getConfiguration(env) {
  let data = require(`./src/config/${env}`)
  const defaultData = require('./src/config/default')

  data = lodash.assign(defaultData, data)

  return data
}

module.exports = (env) => {
  env = env || 'local';
  console.log('Environment: ' + env);

  return {
    entry: path.join(__dirname, '/src/index.js'),
    output: {
      filename: 'build.js',
      path: path.join(__dirname, 'dist')
    },
    module: {
      rules: [
        {
          test: /\.js$/,
          exclude: /node_modules/,
          loader: 'babel-loader'
        }, {
          test: /\.scss$/,
          use: [
            'style-loader',
            'css-loader',
            'postcss-loader',
            'sass-loader'
          ]
        }, {
          test: /\.css$/,
          use: [
            'style-loader',
            'css-loader',
            'postcss-loader'
          ]
        },
        {
          test: /\.html$/,
          use: [{
            loader: 'html-loader',
            options: {
              minimize: false,
              root: path.resolve(__dirname, 'src')
            }
          }]
        }
      ]
    },
    plugins: [
      new HtmlWebpackPlugin(
        {
          template: path.join(__dirname, '/src/index.html')
        }
      )
    ]
  }
}
