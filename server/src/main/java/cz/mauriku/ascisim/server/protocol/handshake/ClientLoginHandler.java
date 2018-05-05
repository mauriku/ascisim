package cz.mauriku.ascisim.server.protocol.handshake;


import cz.mauriku.ascisim.server.objects.client.PlayerClient;
import cz.mauriku.ascisim.server.protocol.PaxImpProtocolHandler;
import cz.mauriku.ascisim.server.protocol.ByteReplyBuilder;
import cz.mauriku.ascisim.server.protocol.ControlByte;
import cz.mauriku.ascisim.server.protocol.MessageHandler;
import cz.mauriku.ascisim.server.services.PlayerAccountService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class ClientLoginHandler implements MessageHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ClientLoginHandler.class);

  private final PlayerAccountService playerAccountService;

  public ClientLoginHandler(PlayerAccountService playerAccountService) {
    this.playerAccountService = playerAccountService;
  }

  @Override
  public ControlByte getHandledByte() {
    return ControlByte.LOGIN;
  }

  @Override
  public void handle(PaxImpProtocolHandler protocol, Channel channel, BinaryWebSocketFrame frame) {
    LOG.debug("Client [" + PaxImpProtocolHandler.getClientFromChannel(channel).getId() +
        "] login request received.");

    try {
      byte[] buffer = new byte[frame.content().readableBytes()];
      frame.content().readBytes(buffer, 0, frame.content().readableBytes());
      String content = new String(buffer, "UTF-8");
      String[] components = content.split("/");

      boolean authenticated = playerAccountService.authenticateAccount(components[0], components[1]);

      PlayerClient client = PaxImpProtocolHandler.getClientFromChannel(channel);
      client.setAuthenticationToken(authenticated ? UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE : 0L);
      client.setAccount(playerAccountService.findAccount(components[0]));

      ByteReplyBuilder reply = new ByteReplyBuilder()
          .begin(64)
          .add(ControlByte.LOGIN)
          .add(authenticated) // client is authenticated
          .add(client.getAuthenticationToken()); // authentication token

      channel.writeAndFlush(reply.build());

    } catch (UnsupportedEncodingException e) {
      LOG.error("Cannot decode client message.", e);
    }
  }
}
