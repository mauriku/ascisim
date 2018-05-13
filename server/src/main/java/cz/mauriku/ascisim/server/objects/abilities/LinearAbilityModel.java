package cz.mauriku.ascisim.server.objects.abilities;

public class LinearAbilityModel implements PaxImpAbilityModel {

  private int min;
  private float coeff;

  public LinearAbilityModel(int min, float coeff) {
    this.min = min;
    this.coeff = coeff;
  }

  @Override
  public int abilityValue(int level) {
    return Math.round(min + coeff * level);
  }
}
