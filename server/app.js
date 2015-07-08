var app = require('express')();
var http = require('http').Server(app);
var pg = require('pg').native;
var waterfall = require('async-waterfall');

app.get('/', function (req, res) {
	res.sendfile('index.html');
});


//Socket.io
var connectionString = 'pg://trespass:RW1@bEKaraLaWw!e@localhost/trespass';

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
				socket.emit('Error', 'Could not connect to database.');
				return console.error('Could not connect to database.', err);
			}

			waterfall([
				//Check if user exists in db.
				function (callback) {
					client.query("select * \
						            from tb_entity \
						           where player_info->>'device_id' = $1", [device_id],
						function (err, result) {
							done();
							if (result && result.rowCount > 0) {
								var entity = result.rows[0].entity;
								console.log("Found entity: " + entity);
								callback(null, result.rows[0]);
							}
							callback(err, null);
						});
				},
				function (entity_row, callback) {
					if (entity_row) {
						//Update socket id
						var player_info = entity_row.player_info;
						player_info.socket_id = socket.id;
						client.query("update tb_entity \
									     set player_info = $1 \
									   where entity = $2", [player_info, entity_row.entity],
							function (err, callback) {
								if (err) {
									return callback(err);
								}
								return callback(null, entity_row.entity);
							}
						);
					}

					var player_info = {};
					player_info.device_id = device_id;
					player_info.socket_id = socket.id;
					//Create entity
					client.query("insert into tb_entity (player_info)\
									values( $1 ) returning entity", [player_info],
						function (err, result) {
							if (err) {
								return callback(err);
							}
							callback(null, result.rows[0].entity);
						}
					);
				},
				//Insert user into waiting list.
				function (entity, callback) {
					if (!entity) {
						callback('Unexpected entity pk when inserting player into waiting list.');
					}
					client.query("insert into tb_waiting_list (player)\
									values( $1 ) returning waiting_list", [entity],
						function (err, result) {
							if (err) {
								return callback(err);
							}
							callback(null, result.rows[0].waiting_list);
						}
					);
				},
				//See of anyone is waiting
				function (waiting_list, callback) {
					if (!waiting_list) {
						callback('Unexpected waiting_list pk when finding player.');
					}
					client.query('update tb_waiting_list set filled = now() \
						           where filled is null \
						             and invalidated is null \
						             and waiting_list != $1 \
						           limit 1 \
						       returning waiting_list, player', [waiting_list],
						function (err, result) {
							if (err) {
								callback(err);
							}
							if (result.rowCount > 0) {

							} else {
								socket.emit('Info', 'No player online.');
							}
						}
					);
				}

			], function (err, result) {
				if (err) {
					console.error('Error: ', err);
				}
				done();
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