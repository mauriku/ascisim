ASCISIM.Display = function (canvas, options) {

  this.context = canvas.getContext("2d");

  var DEFAULT_OPTIONS = {
    width: 80,
    height: 25,
    fontSize: 16,
    spacing: 1,
    border: 0,
    fontFamily: "monospace",
    fontStyle: "",
    fg: "#ccc",
    bg: "#000",
    padding: {
      right: 5,
      bottom: 5,
      left: 5,
      top: 5
    }
  };

  this.data = {};

  for (var opt in options)
    DEFAULT_OPTIONS[opt] = options[opt];

  this.setOptions(DEFAULT_OPTIONS);
};

ASCISIM.Display.prototype.compute = function () {
  var cw = Math.ceil(this.context.measureText("W").width);
  this.spacing = {};
  this.spacing.x = Math.ceil(this.options.spacing * cw);
  this.spacing.y = Math.ceil(this.options.spacing * this.options.fontSize);

  this.context.canvas.width = this.options.width * this.spacing.x + this.options.padding.right + this.options.padding.left;
  this.context.canvas.height = this.options.height * this.spacing.y + this.options.padding.bottom + this.options.padding.top;
};

ASCISIM.Display.prototype.setOptions = function (options) {
  if (!this.options)
    this.options = {};
  
  for (var p in options)
    this.options[p] = options[p];

  var fontDef = (this.options.fontStyle ? this.options.fontStyle + " " : "") + this.options.fontSize + "px " + this.options.fontFamily;
  this.context.font = fontDef;
  this.compute();
  this.context.font = fontDef;
  this.context.textAlign = "center";
  this.context.textBaseline = "middle";
};

ASCISIM.Display.prototype.clear = function (rect) {
  if (!rect)
    this.data = {};
  else {
    for (var x = rect.x; x < rect.x + rect.w; x++) {
      for (var y = rect.y; y < rect.y + rect.h; y++)
        delete this.data[x + "," + y];
    }
  }
};

ASCISIM.Display.prototype.char = function (x, y, ch, fg, bg) {
  if (!fg)
    fg = this.options.fg;
  if (!bg)
    bg = this.options.bg;

  this.data[x + "," + y] = [x, y, ch, fg, bg];
};

ASCISIM.Display.prototype.horizontal = function (x, y, w, ch, fg, bg) {
  if (!fg)
    fg = this.options.fg;
  if (!bg)
    bg = this.options.bg;

  for (var ix = x; ix < x+w; ix++)
    this.data[ix + "," + y] = [ix, y, ch, fg, bg];
};

ASCISIM.Display.prototype.vertical = function (x, y, h, ch, fg, bg) {
  if (!fg)
    fg = this.options.fg;
  if (!bg)
    bg = this.options.bg;

  for (var iy = y; iy < y+h; iy++)
    this.data[x + "," + iy] = [x, iy, ch, fg, bg];
};

ASCISIM.Display.prototype.border = function (x, y, w, h, fg, bg) {
  // top border
  this.char(x, y, "\u250f", fg, bg);
  this.horizontal(x + 1, y, w - 2, "\u2501", fg, bg);
  this.char(x + w - 1, y, "\u2513", fg, bg);
  // left border
  this.vertical(x, y + 1, h - 1, "\u2503", fg, bg);
  // right border
  this.vertical(x + w - 1, y + 1, h - 1, "\u2503", fg, bg);
  // bottom border
  this.char(x, y + h - 1, "\u2517", fg, bg);
  this.horizontal(x + 1, y + h - 1, w - 2, "\u2501", fg, bg);
  this.char(x + w - 1, y + h - 1, "\u251b", fg, bg);
};

ASCISIM.Display.prototype.text = function (x, y, text, fg, bg) {
  for (var i = 0; i < text.length; i++)
    this.char(x + i, y, text[i], fg, bg);
};

ASCISIM.Display.prototype.render = function () {
  this.context.fillStyle = this.options.bg;
  this.context.fillRect(0, 0, this.context.canvas.width, this.context.canvas.height);

  for (var key in this.data) {
    var dt = this.data[key];
    var x = dt[0];
    var y = dt[1];
    var ch = dt[2];
    var fg = dt[3];
    var bg = dt[4];

    if (!ch)
      continue;

    if (bg !== this.options.bg) {
      this.context.fillStyle = bg;
      this.context.fillRect(this.options.padding.left + x * this.spacing.x, this.options.padding.top + y * this.spacing.y, this.spacing.x, this.spacing.y);
    }

    this.context.fillStyle = fg;
    this.context.fillText(ch, this.options.padding.left + (x + 0.5) * this.spacing.x, this.options.padding.top + Math.ceil((y + 0.5) * this.spacing.y));
  }
};