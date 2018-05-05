package cz.mauriku.ascisim.server.protocol;


import cz.mauriku.ascisim.server.objects.client.PlayerClient;
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

public class PaxImpProtocolHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  private static final Logger LOG = LoggerFactory.getLogger(PaxImpProtocolHandler.class);

  private static final AttributeKey<PlayerClient> CLIENT_ATTR = AttributeKey.newInstance("client");
  private static final ChannelGroup CHANNEL_GROUP_ALL = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  private final Map<Byte, MessageHandler> handlers;

  public PaxImpProtocolHandler(MessageHandler ... handlers) {
    this.handlers = new HashMap<>();
    for (MessageHandler handler : handlers)
      this.handlers.put(handler.getHandledByte().opcode, handler);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) throws Exception {
    if (frame instanceof BinaryWebSocketFrame) {
      PlayerClient client = getClientFromChannel(context.channel());
      BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;

      if (binFrame.content().readableBytes() > 0) {
        byte control = binFrame.content().readByte();
        if (!handlers.containsKey(control)) {
          LOG.info("Disconnecting client [" + client.getId() + "]: invalid OPCODE received.");
          context.disconnect();
        }

        LOG.debug("Received opcode [" + String.format("%02X", control) + "] from client [" + client.getId() + "]");
        MessageHandler handler = handlers.get(control);
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
    PlayerClient client = new PlayerClient();
    ctx.channel().attr(CLIENT_ATTR).set(client);
    LOG.info("Client [" + client.getId() + "] connected. Total clients [" + CHANNEL_GROUP_ALL.size() + "].");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOG.error("Error while handling communication.", cause);
  }

  public static PlayerClient getClientFromChannel(Channel channel) {
    return channel.attr(CLIENT_ATTR).get();
  }
}
