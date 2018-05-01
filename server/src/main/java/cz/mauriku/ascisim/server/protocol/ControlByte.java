package cz.mauriku.ascisim.server.protocol;

/*
 * Copyright 2018 by SEFIRA, spol. s r. o.
 * http://www.sefira.cz
 * 
 * cz.mauriku.ascisim.server.protocol.ControlByte
 *
 * Created: 28.04.2018 
 * Author: smolka
 */

/**
 * description
 */
public enum ControlByte {
  CLIENT_HANDSHAKE_REQUEST((byte) 0x01),
  SERVER_HANDSHAKE_RESPONSE((byte) 0x02);

  byte opcode;

  ControlByte(byte opcode) {
    this.opcode = opcode;
  }
}
