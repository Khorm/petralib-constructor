var path = require("path");
var webpack = require('webpack');
var BundleTracker = require('webpack-bundle-tracker');
//const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const devMode = process.env.NODE_ENV !== 'production';


var config = {

  context: __dirname,
  mode: "development",
  node: {
    __dirname: true,  // this makes all the difference
  },
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
		  },
		  'css-loader',
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
      path: path.resolve('../static/'),
      filename: path.join("constructor","constructor.js"),
	  //clean: true,
    },

     plugins: [
     new BundleTracker({filename: path.join('constructor','webpack-constructor.json')}),
	new MiniCssExtractPlugin({
		  filename: devMode ? path.join('constructor','[name].css') : '[name].[hash].css',
		  chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
		}),
	],

});

var loginConfig = Object.assign({}, config, {
    name: "login",
    entry: {
		login_root:'./js/login/login-root.js',
	},
    output: {
      path: path.resolve('../static/'),
      filename: path.join("login","login.js"),
	  //clean: true,
    },

     plugins: [
     new BundleTracker({filename: path.join('login','webpack-login.json')}),
	new MiniCssExtractPlugin({
	      filename: devMode ? path.join('login','[name].css') : '[name].[hash].css',
	      chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
	    }),
    ],

});

var projectsConfig = Object.assign({}, config, {
    name: "projects",
    entry: {
	projects_react:'./js/projects/projects-react.js',
	},
    output: {
      path: path.resolve('../static/'),
      filename:  path.join("projects","projects.js"),
	  //clean: true,
    },

     plugins: [
     //new CleanWebpackPlugin(),
     new BundleTracker({filename: path.join('projects','webpack-projects.json')}),
	new MiniCssExtractPlugin({
	      filename: devMode ? path.join('projects','[name].css') : '[name].[hash].css',
	      chunkFilename: devMode ? '[id].css' : '[id].[hash].css',
	    }),
    ],

});



module.exports = [projectsConfig, loginConfig, constructorConfig];
