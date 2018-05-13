package cz.mauriku.ascisim.server.protocol;

public enum ControlByte {
  HANDSHAKE((byte) 0x01),
  LOGIN((byte) 0x02),
  TOKEN_LOGIN((byte) 0x03),
  CHARACTER_UPDATE ((byte) 0x04);

  byte opcode;

  ControlByte(byte opcode) {
    this.opcode = opcode;
  }
}
