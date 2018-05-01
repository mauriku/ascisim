package cz.mauriku.ascisim.server.objects.client;

/*
 * Copyright 2018 by SEFIRA, spol. s r. o.
 * http://www.sefira.cz
 * 
 * cz.mauriku.ascisim.server.objects.client.ConnectedClient
 *
 * Created: 26.04.2018 
 * Author: smolka
 */

import java.util.UUID;

/**
 * description
 */
public class ConnectedClient implements Client {

  private String id;

  public ConnectedClient() {
    id = UUID.randomUUID().toString();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void onConnect() {

  }
}
