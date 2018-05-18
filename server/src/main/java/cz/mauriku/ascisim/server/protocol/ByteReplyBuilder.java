package cz.mauriku.ascisim.server.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteReplyBuilder {

  private ByteBuffer buffer;

  public ByteReplyBuilder begin(int size) {
    buffer = ByteBuffer.allocate(size);
    return this;
  }

  public ByteReplyBuilder add(ControlByte control) {
    buffer.put(control.opcode);
    return this;
  }

  public ByteReplyBuilder add(int value) {
    buffer.putInt(value);
    return this;
  }

  public ByteReplyBuilder add(long value) {
    buffer.putLong(value);
    return this;
  }

  public ByteReplyBuilder add(byte value) {
    buffer.put(value);
    return this;
  }

  public ByteReplyBuilder add(boolean value) {
    buffer.put(value? (byte) 1 : (byte) 0);
    return this;
  }

  public ByteReplyBuilder add(double value) {
    buffer.putDouble(value);
    return this;
  }

  public ByteReplyBuilder add(String str) {
    buffer.putInt(str.length());
    byte[] arr = str.getBytes();
    for (byte bt : arr)
      buffer.put(bt);

    return this;
  }

  public BinaryWebSocketFrame build() {
    ByteBuf buf = Unpooled.wrappedBuffer(buffer.array());
    return new BinaryWebSocketFrame(buf);
  }

  public static BinaryWebSocketFrame buildLoginReplyMessage(boolean authenticated, String token) {
    ByteReplyBuilder reply = new ByteReplyBuilder()
        .begin(256)
        .add(ControlByte.LOGIN)
        .add(authenticated)
        .add(token);

    return reply.build();
  }

  public static BinaryWebSocketFrame buildHandshakeReplyMessage(boolean open, int width, int height, int fontSize) {
    ByteReplyBuilder reply = new ByteReplyBuilder()
        .begin(64)
        .add(ControlByte.HANDSHAKE)
        .add(open) // server is open for client connections
        .add(width)  // screen width
        .add(height)   // screen height
        .add(fontSize);  // font size
    return reply.build();
  }
}
