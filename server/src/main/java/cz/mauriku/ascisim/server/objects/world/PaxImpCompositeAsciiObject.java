package cz.mauriku.ascisim.server.objects.world;

import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.world.Position;

import java.util.Map;

public class PaxImpCompositeAsciiObject extends PaxImpObject {

  protected Map<Position, PaxImpObject> components;
}
