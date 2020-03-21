// const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const OptimizeCssAssetsWebpackPlugin = require('optimize-css-assets-webpack-plugin')
const TerserWebpackPlugin = require('terser-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const FileManagerWebpackPlugin = require('filemanager-webpack-plugin')
const glob = require('glob')
const isDev = process.env.NODE_ENV === 'development'
const isProd = !isDev


//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
const INPUT_ROOT_DIRECTORY = '/frontend'
const OUTPUT_ROOT_DIRECTORY = '/frontend_build'
const RESOURCES_DIRECTORY = '/src/main/resources'
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

const staticLibraries = `


  jquery.min.js|
  jquery.js|
  bootstrap.min.js|
  bootstrap.js|
  bootstrap.bundle.min.js|
  bootstrap.bundle.js
  
`.replace(/\s/g, '')


module.exports = function /*(env, options)*/() {


  const jsSrcFiles = glob
    .sync('.' + INPUT_ROOT_DIRECTORY + '/**/index.js')
    .filter(item => !item.includes('libraries'))

  let entryPaths = jsSrcFiles
  entryPaths = entryPaths
    .reduce((result, item) => (result[item.replace(INPUT_ROOT_DIRECTORY, '')] = item,
      result), {})
  console.log(entryPaths)
  console.log(`Mode: ${isDev ? 'development' : 'production'}`)

  const htmlWebpackPluginArray = []

  for (let key of Object.keys(entryPaths)) {
    htmlWebpackPluginArray.push(
      new HtmlWebpackPlugin({
        chunks: [key],
        template: entryPaths[key].replace('.js', '.html'),
        filename: entryPaths[key].replace('.js', '.html')
          .replace(INPUT_ROOT_DIRECTORY, '/templates'),
        minify: {
          collapseWhitespace: isProd,
          removeComments: isProd,
          removeRedundantAttributes: isProd,
          removeScriptTypeAttributes: isProd,
          removeStyleLinkTypeAttributes: isProd,
          useShortDoctype: isProd
        },
      })
    )
  }


  // ==========================================================================================
  // ==========================================================================================


  return {
    mode: 'production',
    entry: entryPaths,
    output: {
      filename: 'static/js/[name]',
      path: __dirname + OUTPUT_ROOT_DIRECTORY
    },
    resolve: {
      extensions: ['.js'],
      alias: {
        '@': __dirname + INPUT_ROOT_DIRECTORY
      }
    },
    optimization: {
      minimize: isProd,
      minimizer: isProd ? [] : [
        new OptimizeCssAssetsWebpackPlugin(),
        new TerserWebpackPlugin()
      ]
    },
    plugins: [
      ...htmlWebpackPluginArray,
      new CleanWebpackPlugin(),
      new CopyWebpackPlugin([
        {
          from: __dirname + INPUT_ROOT_DIRECTORY + '/assets',
          to: __dirname + OUTPUT_ROOT_DIRECTORY + '/static/assets'
        }
      ]),
      new FileManagerWebpackPlugin({
        onEnd: [{
          delete: [
            __dirname + RESOURCES_DIRECTORY + '/static',
            __dirname + RESOURCES_DIRECTORY + '/templates'
          ],
          copy: [
            {
              source: __dirname + OUTPUT_ROOT_DIRECTORY + '/',
              destination: __dirname + RESOURCES_DIRECTORY + '/',
            }
          ],
        }]
      }),
      new MiniCssExtractPlugin({
        filename: 'static/css/[contenthash].[hash].css',
      })
    ],
    module: {
      rules: [
        {
          test: /\/bootstrap.min.css$/,
          loader: 'file-loader',
          options: {
            name: '/static/css/[name].[ext]',
          },
        },
        {
          test: new RegExp(`\\/(${staticLibraries})$`),

          loader: 'file-loader',
          options: {
            name: '/static/js/[name].[ext]',
          },
        },
        {
          test: /\.html$/,
          loader: 'html-loader'
        },
        {
          test: /\.(sa|sc|c)ss$/,
          exclude: /node_modules/,
          use: [
            {
              loader: MiniCssExtractPlugin.loader,
            },
            {
              loader: 'css-loader',
              options: {
              }
            },
            {
              loader: 'postcss-loader',
              options: {

              }
            },
            {
              loader: 'sass-loader',
              options: {
                sourceMap: false
              }
            }
          ]
        },
        {
          test: /\.js$/,
          exclude: /node_modules/,
          use: [{
            loader: 'babel-loader',
            options: {
              presets: [
                '@babel/preset-env'
              ]
            }
          }]
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
        {
          test: /\.(ttf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
          loader: 'file-loader',
        },
      ]
    }

  }
}