package cz.mauriku.ascisim.server;

import cz.mauriku.ascisim.server.protocol.PaxImpProtocolHandler;
import cz.mauriku.ascisim.server.protocol.handshake.ClientHandshakeHandler;
import cz.mauriku.ascisim.server.protocol.handshake.ClientLoginHandler;
import cz.mauriku.ascisim.server.services.MetaObjectService;
import cz.mauriku.ascisim.server.services.PlayerAccountService;

public class PaxImpServerMain {

  public static void main(String[] args) throws InterruptedException {

    ServerOptions options = ServerOptions.fromArguments(args);

    PaxImpServer server = new PaxImpServer(options);
    server.initIgnite();

    PlayerAccountService playerAccountService = new PlayerAccountService(server.getIgnite());
    MetaObjectService metaObjectService = new MetaObjectService(server.getIgnite());

    WorldInitialSeed worldSeed = new WorldInitialSeed(
        playerAccountService,
        metaObjectService
    );

    //worldSeed.initWorld();

    server.initWebSocketServer(
        new PaxImpProtocolHandler(
            new ClientHandshakeHandler(),
            new ClientLoginHandler(playerAccountService)
        )
    );
  }
}
