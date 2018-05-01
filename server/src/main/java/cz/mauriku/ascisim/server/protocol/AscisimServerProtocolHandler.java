package cz.mauriku.ascisim.server.protocol;


import cz.mauriku.ascisim.server.objects.client.Client;
import cz.mauriku.ascisim.server.objects.client.ConnectedClient;
import cz.mauriku.ascisim.server.protocol.handshake.ClientHandshakeHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AscisimServerProtocolHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  private static final Logger LOG = LoggerFactory.getLogger(AscisimServerProtocolHandler.class);
  private static final AttributeKey<Client> CLIENT_ATTR = AttributeKey.newInstance("client");
  private static final ChannelGroup CHANNEL_GROUP_ALL = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  protected void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) throws Exception {
    if (frame instanceof BinaryWebSocketFrame) {
      Client client = getClientFromChannel(context.channel());
      BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;

      if (binFrame.content().readableBytes() > 0) {
        byte control = binFrame.content().readByte();
        if (!HANDLERS.containsKey(control)) {
          LOG.info("Disconnecting client [" + client.getId() + "]: invalid OPCODE received.");
          context.disconnect();
        }

        LOG.debug("Received opcode [" + String.format("%02X", control) + "] from client [" + client.getId() + "]");
        MessageHandler handler = HANDLERS.get(control);
        handler.handle(this, context.channel(), binFrame);
      }
    }
    else {
      // TODO
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    CHANNEL_GROUP_ALL.add(ctx.channel());
    Client client = new ConnectedClient();
    ctx.channel().attr(CLIENT_ATTR).set(client);
    LOG.info("Client [" + client.getId() + "] connected. Total clients [" + CHANNEL_GROUP_ALL.size() + "].");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOG.error("Error while handling communication.", cause);
  }

  public static Client getClientFromChannel(Channel channel) {
    return channel.attr(CLIENT_ATTR).get();
  }

  private final Map<Byte, MessageHandler> HANDLERS = new HashMap<Byte, MessageHandler>() {
    {
      put(ControlByte.CLIENT_HANDSHAKE_REQUEST.opcode, new ClientHandshakeHandler());
    }
  };
}
