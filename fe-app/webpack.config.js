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
      filename: '[name].[hash].js',
      chunkFilename: '[name].[chunkhash].js',
      path: path.join(__dirname, 'build')
    },
    optimization: {
      splitChunks: {
        chunks: 'all'
      }
    },
    resolve: {
      extensions: ['.ts', '.tsx', '.js', ".jsx"],
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
        }, {
          test: /\.html$/,
          use: [{
            loader: 'html-loader',
            options: {
              minimize: false,
              root: path.resolve(__dirname, 'src')
            }
          }]
        }, {
          test: /\.(jpg|jpeg|png|svg|woff|eot|ttf|otf|pdf)$/,
          use: ['file-loader']
        }
      ]
    },
    plugins: [
      new HtmlWebpackPlugin(
        {
          template: path.join(__dirname, '/src/index.html')
        }
      )
      // new MiniCssExtractPlugin(),
      // new CopyPlugin([{
      //   from: path.join(__dirname, 'public'),
      //   to: path.join(__dirname, 'build')
      // }]),
      // new webpack.HotModuleReplacementPlugin()
    ],
    devServer: {
      port: 9002,
      host: '0.0.0.0',
      useLocalIp: true,
      compress: true,
      disableHostCheck: true,
      hot: true,
      hotOnly: true,
      open: true,
      overlay: true,
      stats: 'minimal',
      clientLogLevel: 'warning',
      contentBase: path.join(__dirname, 'src'),
      historyApiFallback: {
        disableDotRule: true
      },
    },
    stats: 'minimal',
    mode: 'development'
  }
}
