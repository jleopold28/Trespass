var app = require('express')();
var http = require('http').Server(app);
var pg = require('pg');
var waterfall = require('async-waterfall');
var async = require('async');
app.get('/', function (req, res) {
	res.sendfile('index.html');
});

/*
Emit events:
	dataError: error inputs
	Error: connection/database error
	Info: information.
	userInfo: user tile arrangment
	Game: start game indication
	Move: the other player's move, also indicates your turn.
	End: Game ends, either you win/lose.
*/
//Socket.io
var connectionString = 'pg://trespass:RW1@bEKaraLaWw!e@10.0.1.126/trespass';

var express = require('express');
var router = express.Router();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var waiting_room = io.of('/trespass');
waiting_room.on('connection', function (socket) {
	console.log('A user has connected.');
	socket.on('user_info', function (user_info) {
		var device_id = user_info.device_id;
		var tiles = user_info.tiles;
		if (!device_id) {
			console.log('User with id ' + socket.id + ' sent an invalid device_id');
			return socket.emit('dataError', 'Device id is required.');
		}
		if (!tiles) {
			console.log('User with id ' + socket.id + ' sent an invalid device_id');
			return socket.emit('dataError', 'Tile arrangment is required.');
		}
		console.log('a user ' + device_id + ' is finding a match for game.');

		waterfall([
				//Check if user exists in db.
				function (callback) {
					console.log('Checking if user with id ' + device_id + ' exists in database.');
					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}

						client.query("select * \
						            from tb_entity \
						           where player_info->>'device_id' = $1", [device_id],
							function (err, result) {
								done();
								if (result && result.rowCount > 0) {
									var entity = result.rows[0].entity;
									console.log("User with id '" + device_id + "' is entity: " + entity);
									callback(null, result.rows[0]);
								} else {
									console.log('User with id ' + device_id + ' does not exist in database.');
									callback(err, null);
								}
							});
					});
				},
				function (entity_row, callback) {
					if (entity_row) {
						//Update socket id
						var player_info = entity_row.player_info;
						player_info.socket_id = socket.id;
						pg.connect(connectionString, function (err, client, done) {
							console.log('Updating socket id for user with id ' + device_id + '.');
							if (err) {
								socket.emit('Error', 'Could not connect to database.');
								return console.error('Could not connect to database.', err);
							}
							client.query("update tb_entity \
									     set player_info = $1 \
									   where entity = $2", [player_info, entity_row.entity],
								function (err, result) {
									done();
									if (err) {
										return callback(err);
									}
									return callback(null, entity_row.entity);
								}
							);
						});
					} else {
						var player_info = {};
						player_info.device_id = device_id;
						player_info.socket_id = socket.id;
						//Create entity

						pg.connect(connectionString, function (err, client, done) {

							if (err) {
								socket.emit('Error', 'Could not connect to database.');
								return console.error('Could not connect to database.', err);
							}
							console.log('Creating user with device id ' + device_id + '.');

							client.query("insert into tb_entity (player_info)\
									values( $1 ) returning entity", [player_info],
								function (err, result) {
									done();
									if (err) {
										return callback(err);
									}
									callback(null, result.rows[0].entity);
								}
							);
						});
					}
				},
				//Insert user into waiting list.
				function (entity, callback) {
					if (!entity) {
						callback('Unexpected entity pk when inserting player into waiting list.');
					}

					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						var insert_waiting_list = function () {
							console.log('Inserting user: ' + entity + ' into waiting list.');
							client.query("insert into tb_waiting_list (player, tiles) \
									values( $1, $2) returning waiting_list", [entity, tiles],
								function (err, result) {
									done();

									if (err) {
										return callback(err);
									}
									callback(null, result.rows[0].waiting_list);
								}
							);
						};
						client.query('select waiting_list from tb_waiting_list where player = $1 and filled is null and invalidated is null', [entity],
							function (err, result) {
								if (err) {
									callback(err);
								}
								if (result.rows[0]) {
									console.log('User: ' + entity + ' is already in waiting list id: ' + result.rows[0].waiting_list);
									done();
									callback(null, result.rows[0].waiting_list);
								} else {
									insert_waiting_list();
								}
							}
						);
					});
				},
				//See of anyone is waiting
				function (waiting_list, callback) {
					if (!waiting_list) {
						callback('Unexpected waiting_list pk when finding player.');
					}
					var client = new pg.Client(connectionString);

					client.connect(function (err) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query('begin', function (err, result) {
							console.log('Starting transaction for waiting list id: ' + waiting_list);
							if (err) return callback(err);
							callback(null, waiting_list, client);
						});

					});
				},
				//Lock table
				function (waiting_list, client, callback) {
					client.query('lock tb_waiting_list', function (err) {
						console.log('Locking table for waiting list id: ' + waiting_list);

						if (err) {
							return callback(err);
						}

						callback(null, waiting_list, client);
					});
				},
				function (waiting_list, client, callback) {
					client.query('select * from tb_waiting_list \
						           where filled is null \
						             and invalidated is null \
						             and waiting_list != $1', [waiting_list],
						function (err, result) {
							if (err) {
								return callback(err);
							}
							if (result.rowCount > 0) {
								//Notify the person that a new player is online.
								callback(null, waiting_list, result.rows, client)
							} else {
								client.end();
								console.log('Transaction ended. No player online.');
								return socket.emit('Info', 'No player online.');
							}
						}
					);
				},
				function (waiting_list, wait_listed_players, client, callback) {
					if (!wait_listed_players) {
						console.log('Unexpected wait listed player entry.');
						return socket.emit('Error: ', 'Database error');
					}
					async.eachSeries(wait_listed_players, function (wait_list_row, inner_callback) {
						//First, Get player's socket id.
						if (!wait_list_row) {
							var err = 'Wait list row undefined.';
							console.log(err);
							socket.emit('Error', 'Database error');
							return callback(err);
						}
						client.query("select player_info->>'socket_id' as socket_id \
													from tb_entity where entity = $1", [wait_list_row.player],
							function (err, result) {
								if (err) {
									return callback(err);
								}
								console.log("Getting the other player's socket id");
								if (result.rowCount > 0) {
									var socket_id = result.rows[0].socket_id;
									console.log("The other player's socket id: " + socket_id);

									//Proceed to next.
									if (io.sockets.connected[socket_id]) {
										client.query("update tb_waiting_list \
														 set filled = now() \
													   where waiting_list in ( $1, $2 )", [waiting_list, wait_list_row.waiting_list], function (err, result) {
											if (err) {
												client.end();
												return callback(err);
											}
											return callback(null, waiting_list, wait_list_row, socket_id, client);
										});
									} else {
										console.log("The other player's socket id: " + socket_id + ' is invalid. Remove wait list entry.');

										//Invalidate this wait list entry
										client.query("update tb_waiting_list \
														 set invalidated = now() \
													   where waiting_list = $1", [wait_list_row.waiting_list], function (err, result) {
											if (err) {
												client.query('commit', function (err, result) {
													client.end();
													return callback(err);
												})
											}
											inner_callback();
										});
									}
								} else {
									var err = 'Wait listed player: ' + JSON.stringify(waiting_list_row) + ' does not exist.';
									client.end();
									return callback(err);
								}
							});
					}, function (err, result) {
						if (!err && !result) {
							//No players online.
							client.query('commit', function (err, result) {
								client.end();
								console.log('Transaction ended. No player online.');
								return socket.emit('Info', 'No player online.');
							});
						}
					});
				},
				//End transcation
				function (waiting_list, waiting_list_row, socket_id, client, callback) {
					client.query("commit", function (err, result) {
						client.end();
						if (err) {
							return callback(err);
						}
						callback(null, waiting_list, waiting_list_row, socket_id);
					});
				},
				//Make a new game.
				function (waiting_list, waiting_list_row, socket_id, callback) {
					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query('insert into tb_game( player_1, player_2, player_1_tiles, player_2_tiles ) \
										( select wl.player, wl2.player, $1, $2 \
											from tb_waiting_list wl,\
											     tb_waiting_list wl2 \
										   where wl.waiting_list = $3 \
											 and wl2.waiting_list = $4 \
											) returning game ', [tiles, waiting_list_row.tiles, waiting_list, waiting_list_row.waiting_list], function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							var row = result.rows[0];
							callback(null, row, socket_id, waiting_list_row.tiles);
						});
					});
				},
				//Tell players the game PK that they are in.
				function (game_row, socket_id, player2_tiles, callback) {
					if (io.sockets.connected[socket_id]) {
						console.log('Starting game id: ' + game_row.game);
						socket.emit('userInfo', player2_tiles);
						socket.emit('Game', game_row.game);
						socket.broadcast.to(socket_id).emit('userInfo', tiles);
						socket.broadcast.to(socket_id).emit('Game', game_row.game);
					} else {
						console.log('Player with socket id: ' + socket_id + ' has dropped connection.');
						return socket.emit('Error', 'The other player dropped connection.');
					}
				}
			],
			function (err, result) {
				if (err) {
					console.error('Error: ', err);
				}
			});
	});

	socket.on('start_game', function (game, secret_number, device_id) {
		if (!device_id) {
			console.log('User with socket id ' + socket.id + ' sent an invalid device_id when starting game.');
			return socket.emit('dataError', 'Device id is required.');
		}

		if (!game) {
			console.log('User with socket id ' + socket.id + ' sent an invalid game id when starting game.');
			return socket.emit('dataError', 'Game id is required.');
		}
		if (secret_number < 0 || secret_number > 9) {
			console.log('User with socket id ' + socket.id + ' sent an invalid secret number when starting game.');
			return socket.emit('dataError', 'Invalid secret number.');
		}

		//Check if the other player is ready( if player_1/2_secret is filled )
		var client = new pg.Client(connectionString);

		waterfall([
				function (callback) {
					client.connect(function (err) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query('begin', function (err, result) {
							if (err) return callback(err);
							client.query('lock tb_game', function (err, result) {
								callback(err, client);
							});
						});

					});
				},
				function (client, callback) {
					var query = "select g.*, e.entity from tb_game g \
						   join tb_entity e \
						     on e.player_info->>'device_id' = $1\
				 		  where ( player_1 = e.entity \
				 		     or player_2 = e.entity ) \
					        and g.started is null \
					        and g.game = $2";
					client.query(query, [device_id, game], function (err, result) {
						if (err) {
							client.end();
							return callback(err);
						}
						if (result.rowCount > 0) {
							callback(null, result.rows[0], client);
						} else {
							client.end();
							console.log('Game with id: ' + game + ' for device id: ' + device_id + ' not found.');
							return socket.emit('dataError', 'Game not found.');
						}
					});
				},
				//Insert secret number
				function (game_row, client, callback) {
					var query = '';
					console.log('Inserting secret number for entity: ' + game_row.entity + ' in game: ' + game_row.game);
					if (game_row.player_1 === game_row.entity) {
						query = 'update tb_game set player_1_secret = $1 where game = $2';
						client.query(query, [secret_number, game], function (err, result) {
							if (err) return callback(err);
							if (game_row.player_2_secret) {
								callback(null, client, 1);
							} else {
								client.query('commit', function (err, result) {
									client.end();
									if (err) {
										return callback(err);
									}
									socket.emit('Info', 'The other player not ready.');
									return console.log('The other player not ready.');
								});
							}
						});
					} else {
						query = 'update tb_game set player_2_secret = $1 where game = $2';
						client.query(query, [secret_number, game], function (err, result) {
							if (err) return callback(err);
							if (game_row.player_1_secret) {
								callback(null, client, 2);
							} else {
								client.query('commit', function (err, result) {
									client.end();
									if (err) {
										return callback(err);
									}
									socket.emit('Info', 'The other player not ready.');
									return console.log('The other player not ready.');
								});
							}
						});
					}
				},
				//Start game and commit
				function (client, player_number, callback) {
					var query = 'update tb_game set started = now() where game = $1';
					client.query(query, [game], function (err, result) {
						if (err) return callback(err);
						callback(null, client, player_number);
					});
				},
				function (client, player_number, callback) {
					client.query('commit', function (err, result) {
						client.end();
						if (err) {
							return callback(err);
						}
						callback(null, player_number);
					});
				},
				//Notify players that the game is started.
				function (player_number, callback) {
					var query;
					if (player_number === 1) {
						query = "select player_info->'socket_id' as socket_id\
						      from tb_game g \
						      join tb_entity e \
						        on e.entity = g.player_2 \
						     where game = $1"

					} else {
						query = "select player_info->'socket_id' as socket_id\
						      from tb_game g \
						      join tb_entity e \
						        on e.entity = g.player_1 \
						     where game = $1"
					}
					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query(query, [game], function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							var socket_id = result.rows[0].socket_id;
							if (io.sockets.connected[socket_id]) {
								socket.emit('Info', 'Game started.');
								socket.emit('Move', ''); //Tell the user it's his/her turn.
								socket.broadcast.to(socket_id).emit('Info', 'Game started');
							} else {
								console.log('Player with socket id: ' + socket_id + ' has dropped connection.');
								return socket.emit('Error', 'The other player has dropped connection.');
							}
						});
					});
				}
			],
			function (err, result) {
				if (err) {
					console.error('Error: ', err);
				}
			}
		);
	});

	//from_position: {'row': 0, 'column': 0 }
	socket.on('new_move', function (input) {
		var game = input.game;
		var device_id = input.device_id;
		var from_position = input.from_position;
		var to_position = input.to_position;
		var game_piece = input.game_piece;
		if (!game) {
			console.log('User with socket id ' + socket.id + ' sent an invalid game id when moving a piece.');
			return socket.emit('dataError', 'Game id is required.');
		}

		if (!device_id) {
			console.log('User with socket id ' + socket.id + ' sent an invalid device id when moving a piece.');
			return socket.emit('dataError', 'Device id is required.');
		}

		if (!from_position || !from_position.row || from_position.column) {
			console.log('User with socket id ' + socket.id + ' sent an invalid from position when moving a piece.');
			return socket.emit('dataError', 'From position is required.');
		}

		if (!to_position || !to_position.row || !to_position.column) {
			console.log('User with socket id ' + socket.id + ' sent an invalid device id when moving a piece.');
			return socket.emit('dataError', 'To position is required.');
		}

		if (game_piece < 0 || game_piece > 9) {
			console.log('User with socket id ' + socket.id + ' sent an invalid game piece when moving a piece.');
			return socket.emit('dataError', 'Game piece is invalid.');
		}
		waterfall([
				//Find the game.
				function (callback) {
					var query = "select g.*, e.entity from tb_game g \
						   join tb_entity e \
						     on e.player_info->>'device_id' = $1\
				 		  where ( player_1 = e.entity \
				 		     or player_2 = e.entity ) \
					        and g.started is not null \
					        and g.finished is null \
					        and g.aborted is null \
					        and g.game = $2";

					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query(query, [device_id, game], function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							if (result.rowCount > 0) {
								callback(null, result.rows[0]);
							} else {
								console.log('Game with id: ' + game + ' for device id: ' + device_id + ' not found in new move.');
								return socket.emit('dataError', 'Game not found.');
							}
						});
					});
				},
				//Check if it's this player's turn.
				function (game_row, callback) {
					//Order moves by desc.
					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query('select entity from tb_player_move\
								   where game = $1 order by moved desc limit 1', [game_row.game], function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							if (result.rowCount > 0) {
								if (result.rows[0].entity === game_row.entity) {
									console.log('Not player ' + game_row.entity + "'s turn in game: " + game_row.game);
									return socket.emit('dataError', 'Not your turn.');
								}
							}
							callback(null, game_row);
						});
					});
				},
				//Insert new move.
				function (game_row, callback) {
					console.log('Inserting new move for entity: ' + game_row.entity);
					pg.connect(connectionString, function (err, client, done) {

						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						client.query('insert into tb_player_move( \
																game, \
																entity,\
																from_position, \
																to_position, \
																game_piece\
															) \
						values($1, $2, $3, $4, $5)\
						', [game_row.game, game_row.entity, from_position, to_position, game_piece],
							function (err, result) {
								done();

								if (err) {
									return callback(err);
								}
								callback(null, game_row);
							});
					});
				},
				//Get other player's socket id
				function (game_row, callback) {
					pg.connect(connectionString, function (err, client, done) {
						if (err) {
							socket.emit('Error', 'Could not connect to database.');
							return console.error('Could not connect to database.', err);
						}
						var query = "select e.player_info->>'socket_id' as socket_id\
					   			   from tb_game g \
					   			   join tb_entity e \
					   			     ";
						if (game_row.entity === game_row.player_1) {
							query = query + " on e.entity = g.player_2 \
						          where g.game = $1";
						} else {
							query = query + " on e.entity = g.player_2 \
						          where g.game = $1";
						}
						client.query(query, [game_row.game], function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							var socket_id = result.rows[0].socket_id;
							callback(null, game_row, socket_id);
						});
					});
				},
				//Check win status
				function (game_row, socket_id, callback) {
					var win = false;
					if (to_position.row === 0) {
						if (game_row.entity === game_row.player_1) {
							if (game_piece === game_row.player_1_secret) {
								win = true;
							}
						} else {
							if (game_piece === game_row.player_2_secret) {
								win = true;
							}
						}
					}
					if (win) {
						console.log('Win status achieved for game: ' + game_row.game);
						pg.connect(connectionString, function (err, client, done) {

							if (err) {
								socket.emit('Error', 'Could not connect to database.');
								return console.error('Could not connect to database.', err);
							}
							socket.emit('End', 'You win');
							client.query('update tb_game set finished = now(), winner = $1 where game = $2', [game_row.entity, game_row.game], function (err, result) {
								done();
								if (io.sockets.connected[socket_id]) {
									socket.broadcast.to(socket_id).emit('End', 'You lose');
								} else {
									console.log('Socket id: ' + socket_id + ' unavailable.');
								}
							});
						});
					} else {
						console.log('Notifying other player of the move.');
						socket.emit('Info', 'Wait for the other player.');
						if (io.sockets.connected[socket_id]) {
							var move = {};
							move.from = from_position;
							move.to = to_position;
							socket.broadcast.to(socket_id).emit('Move', JSON.stringify(move));
						} else {
							console.log('Player with socket id: ' + socket_id + ' has dropped connection.');
							return socket.emit('Error', 'The other player has dropped connection.');
						}
					}
				}
			],
			function (err, result) {
				if (err) {
					console.error('Error', err);
				}
			}
		);
	});

	socket.on('disconnect', function () {
		//Invalidate user in waiting list.
		console.log('User with id: ' + socket.id + ' is trying to disconnect. Checking for waiting list/game entry');
		waterfall([
			function (callback) {
				pg.connect(connectionString, function (err, client, done) {
					client.query("update tb_waiting_list set invalidated = now() \
		where player = ( select entity from tb_entity where player_info ->> 'socket_id' = $1 )\
		  and filled is null and invalidated is null returning player", [socket.id],
						function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							if (result.rowCount > 0) {
								console.log('Waiting list entry for user ' + result.rows[0].player + ' invalidated.');
							} else {
								console.log('No wait list entry for user with socket id: ' + socket.id);
							}
							callback();
						}
					);
				});
			},
			//check game entry.
			function (callback) {
				pg.connect(connectionString, function (err, client, done) {
					client.query("select g.*, e.entity \
								    from tb_game g \
								    join tb_entity e \
								      on e.entity = g.player_1 \
								      or e.entity = g.player_2 \
								   where e.player_info->>'socket_id' = $1 \
								     and g.finished is null \
								     and g.aborted is null", [socket.id],
						function (err, result) {
							done();
							if (err) {
								return callback(err);
							}
							if (result.rowCount > 0) {
								var row = result.rows[0];
								console.log('Active game with id ' + row.game + ' for user ' + row.entity + ' \
									is found. Setting game status to abort.');
								callback(null, row);
							} else {
								return console.log('No active game found for user with socket id: ' + socket.id);
							}
						});
				});
			},
			//Change game status to abort if applicable
			function (game_row, callback) {
				if (!game_row) {
					return callback('Unexpected game row when trying to change game status.');
				}
				pg.connect(connectionString, function (err, client, done) {
					client.query("update tb_game g set aborted = now() where game = $1", [game_row.game], function (err, result) {
						done();
						if (err) {
							return callback(err);
						}
						callback(null, game_row);
					})
				});
			},
			//notify the other player that this player has dropped connection.
			function (game_row, callback) {
				if (!game_row) {
					return callback('Unexpected game row when trying to notify user of dropped connection.');
				}
				console.log('Notifying player of dropped connection for game id: ' + game_row.game);
				pg.connect(connectionString, function (err, client, done) {
					var query = "select e.player_info->>'socket_id' as socket_id\
					   			   from tb_game g \
					   			   join tb_entity e \
					   			     ";
					if (game_row.entity === game_row.player_1) {
						query = query + " on e.entity = g.player_2 \
						          where g.game = $1";
					} else {
						query = query + " on e.entity = g.player_1 \
						          where g.game = $1";
					}
					client.query(query, [game_row.game], function (err, result) {
						done();
						if (err) {
							return callback(err);
						}
						var socket_id = result.rows[0].socket_id;
						if (io.sockets.connected[socket_id]) {
							console.log('Notification successful.');
							socket.broadcast.to(socket_id).emit('Error', 'Other player has dropped connection.');
						} else {
							console.log('Socket id: ' + socket_id + ' unavailable.');
						}
					});
				});
			}
		], function (err, result) {
			if (err) {
				console.error('Error', err);
			}
		});

	});
});


http.listen(3001, function () {
	console.log('listening on port 3001');
});