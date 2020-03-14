const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const { CleanWebpackPlugin } = require('clean-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const OptimizeCssAssetsWebpackPlugin = require('optimize-css-assets-webpack-plugin')
const TerserWebpackPlugin = require('terser-webpack-plugin')
const glob = require('glob')
const isDev = process.env.NODE_ENV === 'development'



//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
const INPUT_ROOT_DIRECTORY = '/frontend'
const OUTPUT_ROOT_DIRECTORY = '/src/main/resources'
const HTML_OUT_DIRECTORY = '/templates'
const STATIC_OUT_DIRECTORY = '/static'
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\



module.exports = function (env, options) {


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
          .replace(INPUT_ROOT_DIRECTORY, HTML_OUT_DIRECTORY),
        minify: {
          collapseWhitespace: true
        }
      })
    )
  }

  //Specific method
  const pathToFilename = (path) => (
    path.substring(1, path.length)
  )

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
      filename: pathToFilename(STATIC_OUT_DIRECTORY + '/js/[name]'),
      path: __dirname + OUTPUT_ROOT_DIRECTORY
    },
    resolve: {
      extensions: ['.js'],
      alias: {
        '@': path.resolve(__dirname, 'frontend'),
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
      new MiniCssExtractPlugin({
        filename: pathToFilename(STATIC_OUT_DIRECTORY + '/css/[contenthash].[hash].css')
      })
    ],
    module: {
      rules: [
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