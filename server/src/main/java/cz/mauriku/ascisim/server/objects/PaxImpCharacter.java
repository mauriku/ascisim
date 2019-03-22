package cz.mauriku.ascisim.server.objects;

import cz.mauriku.ascisim.server.objects.abilities.LogisticAbilityModel;
import cz.mauriku.ascisim.server.objects.abilities.PaxImpAbilityModel;

public class PaxImpCharacter extends PaxImpObject {

  public static final PaxImpProperty<Integer> CURRENT_HP = new PaxImpProperty<>("currentHp", false, Integer.class);
  public static final PaxImpProperty<Integer> CURRENT_EP = new PaxImpProperty<>("currentEp", false, Integer.class);
  public static final PaxImpProperty<Integer> MAXIMUM_HP = new PaxImpProperty<>("maximumHp", false, Integer.class);
  public static final PaxImpProperty<Integer> MAXIMUM_EP = new PaxImpProperty<>("maximumEp", false, Integer.class);
  
  public static final PaxImpProperty<Integer> MAX_LEVEL = new PaxImpProperty<>("maxLevel", true, Integer.class);

  public static final PaxImpProperty<Integer> CURRENT_XP = new PaxImpProperty<Integer>("currentXp", false, Integer.class);
  public static final PaxImpProperty<Integer> ADVANCE_XP = new PaxImpProperty<Integer>("advanceXp", false, Integer.class);

  private final PaxImpAbilityModel hpModel = new LogisticAbilityModel(8, 800, 60, -0.1f);
  private final PaxImpAbilityModel epModel = new LogisticAbilityModel(-5, 800, 60, -0.08f);

  public PaxImpCharacter() {
    super();
  }

  public PaxImpCharacter(PaxImpMetaObject metaObject) {
    super(metaObject);
  }

  public int getCurrentHitPoints() {
    return getObjectProperty(CURRENT_HP);
  }

  public int getMaximumHitPoints() {
    return getObjectProperty(MAXIMUM_HP);
  }

  public int getCurrentEnergyPoints() {
    return getObjectProperty(CURRENT_EP);
  }

  public int getMaximumEnergyPoints() {
    return getObjectProperty(MAXIMUM_EP);
  }

  public void setMaximumHitPoints(int hp) {
    setObjectProperty(MAXIMUM_HP, hp);
  }

  public void setMaximumEnergyPoints(int ep) {
    setObjectProperty(MAXIMUM_EP, ep);
  }

  public void setLevel(int level) {
    setLevel(level, false);
  }
  
  public void setLevel(int level, boolean adjustToLevel) {
    if (level < 1 || level > getObjectProperty(MAX_LEVEL))
      throw new IllegalArgumentException("Character level out of bound <1," + getObjectProperty(MAX_LEVEL) + ">.");

    setObjectProperty(PaxImpObject.LEVEL, level);

    if (adjustToLevel) {
      setMaximumHitPoints(hpModel.valueForLevel(level));
      setMaximumEnergyPoints(epModel.valueForLevel(level));
      adjustHpEpToMaximum();
    }
  }

  public int getLevel() {
    return getObjectProperty(PaxImpObject.LEVEL);
  }

  public int getCurrentXp() {
    return getObjectProperty(CURRENT_XP);
  }

  public int getAdvanceXp() {
    return getObjectProperty(ADVANCE_XP);
  }

  public void adjustHpEpToMaximum() {
    setObjectProperty(CURRENT_HP, getMaximumHitPoints());
    setObjectProperty(CURRENT_EP, getMaximumEnergyPoints());
  }

}
