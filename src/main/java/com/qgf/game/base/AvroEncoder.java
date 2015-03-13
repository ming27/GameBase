package com.qgf.game.base;

import java.io.ByteArrayOutputStream;

import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import com.qgf.game.base.protocol.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * @author qinguofeng
 * @date Mar 10, 2015
 */
public class AvroEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
			throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Encoder encoder = EncoderFactory.get().binaryEncoder(bos, null);
		SpecificDatumWriter<Message> writer = new SpecificDatumWriter<Message>(Message.class);
		writer.write(msg, encoder);
		encoder.flush();
		byte[] bs = bos.toByteArray();
//		ByteBuffer bb = ByteBuffer.allocate(bs.length + 4);
//		bb.putInt(bs.length);
//		bb.put(bs);
		out.writeInt(bs.length);
		out.writeBytes(bs);
		bos.close();
	}

}
