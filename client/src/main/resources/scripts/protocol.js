
ASCISIM.ResponseHandler = function () {
};
ASCISIM.ResponseHandler.prototype.handle = function(game, data) {
};



ASCISIM.HandshakeResponseHandler = function() {
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

    game.console.setOptions({x: 0, y: height-10, h: 10, w: width, title: "Game console"});
    game.setFocus(game.screens['login']);
  }
  else {
    game.console.line("# Server is currently closed. Try later.");
  }
};