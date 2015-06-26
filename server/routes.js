var pg = require('pg');
var connectionString = 'pg://trespass:RW1@bEKaraLaWw!e@10.0.1.126/?ssl=true';

module.exports = function (app) {
	var express = require('express');
	var router = express.Router();
	var http = require('http').Server(app);
	var io = require('socket.io')(http);

	io.on('connection', function (socket) {
		console.log('a user connected');
	});

	app.use('/trespass', router);
};