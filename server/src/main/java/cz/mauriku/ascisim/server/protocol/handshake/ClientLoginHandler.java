package cz.mauriku.ascisim.server.protocol.handshake;


import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.protocol.ByteReplyBuilder;
import cz.mauriku.ascisim.server.protocol.ControlByte;
import cz.mauriku.ascisim.server.protocol.MessageHandler;
import cz.mauriku.ascisim.server.protocol.PaxImpProtocolHandler;
import cz.mauriku.ascisim.server.services.PlayerAccountService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
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
    LOG.debug("Client login request received.");

    try {
      byte[] buffer = new byte[frame.content().readableBytes()];
      frame.content().readBytes(buffer, 0, frame.content().readableBytes());
      String content = new String(buffer, "UTF-8");
      String method = content.substring(0,1);
      content = content.substring(1);
      String[] components = content.split("/");

      boolean authenticated = false;
      PlayerAccount acc = null;
      if (components.length == 2) {
        if ("P".equals(method))
          authenticated = playerAccountService.authenticateAccountUsingPassword(components[0], components[1]);
        else
          authenticated = playerAccountService.authenticateAccountUsingToken(components[0], components[1]);

        acc = playerAccountService.findAccount(components[0]);
        if (authenticated) {
          acc.setLastLoginDate(Instant.now());
          UUID id = UUID.randomUUID();
          acc.setAuthenticationToken(id.toString());
          playerAccountService.updateAccount(acc);
        }
      }

      channel.writeAndFlush(ByteReplyBuilder.buildLoginReplyMessage(authenticated,
          authenticated ? acc.getAuthenticationToken() : "NA"));

    } catch (UnsupportedEncodingException e) {
      LOG.error("Cannot decode client message.", e);
    }
  }
}
