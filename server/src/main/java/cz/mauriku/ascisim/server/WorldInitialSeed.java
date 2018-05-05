package cz.mauriku.ascisim.server;

import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObjectType;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.objects.client.PlayerAccountLevel;
import cz.mauriku.ascisim.server.services.MetaObjectService;
import cz.mauriku.ascisim.server.services.PlayerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldInitialSeed {

  private final static Logger LOG = LoggerFactory.getLogger(WorldInitialSeed.class);

  private PlayerAccountService playerAccountService;
  private MetaObjectService metaObjectService;

  public WorldInitialSeed(PlayerAccountService playerAccountService, MetaObjectService metaObjectService) {
    this.playerAccountService = playerAccountService;
    this.metaObjectService = metaObjectService;
  }

  public void initWorld() {

    // create initial administrators
    PlayerAccount xsmlkAcc = playerAccountService.createAccount(
        "xsmlk@pm.me", "xsmlk123", PlayerAccountLevel.IMPERUS);
    PlayerAccount michhAcc = playerAccountService.createAccount(
        "michal.hanzal@pm.me", "michal123", PlayerAccountLevel.IMPERUS);

    // create standard universe
    PaxImpMetaObject standardUniverse =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.UNIVERSE, "Standard Universe");

  }
}
