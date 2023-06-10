var path = require("path");
var webpack = require('webpack');
var BundleTracker = require('webpack-bundle-tracker');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const devMode = process.env.NODE_ENV !== 'production';


var config = {

  context: __dirname,
  mode: "development",
  module: {
  rules: [

	{
		test: /\.m?js$/,
		exclude: /(node_modules|bower_components)/,
		use: {
		  loader: "babel-loader"
		}
	},

	{
		test: /\.tsx?$/,
		use: {
		  loader:'babel-loader',
		  options: {
		    presets: ['@babel/preset-env']
		  }
		},
		exclude: /node_modules/,

	},

	{
		test: /\.(sa|sc|c)ss$/,
		use: [
		  {
		    loader: MiniCssExtractPlugin.loader,
		    options: {
		      hmr: process.env.NODE_ENV === 'development',
		    },
		  },
		  'css-loader',
		  //'postcss-loader',
		  'sass-loader',
		],
	},

    ]
  },


  resolve: {
    extensions: ['*', '.js', '.jsx','.ts','.tsx']
  }
};





var constructorConfig = Object.assign({}, config, {
    name: "constructor",
    entry: {
	constructor_react:'./js/constructor/constructor-react.js',
	},
    output: {
      path: path.resolve('../src/main/resources/static/'),
      filename: "[name].js",
    },

     plugins: [
     new CleanWebpackPlugin(),
     new BundleTracker({filename: './js/constructor/webpack-constructor.json'}),
	new MiniCssExtractPlugin({
	      filename: devMode ? '[name].css' : '[name].[hash].css',
	      chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
	    }),
    ],

});

var loginConfig = Object.assign({}, config, {
    name: "login",
    entry: {
	constructor_react:'./js/login/login-react.js',
	},
    output: {
      path: path.resolve('../static/'),
      filename: "[name].js",
    },

     plugins: [
     new CleanWebpackPlugin(),
     new BundleTracker({filename: './js/login/webpack-login.json'}),
	new MiniCssExtractPlugin({
	      filename: devMode ? '[name].css' : '[name].[hash].css',
	      chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
	    }),
    ],

});



module.exports = [loginConfig];
