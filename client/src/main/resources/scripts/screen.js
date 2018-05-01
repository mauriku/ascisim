ASCISIM.Screen = function (game) {
  this.game = game;
  this.focus = false;
};
ASCISIM.Screen.prototype.onFocus = function() {
  this.focus = true;
};
ASCISIM.Screen.prototype.onFocusLost = function() {
  this.focus = false;
};
ASCISIM.Screen.prototype.onKeyPress = function(event, char) {
};
ASCISIM.Screen.prototype.onKeyDown = function(event) {
};
ASCISIM.Screen.prototype.draw = function() {
};
























