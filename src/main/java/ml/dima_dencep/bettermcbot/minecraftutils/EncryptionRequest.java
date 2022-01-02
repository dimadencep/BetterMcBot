package ml.dima_dencep.bettermcbot.minecraftutils;

import io.netty.buffer.ByteBuf;

public class EncryptionRequest extends DefinedPacket {
   public String serverId;
   public byte[] publicKey;
   public byte[] verifyToken;

   public void read(ByteBuf buf) {
      this.serverId = DefinedPacket.readString(buf);
      this.publicKey = DefinedPacket.readArray(buf);
      this.verifyToken = DefinedPacket.readArray(buf);
   }
}
