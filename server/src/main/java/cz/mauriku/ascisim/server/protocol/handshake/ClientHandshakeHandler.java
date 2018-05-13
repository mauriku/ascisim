package cz.mauriku.ascisim.server.protocol.handshake;


import cz.mauriku.ascisim.server.protocol.PaxImpProtocolHandler;
import cz.mauriku.ascisim.server.protocol.ByteReplyBuilder;
import cz.mauriku.ascisim.server.protocol.ControlByte;
import cz.mauriku.ascisim.server.protocol.MessageHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandshakeHandler implements MessageHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ClientHandshakeHandler.class);

  @Override
  public ControlByte getHandledByte() {
    return ControlByte.HANDSHAKE;
  }

  @Override
  public void handle(PaxImpProtocolHandler protocol, Channel channel, BinaryWebSocketFrame frame) {
    LOG.debug("Client handshake request received.");

      ByteReplyBuilder reply = new ByteReplyBuilder()
          .begin(64)
          .add(ControlByte.HANDSHAKE)
          .add(true) // server is open for client connections
          .add(140)  // screen width
          .add(45)   // screen height
          .add(14);  // font size

    channel.writeAndFlush(reply.build());
  }
}
