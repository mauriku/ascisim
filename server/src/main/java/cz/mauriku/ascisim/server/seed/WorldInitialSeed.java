package cz.mauriku.ascisim.server.seed;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;

import static cz.mauriku.ascisim.server.objects.client.PlayerAccountLevel.*;
import static cz.mauriku.ascisim.server.objects.PaxImpObjectType.*;
import static cz.mauriku.ascisim.server.objects.PaxImpCharacter.*;
import static cz.mauriku.ascisim.server.objects.world.PaxImpCorpusculeType.*;

import cz.mauriku.ascisim.server.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldInitialSeed extends WorldInitialSeedDsl {

  private void createAccounts() {
    ACC_PES = account(
        email("xsmlk@pm.me"),
        password("pes123"),
        level(IMPERIUS)
    );

    ACC_HAM = account(
        email("michal.hanzal@pm.me"),
        password("ham123"),
        level(IMPERIUS)
    );
  }

  private void createMetaObjects() {

    THE_UNIVERSUM_META = meta(
        author(ACC_PES),
        type(UNIVERSE),
        name("The Universe"),
        unique()
    );

    SUBZERO_META = meta(
        author(ACC_PES),
        type(SECTOR),
        name("Sub-Zero Sector"),
        unique()
    );

    THE_CORE_META = meta(
        author(ACC_PES),
        type(CORPUSCULE),
        type(PLANET),
        name("The Core"),
        unique()
    );

    ARX_META = meta(
        author(ACC_HAM),
        type(CORPUSCULE),
        type(PLANET),
        name("Arx"),
        unique()
    );

    GREAT_TEMPLUM_META = meta(
        author(ACC_PES),
        type(LOCATION),
        name("Great Templum"),
        unique()
    );

    HUMAN_META = meta(
        author(ACC_PES),
        type(CHARACTER),
        name("Common Human Character"),

        property(CHAR, '@'),
        property(MAX_LEVEL, 99)
    );

    ANGEL_META = meta(
        author(ACC_PES),
        type(CHARACTER),
        name("Common Angel Character"),

        property(CHAR, '@'),
        property(MAX_LEVEL, 99)
    );

  }

  private void createObjects() {
    THE_UNIVERSUM = object(
        template(THE_UNIVERSUM_META)
    );

    SUBZERO = object(
        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),

        position(0, 0),

        template(SUBZERO_META)
    );

    THE_CORE = object(
        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),
        property(SECTOR_ID, idOf(SUBZERO)),

        position(0, 0),

        template(THE_CORE_META)
    );

    ARX = object(
        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),
        property(SECTOR_ID, idOf(SUBZERO)),

        position(1, 1),

        template(ARX_META)
    );

    GREAT_TEMPLUM = object(
        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),
        property(SECTOR_ID, idOf(SUBZERO)),
        property(CORPUSCULE_ID, idOf(THE_CORE)),

        position(0, 0),

        template(GREAT_TEMPLUM_META)
    );
  }

  private void createCharacters() {

    MAURIKU = character(
        owner(ACC_PES),
        name("Mauriku"),
        level(1),
        adjustToLevel(),
        active(),

        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),
        property(SECTOR_ID, idOf(SUBZERO)),
        property(CORPUSCULE_ID, idOf(THE_CORE)),
        property(LOCATION_ID, idOf(GREAT_TEMPLUM)),

        position(0,0),

        template(HUMAN_META)
    );

    BARACHIEL = character(
        owner(ACC_HAM),
        name("Barachiel"),
        level(1),
        adjustToLevel(),
        active(),

        property(UNIVERSE_ID, idOf(THE_UNIVERSUM)),
        property(SECTOR_ID, idOf(SUBZERO)),
        property(CORPUSCULE_ID, idOf(THE_CORE)),
        property(LOCATION_ID, idOf(GREAT_TEMPLUM)),

        position(0,0),

        template(ANGEL_META)
    );
  }

  // ---

  private final static Logger LOG = LoggerFactory.getLogger(WorldInitialSeed.class);

  public WorldInitialSeed(PlayerAccountService playerAccountService, MetaObjectService metaObjectService,
                          CharacterService characterService, ObjectService objectService,
                          PositioningService positioningService) {
    super(playerAccountService, metaObjectService, characterService, objectService, positioningService);
  }

  private PlayerAccount ACC_PES;
  private PlayerAccount ACC_HAM;

  private PaxImpMetaObject THE_UNIVERSUM_META;
  private PaxImpMetaObject SUBZERO_META;
  private PaxImpMetaObject THE_CORE_META;
  private PaxImpMetaObject ARX_META;
  private PaxImpMetaObject GREAT_TEMPLUM_META;

  private PaxImpMetaObject HUMAN_META;
  private PaxImpMetaObject ANGEL_META;

  private PaxImpCharacter MAURIKU;
  private PaxImpCharacter BARACHIEL;

  private PaxImpObject THE_UNIVERSUM;
  private PaxImpObject SUBZERO;
  private PaxImpObject THE_CORE;
  private PaxImpObject ARX;
  private PaxImpObject GREAT_TEMPLUM;

  public void initializeWorld() {

    if (playerAccountService.findAccount("xsmlk@pm.me") != null) {
      LOG.info("Not seeding world, already seeded.");
      return;
    }

    createAccounts();
    createMetaObjects();
    createObjects();
    createCharacters();
  }
}
