package cz.mauriku.ascisim.server;

import cz.mauriku.ascisim.server.protocol.PaxImpProtocolHandler;
import cz.mauriku.ascisim.server.protocol.handshake.ClientHandshakeHandler;
import cz.mauriku.ascisim.server.protocol.handshake.ClientLoginHandler;
import cz.mauriku.ascisim.server.seed.WorldInitialSeed;
import cz.mauriku.ascisim.server.services.*;

public class PaxImpServerMain {

  public static void main(String[] args) throws InterruptedException {

    ServerOptions options = ServerOptions.fromArguments(args);

    PaxImpServer server = new PaxImpServer(options);
    server.initIgnite();

    PlayerAccountService playerAccountService = new PlayerAccountService(server.getIgnite());
    MetaObjectService metaObjectService = new MetaObjectService(server.getIgnite());
    CharacterService characterService = new CharacterService(server.getIgnite(), metaObjectService);
    ObjectService objectService = new ObjectService(server.getIgnite(), metaObjectService);
    PositioningService positioningService = new PositioningService(server.getIgnite());

    WorldInitialSeed worldSeed = new WorldInitialSeed(
        playerAccountService,
        metaObjectService,
        characterService,
        objectService,
        positioningService
    );
    worldSeed.initializeWorld();

    server.initWebSocketServer(
        new PaxImpProtocolHandler(
            new ClientHandshakeHandler(),
            new ClientLoginHandler(
                playerAccountService,
                characterService,
                positioningService,
                objectService
            )
        )
    );
  }
}
