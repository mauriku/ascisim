ASCISIM.Game = function(canvasId) {
  this.canvas = document.querySelector(canvasId);
  this.display = new ASCISIM.Display(this.canvas, {});
  this.console = new ASCISIM.TextConsole(this, {
    x: 0,
    y: 0,
    w: this.display.options.width,
    h: this.display.options.height,
    title: "Connecting to server"
  });
  this.screens = {
    'login': new ASCISIM.LoginScreen(this)
  };

  this.focused = null;

  this.server = {
    'host': 'localhost',
    'port': 7070
  };

  this.handlers = {
    '2': new ASCISIM.HandshakeResponseHandler()
  };
  
  requestAnimationFrame(this.render.bind(this));

  window.addEventListener("keypress", function(event) {
    var ch = String.fromCharCode(event.charCode);
    if (this.focused)
      this.focused.onKeyPress(event, ch);
    game.render();
  }.bind(this));

  window.addEventListener("keydown", function(event) {
    if (this.focused)
      this.focused.onKeyDown(event);
    game.render();
  }.bind(this));
};

ASCISIM.Game.prototype.connect = function () {
  this.console.line("Connecting to server " + this.getServerUrl() + "...");
  this.socket = new WebSocket(this.getServerUrl());
  this.socket.binaryType = 'arraybuffer';

  this.socket.onopen = function (event) {
    this.console.line("Successfully connected to " + this.getServerUrl() + ".");
    this.console.line("Sending initial handshake.");
    this.socket.send(new Uint8Array([0x01]));
    game.render();
  }.bind(this);

  this.socket.onerror = function (event) {
    this.console.line("Error while connecting to server: " + event);
    game.render();
  }.bind(this);

  this.socket.onmessage = function (event) {
    if (event.data instanceof ArrayBuffer) {
      var res = new DataView(event.data);
      var control = res.getInt8();
      this.handlers[control].handle(this, res);
    }
    else
      this.console.line("> Received " + event.data + ".");
    game.render();
  }.bind(this);
};

ASCISIM.Game.prototype.getServerUrl = function () {
  return "ws://" + this.server.host + ":" + this.server.port;
};

ASCISIM.Game.prototype.setFocus = function(screen) {
  if (this.focused)
    this.focused.onFocusLost();

  this.display.clear();
  
  this.focused = screen;
  screen.onFocus();
};

ASCISIM.Game.prototype.render = function() {
  this.console.draw();
  if (this.focused)
    this.focused.draw();
  requestAnimationFrame(this.display.render.bind(this.display));
};


