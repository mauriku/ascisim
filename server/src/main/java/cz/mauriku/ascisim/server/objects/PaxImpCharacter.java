package cz.mauriku.ascisim.server.objects;

public class PaxImpCharacter extends PaxImpObject {

  public static final PaxImpProperty<Integer> CURRENT_HP = new PaxImpProperty<>("currentHp", false, Integer.class);
  public static final PaxImpProperty<Integer> CURRENT_EP = new PaxImpProperty<>("currentEp", false, Integer.class);
  public static final PaxImpProperty<Integer> MAXIMUM_HP = new PaxImpProperty<>("maximumHp", false, Integer.class);
  public static final PaxImpProperty<Integer> MAXIMUM_EP = new PaxImpProperty<>("maximumEp", false, Integer.class);

  public static final PaxImpProperty<Integer> STRENGTH = new PaxImpProperty<Integer>("strength", false, Integer.class);
  public static final PaxImpProperty<Integer> DEXTERITY = new PaxImpProperty<Integer>("dexterity", false, Integer.class);
  public static final PaxImpProperty<Integer> CONSTITUTION = new PaxImpProperty<Integer>("constitution", false, Integer.class);
  public static final PaxImpProperty<Integer> INTELLIGENCE = new PaxImpProperty<Integer>("intelligence", false, Integer.class);
  public static final PaxImpProperty<Integer> MAX_LEVEL = new PaxImpProperty<>("maxLevel", true, Integer.class);

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
  
  public void setLevel(int level, boolean modifyCharAbilities) {
    if (level < 1 || level > getObjectProperty(MAX_LEVEL))
      throw new IllegalArgumentException("Character level out of bound <1," + getObjectProperty(MAX_LEVEL) + ">.");

    setObjectProperty(PaxImpObject.LEVEL, level);

    if (modifyCharAbilities) {
      //setMaximumHitPoints(hpModel.getAbilityValue(level));
      //setMaximumEnergyPoints(epModel.getAbilityValue(level));
    }
  }

  public int getLevel() {
    return getObjectProperty(PaxImpObject.LEVEL);
  }

  public void setStrength(int strength) {
    setAbility(STRENGTH, strength);
  }

  public int getStrength() {
    return getObjectProperty(STRENGTH);
  }

  public void setDexterity(int dexterity) {
    setAbility(DEXTERITY, dexterity);
  }

  public int getDexterity() {
    return getObjectProperty(DEXTERITY);
  }

  public void setConstitution(int constitution) {
    setAbility(CONSTITUTION, constitution);
  }

  public int getIntelligence() {
    return getObjectProperty(INTELLIGENCE);
  }

  public void setIntelligence(int constitution) {
    setAbility(INTELLIGENCE, constitution);
  }

  public int getConstitution() {
    return getObjectProperty(CONSTITUTION);
  }

  private void setAbility(PaxImpProperty<Integer> ability, int value) {
    if (value < 1 || value > 99)
      throw new IllegalArgumentException("Character " + ability.getName() + " out of bound <1,99>.");

    setObjectProperty(ability, value);
  }

}
