package cz.mauriku.ascisim.server;


import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import cz.mauriku.ascisim.server.protocol.AscisimServerProtocolHandler;
import cz.mauriku.ascisim.server.services.PlayerAccountService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.*;

public class AscisimServer {

  private Ignite ignite;
  private ServerBootstrap server;

  private void initIgnite(AscisimServerOptions options) {
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

  private void initWebSocketServer(AscisimServerOptions options) throws InterruptedException {
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
                new AscisimServerProtocolHandler());
          }
        });
    final Channel ch = server.bind().sync().channel();
    ch.closeFuture().sync();
  }

  public static void main(String[] args) throws InterruptedException {
    AscisimServerOptions options = AscisimServerOptions.fromArguments(args);

    AscisimServer server = new AscisimServer();
    server.initIgnite(options);
    server.initWebSocketServer(options);
  }
}
