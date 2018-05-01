package cz.mauriku.ascisim.server.objects.client;

/*
 * Copyright 2018 by SEFIRA, spol. s r. o.
 * http://www.sefira.cz
 * 
 * cz.mauriku.ascisim.server.objects.client.Client
 *
 * Created: 26.04.2018 
 * Author: smolka
 */

import cz.mauriku.ascisim.server.objects.Object;

/**
 * description
 */
public interface Client extends Object {

  void onConnect();
}
