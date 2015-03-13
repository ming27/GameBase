package com.qgf.game.base;

import java.util.List;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import com.qgf.game.base.protocol.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * [32bit length] + [avro data]
 * @author qinguofeng
 * @date Mar 10, 2015
 */
public class AvroDecoder extends ByteToMessageDecoder {
	
	private BinaryDecoder mBinaryDecoder;
	private DatumReader<Message> mDatumReader;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// int is 32bit, 4 bytes
		if (msg.isReadable() && msg.readableBytes() > 4) {
			msg.markReaderIndex();
			int msgSize = msg.readInt();
			int totalSize = msg.readableBytes();
			if (msgSize > totalSize) {
				msg.resetReaderIndex();
			}
			ByteBuf msgBuf = msg.readBytes(msgSize);
			mBinaryDecoder = DecoderFactory.get().binaryDecoder(msgBuf.array(), mBinaryDecoder);
			// TODO ... difference between SpecificDatumReader and ReflectDatumReader
			mDatumReader = new SpecificDatumReader<Message>(Message.class);
			// TODO ... do not reuse message.is it right?
			Message msgObj = mDatumReader.read(null, mBinaryDecoder);
			out.add(msgObj);

			msg.discardReadBytes();
		}
	}

}
