package cz.mauriku.ascisim.server;


import cz.mauriku.ascisim.server.protocol.AscisimServerProtocolHandler;
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

import java.net.InetSocketAddress;

public class AscisimServer {

  // https://www.gridgain.com/resources/blog/we-use-apache-ignite-in-everyday-life
  //https://leanjava.co/2018/03/14/getting-started-with-netty-building-a-websocket-broadcast-server/

  public static void main(String[] args) throws InterruptedException {
    final ServerBootstrap sb = new ServerBootstrap();
    try {
      sb.group(new NioEventLoopGroup(), new NioEventLoopGroup())
          .channel(NioServerSocketChannel.class)
          .localAddress(new InetSocketAddress(7070))
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

      final Channel ch = sb.bind().sync().channel();

      ch.closeFuture().sync();
    } finally {
      //
    }
  }
}
