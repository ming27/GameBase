package com.qgf.game.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 *
 * @author qinguofeng
 * @date Mar 13, 2015
 */
public class AvroUtil {
	public static Object decodeFrom(ByteBuffer data, String name) {
		// TODO ... 缓存decoder等信息 提高效率
		try {
			Class<?> clazz = Class.forName(name);
			BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data.array(), null);
			DatumReader<?> reader = new SpecificDatumReader<>(clazz);
			return reader.read(null, decoder);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> ByteBuffer encode2Byte(T t) {
		// TODO ... 优化序列化方式, 目前这种不确定是否高效
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Encoder encoder = EncoderFactory.get().binaryEncoder(bos, null);
			SpecificDatumWriter<T> writer = new SpecificDatumWriter<T>((Class<T>)t.getClass());
			writer.write(t, encoder);
			encoder.flush();
			byte[] bs = bos.toByteArray();
			ByteBuffer buffer = ByteBuffer.allocate(bs.length);
			buffer.put(bs);
			buffer.flip();
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
