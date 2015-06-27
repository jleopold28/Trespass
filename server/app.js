var app = require('express')();
var http = require('http').Server(app);
var pg = require('pg');
var waterfall = require('async-waterfall');

app.get('/', function (req, res) {
	res.sendfile('index.html');
});


//Socket.io
var connectionString = 'pg://trespass:RW1@bEKaraLaWw!e@10.0.1.126/trespass?ssl=true';

var express = require('express');
var router = express.Router();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var waiting_room = io.of('/find_match');
waiting_room.on('connection', function (socket) {
	console.log('A user has connected.');
	socket.on('user_info', function (device_id) {
		if (!device_id) {
			console.log('User with id ' + socket.id + ' sent an invalid device_id');
			return socket.emit('dataError', 'Device id is required.');
		}
		console.log('a user ' + device_id + 'is finding a match for game.');
		pg.connect(connectionString, function (err, client, done) {

			if (err) {
				return console.error('error fetching client from pool', err);
			}

			waterfall([
				//Check if user exists in db.
				function (callback) {
					client.query("select entity \
						            from tb_entity \
						           where player_info->>'device_id' = $1", [device_id],
						function (err, result) {
							done();
							if (result && result.rowCount > 0) {
								var entity = result.rows[0].entity;
								console.log("Found entity: " + entity);
								callback(null, entity)
							}
							callback(err, null);
						});
				},
				function (entity, callback) {
					if (entity) {
						callback(null, entity);
					}

					//Create entity
					console.log('second');
					callback(null, result);
				}
				//Insert user into waiting list.

			], function (err, result) {
				if (err) {
					console.log(err);
				}
			});
		});
	});

	socket.on('disconnect', function () {
		console.log('user disconnected');
	});
});

http.listen(3000, function () {
	console.log('listening on port 3000');
});