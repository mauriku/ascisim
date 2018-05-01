package cz.mauriku.ascisim.server.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public interface MessageHandler {

  void handle(AscisimServerProtocolHandler protocol, Channel channel, BinaryWebSocketFrame frame);
}
