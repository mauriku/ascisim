package cz.mauriku.ascisim.server.seed;

import cz.mauriku.ascisim.server.objects.*;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.objects.client.PlayerAccountLevel;
import cz.mauriku.ascisim.server.objects.world.PaxImpCorpusculeType;
import cz.mauriku.ascisim.server.services.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WorldInitialSeedDsl {

  protected final PlayerAccountService playerAccountService;
  protected final MetaObjectService metaObjectService;
  protected final CharacterService characterService;
  protected final ObjectService objectService;
  protected final PositioningService positioningService;

  protected WorldInitialSeedDsl(PlayerAccountService playerAccountService, MetaObjectService metaObjectService,
                                CharacterService characterService, ObjectService objectService,
                                PositioningService positioningService) {
    this.playerAccountService = playerAccountService;
    this.metaObjectService = metaObjectService;
    this.characterService = characterService;
    this.objectService = objectService;
    this.positioningService = positioningService;
  }

  protected PlayerAccount account(ArgumentFragment... args) {
    String email = requireOne(ArgumentFragment.Email.class, args).getValue1();
    String password = requireOne(ArgumentFragment.Password.class, args).getValue1();
    PlayerAccountLevel level = requireOne(ArgumentFragment.AccountLevel.class, args).getValue1();

    return playerAccountService.createAccount(email, password, level);
  }

  protected PaxImpMetaObject meta(ArgumentFragment... args) {

    PlayerAccount author = requireOne(ArgumentFragment.Author.class, args).getValue1();
    PaxImpObjectType type = requireOne(ArgumentFragment.ObjectType.class, args).getValue1();
    String name = requireOne(ArgumentFragment.Name.class, args).getValue1();
    boolean unique = optionalOne(ArgumentFragment.Unique.class, false, args).getValue1();
    List<? extends ArgumentFragment> properties = optionalMany(ArgumentFragment.Property.class, args);

    PaxImpMetaObject metaObject =
        metaObjectService.createMetaObject(author, type, name);
    metaObject.setUnique(unique);


    for (ArgumentFragment arg : properties) {
      ArgumentFragment.Property prop = (ArgumentFragment.Property) arg;
      metaObject.setMetaObjectProperty(prop.getValue1(), prop.getValue2());
    }

    metaObjectService.updateMetaObject(author, metaObject);
    return metaObject;
  }

  protected PaxImpCharacter character(ArgumentFragment... args) {
    PlayerAccount owner = requireOne(ArgumentFragment.Owner.class, args).getValue1();
    String name = requireOne(ArgumentFragment.Name.class, args).getValue1();
    int level = optionalOne(ArgumentFragment.CharacterLevel.class, 1, args).getValue1();
    PaxImpMetaObject metaObject = requireOne(ArgumentFragment.Template.class, args).getValue1();
    boolean adjustToLevel = optionalOne(ArgumentFragment.AdjustToLevel.class, false, args).getValue1();
    List<? extends ArgumentFragment> properties = optionalMany(ArgumentFragment.Property.class, args);
    ArgumentFragment.Position position = (ArgumentFragment.Position)
        optionalOne(ArgumentFragment.Position.class, args);

    PaxImpCharacter character = metaObject.createNewObject(characterService);
    character.setLevel(level, adjustToLevel);
    character.setObjectProperty(PaxImpObject.NAME, name);

    for (ArgumentFragment arg : properties) {
      ArgumentFragment.Property prop = (ArgumentFragment.Property) arg;
      character.setObjectProperty(prop.getValue1(), prop.getValue2());
    }

    characterService.storeNewCharacter(character);

    owner.getCharacterIds().add(character.getId());
    playerAccountService.updateAccount(owner);

    if (position != null)
      positioningService.updatePosition(character, position.getValue1(), position.getValue2());

    return character;
  }

  protected PaxImpObject object(ArgumentFragment... args) {
    PaxImpMetaObject metaObject = requireOne(ArgumentFragment.Template.class, args).getValue1();

    List<? extends ArgumentFragment> properties = optionalMany(ArgumentFragment.Property.class, args);
    ArgumentFragment.Position position = (ArgumentFragment.Position)
        optionalOne(ArgumentFragment.Position.class, args);

    PaxImpObject object = metaObject.createNewObject(objectService);
    for (ArgumentFragment arg : properties) {
      ArgumentFragment.Property prop = (ArgumentFragment.Property) arg;
      object.setObjectProperty(prop.getValue1(), prop.getValue2());
    }
    objectService.storeNewObject(object);

    if (position != null)
      positioningService.updatePosition(object, position.getValue1(), position.getValue2());

    return object;
  }

  protected ArgumentFragment.Email email(String email) {
    return new ArgumentFragment.Email(email);
  }

  protected ArgumentFragment.Password password(String password) {
    return new ArgumentFragment.Password(password);
  }

  protected ArgumentFragment.AccountLevel level(PlayerAccountLevel level) {
    return new ArgumentFragment.AccountLevel(level);
  }

  protected ArgumentFragment.Author author(PlayerAccount author) {
    return new ArgumentFragment.Author(author);
  }

  protected ArgumentFragment.Owner owner(PlayerAccount owner) {
    return new ArgumentFragment.Owner(owner);
  }

  protected ArgumentFragment.Template template(PaxImpMetaObject metaObject) {
    return new ArgumentFragment.Template(metaObject);
  }

  protected ArgumentFragment.ObjectType type(PaxImpObjectType type) {
    return new ArgumentFragment.ObjectType(type);
  }

  protected ArgumentFragment.CorpusculeType type(PaxImpCorpusculeType type) {
    return new ArgumentFragment.CorpusculeType(type);
  }

  protected ArgumentFragment.Name name(String name) {
    return new ArgumentFragment.Name(name);
  }

  protected ArgumentFragment.Unique unique() {
    return new ArgumentFragment.Unique(true);
  }

  protected ArgumentFragment.AdjustToLevel adjustToLevel() {
    return new ArgumentFragment.AdjustToLevel(true);
  }

  protected ArgumentFragment.CharacterLevel level(int level) {
    return new ArgumentFragment.CharacterLevel(level);
  }

  protected <T> ArgumentFragment.Property property(PaxImpProperty<T> property, T value) {
    return new ArgumentFragment.Property(property, value);
  }

  protected ArgumentFragment.Position position(int x, int y) {
    return new ArgumentFragment.Position(x, y);
  }

  protected String idOf(PaxImpObject object) {
    return object.getId();
  }

  protected String idOf(PaxImpMetaObject metaObject) {
    return metaObject.getId();
  }

  private <T, U> ArgumentFragment<T, U> requireOne(Class<? extends ArgumentFragment<T, U>> cls, ArgumentFragment[] args) {
    for (ArgumentFragment arg : args)
      if (arg.getClass().isAssignableFrom(cls))
        return arg;

    throw new IllegalArgumentException("Cannot find argument of type " + cls + ".");
  }

  private <T, U> List<? extends ArgumentFragment<T, U>> requireMany(Class<? extends ArgumentFragment<T, U>> cls, ArgumentFragment[] args) {
    List<? extends ArgumentFragment<T, U>> result = optionalMany(cls, args);

    if (result.size() == 0)
      throw new IllegalArgumentException("Cannot find arguments of type " + cls + ".");

    return result;
  }

  private <T, U> ArgumentFragment<T, U> optionalOne(Class<? extends ArgumentFragment<T, U>> cls, T default1, ArgumentFragment[] args) {
    for (ArgumentFragment arg : args)
      if (arg.getClass().isAssignableFrom(cls))
        return arg;

    try {
      return cls.getConstructor(default1.getClass()).newInstance(default1);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new IllegalStateException("Cannot create instance of default value.", e);
    }
  }

  private <T, U> ArgumentFragment<T, U> optionalOne(Class<? extends ArgumentFragment<T, U>> cls, ArgumentFragment[] args) {
    for (ArgumentFragment arg : args)
      if (arg.getClass().isAssignableFrom(cls))
        return arg;

    return null;
  }

  private <T, U> List<? extends ArgumentFragment<T, U>> optionalMany(Class<? extends ArgumentFragment<T, U>> cls, ArgumentFragment[] args) {
    List<ArgumentFragment<T, U>> result = new ArrayList<>();

    for (ArgumentFragment arg : args)
      if (arg.getClass().isAssignableFrom(cls))
        result.add(arg);

    return result;
  }
}
