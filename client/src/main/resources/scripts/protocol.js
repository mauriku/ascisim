ASCISIM.ResponseHandler = function () {
};

ASCISIM.ResponseHandler.prototype.handle = function (game, data) {
};


ASCISIM.HandshakeResponseHandler = function () {
  ASCISIM.ResponseHandler.call(this);
};
ASCISIM.HandshakeResponseHandler.prototype = Object.create(ASCISIM.ResponseHandler.prototype);
ASCISIM.HandshakeResponseHandler.prototype.handle = function (game, data) {
  var reader = new ASCISIM.DataReader(data, 1);

  var open = reader.getByte();
  var width = reader.getInteger();
  var height = reader.getInteger();
  var fontSize = reader.getInteger();

  if (open === 1) {
    game.console.line("# Received initial configuration.");
    game.display.setOptions({
      width: width,
      height: height,
      fontSize: fontSize
    });

    game.console.setOptions({x: 0, y: height - 10, h: 10, w: width, title: "Game console"});

    if (document.cookie.split(';').filter(function (item) {
          return item.indexOf('paxtoken=') >= 0
        }).length) {
      game.display.clear();
      var token = document.cookie.split("=")[1];
      game.sendBinary(0x02, 'T' + token);
      game.setFocus(game.screens['login']);
      game.focused.username = token.split("/")[0];
      game.console.line("> Logging in using stored token.");
    }
    else
      game.setFocus(game.screens['login']);
  }
  else {
    game.console.line("# Server is currently closed. Try later.");
  }
};

ASCISIM.LoginResponseHandler = function () {
  ASCISIM.ResponseHandler.call(this);
};
ASCISIM.LoginResponseHandler.prototype = Object.create(ASCISIM.ResponseHandler.prototype);
ASCISIM.LoginResponseHandler.prototype.handle = function (game, data) {
  var reader = new ASCISIM.DataReader(data, 1);

  var authenticated = reader.getByte();
  var token = reader.getString();

  if (authenticated === 1) {
    document.cookie = "paxtoken=" + game.focused.username + "/" + token;

    game.console.line("# Logged in. Ready player one.");
    game.setFocus(game.screens['main']);
  }
  else {
    game.console.line("# Unsuccessful login. Try again.");
    game.focused.tryAgain();
  }
};

ASCISIM.CharacterUpdateHandler = function () {
  ASCISIM.ResponseHandler.call(this);
};
ASCISIM.CharacterUpdateHandler.prototype = Object.create(ASCISIM.ResponseHandler.prototype);
ASCISIM.CharacterUpdateHandler.prototype.handle = function (game, data) {

  ASCISIM.PLAYER.properties.char = this.getTextFromData(1, data);
};


ASCISIM.DataReader = function(data, initialOffset) {
  this.data = data;
  this.offset = initialOffset;
};

ASCISIM.DataReader.prototype.getString = function () {
  var length = this.getInteger();
  var bytes = new Uint8Array(this.data.buffer, this.offset, length);
  this.offset += length;
  return new TextDecoder().decode(bytes);
};

ASCISIM.DataReader.prototype.getInteger = function () {
  var int = this.data.getInt32(this.offset);
  this.offset += 4;
  return int;
};

ASCISIM.DataReader.prototype.getByte = function () {
  var byte = this.data.getInt8(this.offset);
  this.offset += 1;
  return byte;
};