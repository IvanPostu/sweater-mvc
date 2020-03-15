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



//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
const INPUT_ROOT_DIRECTORY = '/frontend'
const OUTPUT_ROOT_DIRECTORY = '/frontend_build'
const RESOURCES_DIRECTORY = '/src/main/resources'
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\



module.exports = function /*(env, options)*/() {


  const jsSrcFiles = glob
    .sync('.' + INPUT_ROOT_DIRECTORY + '/**/index.js')

  let entryPaths = jsSrcFiles
  entryPaths = entryPaths
    .reduce((result, item) => (result[item.replace(INPUT_ROOT_DIRECTORY, '')] = item,
      result), {})
  console.log(entryPaths)

  const htmlWebpackPluginArray = []

  for (var key of Object.keys(entryPaths)) {
    htmlWebpackPluginArray.push(
      new HtmlWebpackPlugin({
        chunks: [key],
        template: entryPaths[key].replace('.js', '.html'),
        filename: entryPaths[key].replace('.js', '.html')
          .replace(INPUT_ROOT_DIRECTORY, '/templates'),
        minify: {
          collapseWhitespace: true
        },
      })
    )
  }


  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================
  // ==========================================================================================


  return {
    mode: 'production',
    entry: entryPaths,
    output: {
      filename: 'static/js/[name]',
      path: __dirname + OUTPUT_ROOT_DIRECTORY,
      publicPath: '/'
    },
    resolve: {
      extensions: ['.js'],
      alias: {
        '@': __dirname + INPUT_ROOT_DIRECTORY
      }
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
      new CopyWebpackPlugin([
        {
          from: __dirname + INPUT_ROOT_DIRECTORY + '/libraries',
          to: __dirname + OUTPUT_ROOT_DIRECTORY + '/static/libraries'
        },
        {
          from: __dirname + INPUT_ROOT_DIRECTORY + '/assets',
          to: __dirname + OUTPUT_ROOT_DIRECTORY + '/static/assets'
        }
      ]),
      new FileManagerWebpackPlugin({
        onEnd: [{
          copy: [
            {
              source: __dirname + OUTPUT_ROOT_DIRECTORY + '/',
              destination: __dirname + RESOURCES_DIRECTORY + '/',
            }
          ],
          // delete: [
          //   __dirname + OUTPUT_ROOT_DIRECTORY
          // ]
        }]
      }),
      new MiniCssExtractPlugin({
        filename: 'static/css/[contenthash].[hash].css'
      })
    ],
    module: {
      rules: [
        {
          test: /\.html$/,
          loader: 'html-loader'
        },
        {
          test: /\.scss$/,
          use: [
            'style-loader',
            MiniCssExtractPlugin.loader,
            {
              loader: 'css-loader',
              options: { sourceMap: isDev }
            },
            {
              loader: 'postcss-loader',
              options: {
                sourceMap: isDev,

              }
            },
            {
              loader: 'sass-loader',
              options: { sourceMap: isDev }
            }
          ]
        },
        {
          test: /\.css$/,
          use: [
            'style-loader',
            MiniCssExtractPlugin.loader,
            {
              loader: 'css-loader',
              options: { sourceMap: isDev }
            },
            {
              loader: 'postcss-loader',
              options: {
                sourceMap: isDev,

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
      ]
    }

  }
}