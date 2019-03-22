var ASCISIM = {
  VK_BACK_SPACE: 8,
  VK_RETURN: 13
};

ASCISIM.PLAYER = {};

ASCISIM.PLAYER.properties = {
  char: '@',
  hpCur: 0,
  hpMax: 999,
  epCur: 0,
  epMax: 999,
  level: 1,
  xpCur: 0,
  xpNxt: 999999,
  name: 'Player One',
  locationName: 'Unknown Location'
};

ASCISIM.GENERATORS = {};

ASCISIM.GENERATORS.homogeneousText = function (char, length) {
  var text = "";
  for (var i = 0; i < length; i++)
    text += char;

  return text;
};