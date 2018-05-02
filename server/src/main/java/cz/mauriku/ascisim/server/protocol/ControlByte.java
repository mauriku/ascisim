package cz.mauriku.ascisim.server.protocol;

public enum ControlByte {
  HANDSHAKE((byte) 0x01),
  LOGIN((byte) 0x02);

  byte opcode;

  ControlByte(byte opcode) {
    this.opcode = opcode;
  }
}
