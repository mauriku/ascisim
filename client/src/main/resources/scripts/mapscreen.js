ASCISIM.MapScreen = function (game) {
  ASCISIM.Screen.call(this, game);
};
ASCISIM.MapScreen.prototype = Object.create(ASCISIM.Screen.prototype);

ASCISIM.MapScreen.prototype.onFocus = function() {
  Object.getPrototypeOf(ASCISIM.MapScreen.prototype).onFocus();
};
ASCISIM.MapScreen.prototype.onFocusLost = function() {
  Object.getPrototypeOf(ASCISIM.MapScreen.prototype).onFocusLost();
};

ASCISIM.MapScreen.prototype.onKeyPress = function(event, char) {
};
ASCISIM.MapScreen.prototype.onKeyDown = function(event) {
};
ASCISIM.MapScreen.prototype.draw = function() {
};