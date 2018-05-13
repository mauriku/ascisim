package cz.mauriku.ascisim.server.protocol;


import cz.mauriku.ascisim.server.objects.client.PlayerAccount;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
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

@ChannelHandler.Sharable
public class PaxImpProtocolHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  private static final Logger LOG = LoggerFactory.getLogger(PaxImpProtocolHandler.class);

  private static final AttributeKey<PlayerAccount> ACCOUNT_ATTR = AttributeKey.newInstance("account");
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
      PlayerAccount acc = getAccountFromChannel(context.channel());
      BinaryWebSocketFrame binFrame = (BinaryWebSocketFrame) frame;

      if (binFrame.content().readableBytes() > 0) {
        byte control = binFrame.content().readByte();
        if (!handlers.containsKey(control)) {
          LOG.info("Disconnecting client [" + (acc != null ? acc.getEmail() : "N/A") + "]: invalid OPCODE received.");
          context.disconnect();
        }

        LOG.debug("Received opcode [" + String.format("%02X", control) + "] from client [" +
            (acc != null ? acc.getEmail() : "N/A") + "]");
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
    LOG.info("Client connected. Total clients [" + CHANNEL_GROUP_ALL.size() + "].");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOG.error("Error while handling communication.", cause);
  }

  public static PlayerAccount getAccountFromChannel(Channel channel) {
    return channel.attr(ACCOUNT_ATTR).get();
  }
}
