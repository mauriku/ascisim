package cz.mauriku.ascisim.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

public class ServerOptions {

  private static final Logger LOG = LoggerFactory.getLogger(ServerOptions.class);

  public final int DEFAULT_PORT = 7077;
  public final String DEFAULT_DATA_DIR = Paths.get("./data").toAbsolutePath().toString();

  private Integer port;
  private String dataDirPath;

  public static ServerOptions fromArguments(String[] args) {
    ServerOptions opts = new ServerOptions();
    Iterator<String> argumentsIterator = Arrays.asList(args).iterator();

    while(argumentsIterator.hasNext()) {
      String opt = argumentsIterator.next();

      if ("--wsp".equalsIgnoreCase(opt))
        opts.setPort(argumentsIterator.next());
      else if ("--data".equalsIgnoreCase(opt))
        opts.setDataDirPath(argumentsIterator.next());
    }

    return opts;
  }

  public Integer getPort() {
    return port != null ? port : DEFAULT_PORT;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setPort(String port) {
    try {
      this.port = Integer.valueOf(port, 10);
    } catch (NumberFormatException e) {
      this.port = DEFAULT_PORT;
      LOG.warn("Passed port number is invalid. Setting default " + DEFAULT_PORT + ".");
    }
  }

  public String getDataDirPath() {
    return dataDirPath != null ? dataDirPath : DEFAULT_DATA_DIR;
  }

  public void setDataDirPath(String dataDirPath) {
    this.dataDirPath = dataDirPath;
  }
}
