package com.hydrovolt.decoder;

import com.hydrovolt.model.TelemetryPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class CustomPacketDecoder extends ByteToMessageDecoder {

    // Frame size: 4 bytes (Int) + 4 bytes (Int) + 4 bytes (Float) + 4 bytes (Int/CRC) = 16 Bytes
    private static final int PACKET_SIZE = 16;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TCP Stream Buffer Check: Jab tak poore 16 bytes nahi milte, wait karo
        if (in.readableBytes() < PACKET_SIZE) {
            return;
        }

        // Direct Off-Heap Memory se Fast Byte Reading
        int deviceId = in.readInt();
        long timestamp = in.readUnsignedInt(); // 4-byte unsigned timestamp
        float metricValue = in.readFloat();
        long crc32 = in.readUnsignedInt();

        // Object build karke next pipeline handler ko pass kar do
        TelemetryPacket packet = new TelemetryPacket(deviceId, timestamp, metricValue, crc32);
        out.add(packet);
    }
}
