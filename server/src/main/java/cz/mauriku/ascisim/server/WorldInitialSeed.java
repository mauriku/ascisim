package cz.mauriku.ascisim.server;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.PaxImpObjectType;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.objects.client.PlayerAccountLevel;
import cz.mauriku.ascisim.server.objects.world.Position;
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
        "xsmlk@pm.me", "xsmlk123", PlayerAccountLevel.IMPERIUS);
    PlayerAccount michhAcc = playerAccountService.createAccount(
        "michal.hanzal@pm.me", "michal123", PlayerAccountLevel.IMPERIUS);

    // create common character meta
    PaxImpMetaObject commonCharacterMeta =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.CHARACTER, "Common character");
    commonCharacterMeta.setMetaObjectProperty(PaxImpCharacter.MAX_LEVEL, 99);
    commonCharacterMeta.setMetaObjectProperty(PaxImpObject.CHAR, '@');

    // create prime universe meta
    PaxImpMetaObject primeUniverseMeta =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.UNIVERSE, "Universum Prime");
    primeUniverseMeta.setMetaObjectProperty(PaxImpObject.DESCRIPTION, "The core universe of the PI multiverse.");
    primeUniverseMeta.setUnique(true);
    metaObjectService.updateMetaObject(xsmlkAcc, primeUniverseMeta);

    // create zero sector meta
    PaxImpMetaObject zeroSectorMeta =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.SECTOR, "Zero Sector");
    zeroSectorMeta.setMetaObjectProperty(PaxImpObject.DESCRIPTION, "The legendary founding sector.");
    zeroSectorMeta.setMetaObjectProperty(PaxImpObject.UNIVERSE_ID, primeUniverseMeta.getId());
    zeroSectorMeta.setUnique(true);
    metaObjectService.updateMetaObject(xsmlkAcc, zeroSectorMeta);

    // create core planet meta
    PaxImpMetaObject corePlanet =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.CORPUSCULE, "The Core");
    corePlanet.setMetaObjectProperty(PaxImpObject.DESCRIPTION, "The center of the universe.");
    corePlanet.setMetaObjectProperty(PaxImpObject.SECTOR_ID, zeroSectorMeta.getId());
    corePlanet.setMetaObjectProperty(PaxImpObject.POSITION, Position.of(0L, 0L));
    corePlanet.setUnique(true);
    metaObjectService.updateMetaObject(xsmlkAcc, corePlanet);

    // create palace of the creation meta
    PaxImpMetaObject imperusPalace =
        metaObjectService.createMetaObject(xsmlkAcc, PaxImpObjectType.LOCATION, "Imperius Palace");
    imperusPalace.setMetaObjectProperty(PaxImpObject.DESCRIPTION, "Temple of the creators.");
    imperusPalace.setMetaObjectProperty(PaxImpObject.CORPUSCULE_ID, corePlanet.getId());
    imperusPalace.setUnique(true);
    imperusPalace.setMetaObjectProperty(PaxImpObject.POSITION, Position.of(0L, 0L));

    // create players characters
    PaxImpCharacter mauriku = commonCharacterMeta.createNewObject(PaxImpCharacter.class);
    mauriku.setLevel(1, true);

    PaxImpCharacter barachiel = commonCharacterMeta.createNewObject(PaxImpCharacter.class);
    barachiel.setLevel(1, true);

  }
}
