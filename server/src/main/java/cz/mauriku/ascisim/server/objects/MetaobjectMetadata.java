package cz.mauriku.ascisim.server.objects;

import java.time.LocalDateTime;

public interface MetaobjectMetadata {

  WorldObject getAuthor();
  LocalDateTime getDateCreated();
  String getDescription();

}
