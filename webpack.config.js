const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const OptimizeCssAssetsWebpackPlugin = require('optimize-css-assets-webpack-plugin')
const TerserWebpackPlugin = require('terser-webpack-plugin')
const glob = require('glob')
const isDev = process.env.NODE_ENV === 'development'
const isProd = !isDev



//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
const INPUT_ROOT_DIRECTORY = '/frontend'
const OUTPUT_ROOT_DIRECTORY = '/dist'
const HTML_OUT_DIRECTORY = '/html'
const STATIC_OUT_DIRECTORY = '/static'
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\



function cssLoaders(extraLoader) {
  const loaders = [
    {
      loader: MiniCssExtractPlugin.loader,
      options: {
        hmr: isDev,
        reloadAll: true
      }
    }, 'css-loader'
  ]

  if (extraLoader) {
    loaders.push(extraLoader)
  }

  return loaders
}

function sourceFileSeparator(filePath) {
  let newPath = new String(filePath)

  newPath = newPath.replace('./frontend/', '')
  const str = newPath.substr(newPath.lastIndexOf('/') + 1) + '$';
  newPath = newPath.replace(new RegExp(str), '');

  return newPath
}

module.exports = function (env, options) {


  const jsSrcFiles = glob
    .sync('.' + INPUT_ROOT_DIRECTORY + '/**/index.js')

  let entryPaths = jsSrcFiles
  entryPaths = entryPaths
    .reduce((result, item) => (result[item.replace(INPUT_ROOT_DIRECTORY, STATIC_OUT_DIRECTORY)] = item,
      result), {})
  console.log(entryPaths)


  const htmlWebpackPluginArray = jsSrcFiles.map(item => (
    new HtmlWebpackPlugin({
      chunks: [item],
      template: item.replace('.js', '.html'),
      filename: item.replace('.js', '.html').replace(INPUT_ROOT_DIRECTORY, HTML_OUT_DIRECTORY),
      minify: {
        collapseWhitespace: true
      }
    })
  ))

  return {
    mode: 'production',
    entry: entryPaths,
    output: {
      filename: '[name]',
      path: __dirname + OUTPUT_ROOT_DIRECTORY
    },
    optimization: {
      splitChunks: {
        chunks: 'all',
      },
      minimizer: [
        new OptimizeCssAssetsWebpackPlugin(),
        new TerserWebpackPlugin()
      ]
    },
    plugins: [
      ...htmlWebpackPluginArray,
      new CleanWebpackPlugin(),
      new MiniCssExtractPlugin({
        filename: '[hash].[hash].css'
      })
    ],
    module: {
      rules: [
        {
          test: /\.css$/,
          use: cssLoaders()

        },
        {
          test: /\.less$/,
          use: cssLoaders('less-loader')

        },
        {
          test: /\.s[ac]ss$/,
          use: cssLoaders('sass-loader')

        },
        {
          test: /\.js$/,
          exclude: /node_modules/,
          loader: {
            loader: 'babel-loader',
            options: {
              presets: [
                '@babel/preset-env'
              ]
            }
          }
        },
        {
          test: /\.ts$/,
          exclude: /node_modules/,
          loader: {
            loader: 'babel-loader',
            options: {
              presets: [
                '@babel/preset-env',
                '@babel/preset-typescript'
              ]
            }
          }
        },
      ]
    }

  }
}