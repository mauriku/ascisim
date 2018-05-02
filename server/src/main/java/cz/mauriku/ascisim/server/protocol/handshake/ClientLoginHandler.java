package cz.mauriku.ascisim.server.protocol.handshake;


import cz.mauriku.ascisim.server.objects.client.PlayerClient;
import cz.mauriku.ascisim.server.protocol.AscisimServerProtocolHandler;
import cz.mauriku.ascisim.server.protocol.ByteReplyBuilder;
import cz.mauriku.ascisim.server.protocol.ControlByte;
import cz.mauriku.ascisim.server.protocol.MessageHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class ClientLoginHandler implements MessageHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ClientLoginHandler.class);
  private final Random random = new Random();

  @Override
  public void handle(AscisimServerProtocolHandler protocol, Channel channel, BinaryWebSocketFrame frame) {
    LOG.debug("Client [" + AscisimServerProtocolHandler.getClientFromChannel(channel).getId() +
        "] login request received.");

    try {
      byte[] buffer = new byte[frame.content().readableBytes()];
      frame.content().readBytes(buffer, 0, frame.content().readableBytes());
      String content = new String(buffer, "UTF-8");
      String[] components = content.split("/");

      // TODO: authentication

      PlayerClient client = AscisimServerProtocolHandler.getClientFromChannel(channel);
      client.setAuthenticationToken(random.nextInt());

      ByteReplyBuilder reply = new ByteReplyBuilder()
          .begin(64)
          .add(ControlByte.LOGIN)
          .add(true) // client is authenticated
          .add(client.getAuthenticationToken()); // authentication token

      channel.writeAndFlush(reply.build());

    } catch (UnsupportedEncodingException e) {
      LOG.error("TODO", e);
    }
  }
}
