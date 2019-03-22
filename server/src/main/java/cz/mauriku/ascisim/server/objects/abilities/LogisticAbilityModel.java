package cz.mauriku.ascisim.server.objects.abilities;

public class LogisticAbilityModel implements PaxImpAbilityModel {

  private int base;
  private int target;
  private int midpoint;
  private float coeff;

  public LogisticAbilityModel(int base, int target, int midpoint, float coeff) {
    this.base = base;
    this.target = target;
    this.midpoint = midpoint;
    this.coeff = coeff;
  }

  @Override
  public int valueForLevel(int level) {
    return Math.round(base + target * (1 / (1 + (float) Math.exp(-coeff * (level - midpoint)))));
  }
}
