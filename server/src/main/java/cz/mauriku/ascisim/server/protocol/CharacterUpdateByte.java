package cz.mauriku.ascisim.server.protocol;

public enum CharacterUpdateByte {

  UI((byte) 0x01),
  POSITION((byte) 0x02);

  byte opcode;

  CharacterUpdateByte(byte opcode) {
    this.opcode = opcode;
  }
}
