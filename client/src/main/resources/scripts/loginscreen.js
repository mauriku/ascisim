ASCISIM.LoginScreen = function (game) {
  ASCISIM.Screen.call(this, game);

  this.CONFIG = {
    dialogWidth: 40,
    dialogHeight: 10
  };

  this.username = "";
  this.password = "";

  this.STATES = {
    USERNAME_INPUT: 1,
    PASSWORD_INPUT: 2,
    READY: 3
  };
  this.state = this.STATES.USERNAME_INPUT;
};
ASCISIM.LoginScreen.prototype = Object.create(ASCISIM.Screen.prototype);

ASCISIM.LoginScreen.prototype.onFocus = function () {
  Object.getPrototypeOf(ASCISIM.LoginScreen.prototype).onFocus();
  this.game.console.line("Username:");
};

ASCISIM.LoginScreen.prototype.onKeyPress = function (event, char) {
  this.game.console.onKeyPress(event, char);
};

ASCISIM.LoginScreen.prototype.onKeyDown = function (event) {
  if (event.keyCode === ASCISIM.VK_RETURN) {
    if (this.state === this.STATES.USERNAME_INPUT) {
      this.username = this.game.console.input;
    }
    else if (this.state === this.STATES.PASSWORD_INPUT) {
      this.password = this.game.console.input;
    }
  }

  this.game.console.onKeyDown(event);

  if (event.keyCode === ASCISIM.VK_RETURN) {
    if (this.state === this.STATES.USERNAME_INPUT) {
      this.state = this.STATES.PASSWORD_INPUT;
      this.game.console.mode = this.game.console.MODES.PASSWORD;
      this.game.console.line("Password:");
    }
    else if (this.state === this.STATES.PASSWORD_INPUT) {
      this.state = this.STATES.READY;
      this.game.console.mode = this.game.console.MODES.NORMAL;
      this.game.console.line("Sending login request.");
    }
  }
};

ASCISIM.LoginScreen.prototype.draw = function () {
  var w = this.game.display.options.width;
  var h = this.game.display.options.height - this.game.console.options.h;

  var x = w / 2 - this.CONFIG.dialogWidth / 2;
  var y = h / 2 - this.CONFIG.dialogHeight / 2;

  this.game.display.border(x, y, this.CONFIG.dialogWidth, this.CONFIG.dialogHeight);
  this.game.display.text(x + 2, y + 1, "Username");
  this.game.display.border(x + 2, y + 2, this.CONFIG.dialogWidth - 4, 3);
  this.game.display.text(x + 4, y + 3, this.username);

  this.game.display.text(x + 2, y + 5, "Password");
  this.game.display.border(x + 2, y + 6, this.CONFIG.dialogWidth - 4, 3);
  this.game.display.text(x + 4, y + 7, ASCISIM.GENERATORS.homogeneousText('*', this.password.length));
};