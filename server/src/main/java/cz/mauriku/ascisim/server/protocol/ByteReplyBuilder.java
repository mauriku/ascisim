package cz.mauriku.ascisim.server.protocol;

import cz.mauriku.ascisim.server.objects.PaxImpCharacter;
import cz.mauriku.ascisim.server.objects.PaxImpObject;
import cz.mauriku.ascisim.server.objects.PaxImpPositioning;
import cz.mauriku.ascisim.server.services.ObjectService;
import cz.mauriku.ascisim.server.services.PositioningService;
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
    buffer.put(value ? (byte) 1 : (byte) 0);
    return this;
  }

  public ByteReplyBuilder add(double value) {
    buffer.putDouble(value);
    return this;
  }

  public ByteReplyBuilder add(String str) {
    byte[] arr = str.getBytes();
    buffer.putInt(arr.length);
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
        .add(open)        // server is open for client connections
        .add(width)       // screen width
        .add(height)      // screen height
        .add(fontSize);   // font size
    return reply.build();
  }

  public static BinaryWebSocketFrame buildCharacterUpdateMessage(
      CharacterUpdateByte charByte,
      PaxImpCharacter character,
      PositioningService positioningService,
      ObjectService objectService
  ) {
    ByteReplyBuilder reply = new ByteReplyBuilder()
        .begin(512)
        .add(ControlByte.CHARACTER_UPDATE)
        .add(charByte.opcode);

    PaxImpPositioning pos = null;
    PaxImpObject location = null;

    if (charByte == CharacterUpdateByte.UI || charByte == CharacterUpdateByte.POSITION) {
      pos = positioningService.getPosition(character.getId());
      location = objectService.findById(character.getLocationId());
    }

    switch (charByte) {
      case UI:
        reply.add("" + character.getObjectProperty(PaxImpObject.CHAR)); // character visualization
        reply.add(character.getName());                                 // character name
        reply.add((int) pos.getPoint().getX());                         // position x
        reply.add((int) pos.getPoint().getY());                         // position y
        reply.add(location.getName());                                  // location name
        reply.add(character.getCurrentHitPoints());                     // current hp
        reply.add(character.getMaximumHitPoints());                     // maximum hp
        reply.add(character.getCurrentEnergyPoints());                  // current energy points
        reply.add(character.getMaximumEnergyPoints());                  // maximum energy points
        reply.add(character.getLevel());                                // level
        reply.add(character.getCurrentXp());                            // current xp
        reply.add(character.getAdvanceXp());                            // advance xp
        break;

      case POSITION:
        reply.add((int) pos.getPoint().getX());
        reply.add((int) pos.getPoint().getY());
        break;
    }

    return reply.build();
  }
}
