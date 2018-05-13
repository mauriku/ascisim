ASCISIM.ResponseHandler = function () {
};
ASCISIM.ResponseHandler.prototype.handle = function (game, data) {
};
ASCISIM.ResponseHandler.prototype.getTextFromData = function (start, data) {
  var length = data.getInt32(start);
  var bytes = new Uint8Array(data.buffer, start + 4, length);
  return new TextDecoder().decode(bytes);
};


ASCISIM.HandshakeResponseHandler = function () {
  ASCISIM.ResponseHandler.call(this);
};
ASCISIM.HandshakeResponseHandler.prototype = Object.create(ASCISIM.ResponseHandler.prototype);
ASCISIM.HandshakeResponseHandler.prototype.handle = function (game, data) {
  var open = data.getInt8(1);
  var width = data.getInt32(2);
  var height = data.getInt32(6);
  var fontSize = data.getInt32(10);

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
      token = document.cookie.split("=")[1];
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
  var authenticated = data.getInt8(1);
  var token = this.getTextFromData(2, data);

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