package cz.mauriku.ascisim.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

import java.net.InetSocketAddress;

public class PaxImpServer {

  private Ignite ignite;
  private ServerBootstrap server;
  private final ServerOptions options;

  public PaxImpServer(ServerOptions options) {
    this.options = options;
  }

  public void initIgnite() {
    IgniteConfiguration cfg = new IgniteConfiguration();
    cfg.setGridLogger(new Slf4jLogger());
    cfg.setClientMode(false);
    cfg.setIgniteHome(options.getDataDirPath() + "/db");

    DataStorageConfiguration dataCfg = new DataStorageConfiguration();
    dataCfg.setDefaultDataRegionConfiguration(new DataRegionConfiguration().setPersistenceEnabled(true));
    cfg.setDataStorageConfiguration(dataCfg);

    ignite = Ignition.start(cfg);
    ignite.cluster().active(true);
  }

  public void initWebSocketServer(ChannelHandler handler) throws InterruptedException {
    server = new ServerBootstrap();
    server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress(options.getPort()))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(final SocketChannel ch) throws Exception {
            ch.pipeline().addLast(
                new HttpRequestDecoder(),
                new HttpObjectAggregator(65536),
                new HttpResponseEncoder(),
                new WebSocketServerProtocolHandler("/", null, true),
                handler);
          }
        });
    final Channel ch = server.bind().sync().channel();
    ch.closeFuture().sync();
  }

  public Ignite getIgnite() {
    return ignite;
  }

  public ServerBootstrap getServer() {
    return server;
  }
}
