var ASCISIM = {
  VK_BACK_SPACE: 8,
  VK_RETURN: 13
};

ASCISIM.GENERATORS = {};

ASCISIM.GENERATORS.homogeneousText = function (char, length) {
  var text = "";
  for (var i = 0; i < length; i++)
    text += char;

  return text;
};