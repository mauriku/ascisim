package cz.mauriku.ascisim.server.objects;

import java.time.LocalDateTime;

public interface MetaobjectMetadata {

  Object getAuthor();
  LocalDateTime getDateCreated();
  String getDescription();

}
