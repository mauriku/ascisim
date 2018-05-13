ASCISIM.MainScreen = function (game) {
  ASCISIM.Screen.call(this, game);
};
ASCISIM.MainScreen.prototype = Object.create(ASCISIM.Screen.prototype);

ASCISIM.MainScreen.prototype.onFocus = function () {
  Object.getPrototypeOf(ASCISIM.MainScreen.prototype).onFocus();
};
ASCISIM.MainScreen.prototype.onFocusLost = function () {
  Object.getPrototypeOf(ASCISIM.MainScreen.prototype).onFocusLost();
};

ASCISIM.MainScreen.prototype.onKeyPress = function (event, char) {
};
ASCISIM.MainScreen.prototype.onKeyDown = function (event) {
};
ASCISIM.MainScreen.prototype.draw = function () {
  this.drawHealthEnergyControls();
  this.drawLevelControls();
  this.drawNames();
  this.drawHealthEnergyBars();
};

ASCISIM.MainScreen.prototype.drawHealthEnergyControls = function () {
  var xb = this.game.console.options.w;
  var yb = this.game.console.options.y;

  this.game.display.text(xb - 50, yb, "HP[   /   ]", "#9c0");
  this.game.display.text(xb - 47, yb, this.format(ASCISIM.PLAYER.properties.hpCur, 3), "#8f0");
  this.game.display.text(xb - 43, yb, this.format(ASCISIM.PLAYER.properties.hpMax, 3), "#8f0");

  this.game.display.text(xb - 38, yb, "EP[   /   ]", "#0ac");
  this.game.display.text(xb - 35, yb, this.format(ASCISIM.PLAYER.properties.epCur, 3), "#0df");
  this.game.display.text(xb - 31, yb, this.format(ASCISIM.PLAYER.properties.epMax, 3), "#0df");
};

ASCISIM.MainScreen.prototype.drawLevelControls = function () {
  var xb = this.game.console.options.w;
  var yb = this.game.console.options.y;

  this.game.display.text(xb - 19, yb, "XP[      /      ]", "#f7f");
  this.game.display.text(xb - 16, yb, this.format(ASCISIM.PLAYER.properties.xpCur, 6), "#faf");
  this.game.display.text(xb - 9, yb, this.format(ASCISIM.PLAYER.properties.xpNxt, 6), "#faf");
  this.game.display.text(xb - 26, yb, "LV[  ]", "#f7f");
  this.game.display.text(xb - 23, yb, this.format(ASCISIM.PLAYER.properties.level, 2), "#faf");
};

ASCISIM.MainScreen.prototype.drawNames = function () {
  var yb = this.game.console.options.y;

  this.game.display.text(18, yb, "[", "#f20");
  this.game.display.text(19, yb, ASCISIM.PLAYER.properties.name, "#f50");
  this.game.display.text(19 + ASCISIM.PLAYER.properties.name.length, yb, "]", "#f20");

  this.game.display.text(18 + ASCISIM.PLAYER.properties.name.length + 3, yb, "[", "#a80");
  this.game.display.text(18 + ASCISIM.PLAYER.properties.name.length + 4, yb, ASCISIM.PLAYER.properties.locationName, "#d90");
  this.game.display.text(18 + ASCISIM.PLAYER.properties.name.length + 4 + ASCISIM.PLAYER.properties.locationName.length, yb, "]", "#a80");
};

ASCISIM.MainScreen.prototype.drawHealthEnergyBars = function () {
  var availableHeight = this.game.display.options.height - this.game.console.options.h;
  var barHeight = availableHeight / 2 - 4;
  var hpBarStart = 0;
  var epBarStart = hpBarStart + barHeight + 4;

  this.game.display.vertical(this.game.display.options.width-1, hpBarStart + 3, barHeight, '\u2593', "#9c0");
  this.game.display.char(this.game.display.options.width - 1, hpBarStart, 'H', "#8f0");
  this.game.display.char(this.game.display.options.width - 1, hpBarStart + 1, 'P', "#8f0");
  this.game.display.char(this.game.display.options.width - 1, hpBarStart + 2, '\u253b', "#8f0");
  this.game.display.char(this.game.display.options.width-1, hpBarStart + 3 + barHeight, '\u2533', "#8f0");

  this.game.display.vertical(this.game.display.options.width-1, epBarStart + 3, barHeight, '\u2593', "#0ac");
  this.game.display.char(this.game.display.options.width - 1, epBarStart, 'E', "#0df");
  this.game.display.char(this.game.display.options.width - 1, epBarStart + 1, 'P', "#0df");
  this.game.display.char(this.game.display.options.width - 1, epBarStart + 2, '\u253b', "#0df");
  this.game.display.char(this.game.display.options.width-1, epBarStart + 3 + barHeight, '\u2533', "#0df");
};

ASCISIM.MainScreen.prototype.format = function (value, places) {
  var val = "" + value;
  var d = places - val.length;
  var res = "";

  for (var i = 0; i < d; i++)
    res += "0";
  res += val;

  return res;
};