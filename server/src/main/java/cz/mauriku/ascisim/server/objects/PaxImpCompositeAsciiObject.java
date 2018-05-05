package cz.mauriku.ascisim.server.objects;

import cz.mauriku.ascisim.server.objects.world.GridPosition;

import java.util.Map;

public class PaxImpCompositeAsciiObject extends PaxImpObject {

  protected Map<GridPosition, PaxImpObject> components;
}
