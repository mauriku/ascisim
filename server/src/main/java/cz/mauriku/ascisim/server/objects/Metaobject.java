package cz.mauriku.ascisim.server.objects;

import java.util.List;

public interface Metaobject {

  MetaobjectMetadata getMetadata();
  List<MetaobjectComponent> getComponents();

  boolean isPersistent();
}
