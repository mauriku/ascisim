package cz.mauriku.ascisim.server.seed;


import cz.mauriku.ascisim.server.objects.PaxImpMetaObject;
import cz.mauriku.ascisim.server.objects.PaxImpObjectType;
import cz.mauriku.ascisim.server.objects.PaxImpProperty;
import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.objects.client.PlayerAccountLevel;
import cz.mauriku.ascisim.server.objects.world.PaxImpCorpusculeType;

public class ArgumentFragment<T, U> {
  private final T value1;
  private final U value2;

  public ArgumentFragment(T value) {
    this.value1 = value;
    this.value2 = null;
  }

  public ArgumentFragment(T value1, U value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public T getValue1() {
    return value1;
  }

  public U getValue2() {
    return value2;
  }

  public static class AccountLevel extends ArgumentFragment<PlayerAccountLevel, Void> {

    public AccountLevel(PlayerAccountLevel value) {
      super(value);
    }
  }

  public static class AdjustToLevel extends ArgumentFragment<Boolean, Void> {

    public AdjustToLevel(Boolean value) {
      super(value);
    }
  }

  public static class Author extends ArgumentFragment<PlayerAccount, Void> {

    public Author(PlayerAccount value) {
      super(value);
    }
  }

  public static class CharacterLevel extends ArgumentFragment<Integer, Void> {

    public CharacterLevel(Integer value) {
      super(value);
    }
  }

  public static class CorpusculeType extends ArgumentFragment<PaxImpCorpusculeType, Void> {

    public CorpusculeType(PaxImpCorpusculeType value) {
      super(value);
    }
  }

  public static class Email extends ArgumentFragment<String, Void> {

    public Email(String value) {
      super(value);
    }
  }

  public static class Name extends ArgumentFragment<String, Void> {

    public Name(String value) {
      super(value);
    }
  }

  public static class ObjectType extends ArgumentFragment<PaxImpObjectType, Void> {

    public ObjectType(PaxImpObjectType value) {
      super(value);
    }
  }

  public static class Owner extends ArgumentFragment<PlayerAccount, Void> {

    public Owner(PlayerAccount value) {
      super(value);
    }
  }

  public static class Password extends ArgumentFragment<String, Void> {

    public Password(String value) {
      super(value);
    }
  }

  public static class Property extends ArgumentFragment<PaxImpProperty, Object> {

    public Property(PaxImpProperty value1, Object value2) {
      super(value1, value2);
    }
  }

  public static class Template extends ArgumentFragment<PaxImpMetaObject, Void> {

    public Template(PaxImpMetaObject value) {
      super(value);
    }
  }

  public static class Unique extends ArgumentFragment<Boolean, Void> {

    public Unique(Boolean value) {
      super(value);
    }
  }

  public static class Position extends ArgumentFragment<Integer, Integer> {

    public Position(Integer value1, Integer value2) {
      super(value1, value2);
    }
  }
}
