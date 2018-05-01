ASCISIM.TextConsole = function (game, options) {
  ASCISIM.Screen.call(this, game);

  var DEFAULT_OPTIONS = {
    x: 0,
    y: 0,
    w: 80,
    h: 25,
    fg: "#ccc",
    bg: "#000",
    title: "console"
  };

  for (var opt in options)
    DEFAULT_OPTIONS[opt] = options[opt];
  this.setOptions(DEFAULT_OPTIONS);

  this.lines = [];
  this.input = "";

  this.MODES = {
    NORMAL: 1,
    PASSWORD: 2
  };

  this.mode = this.MODES.NORMAL;
};
ASCISIM.TextConsole.prototype = Object.create(ASCISIM.Screen.prototype);

ASCISIM.TextConsole.prototype.setOptions = function (options) {
  if (!this.options)
    this.options = {};

  for (var p in options)
    this.options[p] = options[p];
};

ASCISIM.TextConsole.prototype.line = function (line) {
  this.lines.push(line);
};

ASCISIM.TextConsole.prototype.draw = function () {
  var x = this.options.x;
  var y = this.options.y;
  var w = this.options.w;
  var h = this.options.h;

  this.game.display.clear({x: x, y: y, w: w, h: h});

  // render text
  // TODO: truncate too lengthy text
  var maxLines = Math.min(this.lines.length, h - 3);
  for (var i = 0; i < maxLines; i++)
    this.game.display.text(x + 1, y + h - i - 3, this.lines[this.lines.length - i - 1]);

  if (this.mode === this.MODES.NORMAL)
    this.game.display.text(x + 1, y + h - 2, ">" + this.input + "_");
  else
    this.game.display.text(x + 1, y + h - 2, ">" + ASCISIM.GENERATORS.homogeneousText('*', this.input.length) + "_");


  this.game.display.border(x, y, w, h);
  this.game.display.text(x + 3, y, "\u252b" + this.options.title + "\u2523");
};

ASCISIM.TextConsole.prototype.onKeyPress = function(event, char) {
  if (event.keyCode === ASCISIM.VK_RETURN)
    return;
  
  this.input += char;
};

ASCISIM.TextConsole.prototype.onKeyDown = function(event) {
  
  if (event.keyCode === ASCISIM.VK_BACK_SPACE) {
    if (this.input.length > 0)
      this.input = this.input.substring(0, this.input.length-1);
  }
  else if (event.keyCode === ASCISIM.VK_RETURN) {
    if (this.input.length > 0) {
      if (this.mode === this.MODES.NORMAL)
        this.line(this.input);
      else
        this.line(ASCISIM.GENERATORS.homogeneousText('*', this.input.length));
      this.input = "";
    }
  }
};