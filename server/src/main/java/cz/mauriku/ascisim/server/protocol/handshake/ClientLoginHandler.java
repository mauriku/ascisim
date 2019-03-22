package cz.mauriku.ascisim.server.protocol.handshake;


import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.protocol.*;
import cz.mauriku.ascisim.server.services.CharacterService;
import cz.mauriku.ascisim.server.services.ObjectService;
import cz.mauriku.ascisim.server.services.PlayerAccountService;
import cz.mauriku.ascisim.server.services.PositioningService;
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
  private final CharacterService characterService;
  private final PositioningService positioningService;
  private final ObjectService objectService;

  public ClientLoginHandler(PlayerAccountService playerAccountService, CharacterService characterService,
                            PositioningService positioningService, ObjectService objectService) {
    this.playerAccountService = playerAccountService;
    this.characterService = characterService;
    this.positioningService = positioningService;
    this.objectService = objectService;
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
      String method = content.substring(0, 1);
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

      if (authenticated) {
        PaxImpCharacter chr = characterService.findById(acc.getActiveCharacterId());
        channel.writeAndFlush(ByteReplyBuilder.buildCharacterUpdateMessage(
            CharacterUpdateByte.UI, chr, positioningService, objectService));
      }

    } catch (UnsupportedEncodingException e) {
      LOG.error("Cannot decode client message.", e);
    }
  }
}
