package cz.mauriku.ascisim.server.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public interface MessageHandler {

  ControlByte getHandledByte();
  void handle(PaxImpProtocolHandler protocol, Channel channel, BinaryWebSocketFrame frame);
}
