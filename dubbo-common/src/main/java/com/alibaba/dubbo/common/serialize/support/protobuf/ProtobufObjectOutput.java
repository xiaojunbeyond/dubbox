package com.alibaba.dubbo.common.serialize.support.protobuf;

import com.alibaba.dubbo.common.serialize.AbstractObjectOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.esotericsoftware.kryo.Kryo;

/**
 * Protobuf序列化机制的对象输出流，针对Date/DateTime、String、Collection、Map、Long/Double/Integer/Float/Character/Short/Byte/Boolean/byte[]特殊处理。
 * @author xiaojunkingwolf
 */
public class ProtobufObjectOutput implements AbstractObjectOutput {
	private final LinkedBuffer buffer;
	
	private final Kryo kryo;

	public ProtobufObjectOutput (int bufferSize, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(bufferSize);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	public ProtobufObjectOutput (int bufferSize, int maxBufferSize, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(bufferSize, maxBufferSize);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	public ProtobufObjectOutput (byte[] buffer, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(buffer);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	public ProtobufObjectOutput (byte[] buffer, int maxBufferSize, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(buffer, maxBufferSize);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	public ProtobufObjectOutput (OutputStream outputStream, int bufferSize, int maxBufferSize, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(outputStream, bufferSize, maxBufferSize);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	public ProtobufObjectOutput (OutputStream outputStream, int bufferSize, LinkedBuffer linkedBuffer, Kryo kryo) {
		super(outputStream, bufferSize);
		this.buffer = linkedBuffer;
		this.kryo = kryo;
	}

	
	@Override
	public void writeObject(Object obj) throws IOException {
		if (obj == null) {
			writeByte(0);
		} else if (obj instanceof Collection || obj instanceof Map) {
			writeByte(1);
			kryo.writeClassAndObject(this, obj);
		} else if (obj instanceof String) {
			writeByte(2);
			writeString((String) obj);
		} else if (obj instanceof Long) {
			writeByte(3);
			writeLong((Long) obj, true);
		} else if (obj instanceof Integer) {
			writeByte(4);
			writeInt((Integer) obj, true);
		} else if (obj instanceof Boolean) {
			writeByte(5);
			writeBoolean((Boolean) obj);
		} else if (obj instanceof Date) {
			writeByte(8);
			writeLong(((Date) obj).getTime(), true);
		} else if (obj instanceof DateTime) {
			writeByte(7);
			writeLong(((DateTime) obj).getMillis(), true);
		} else if (obj instanceof Double) {
			writeByte(9);
			writeDouble((Double) obj);
		} else if (obj instanceof Character) {
			writeByte(10);
			writeChar((Character) obj);
		} else if (obj instanceof Float) {
			writeByte(11);
			writeFloat((Float) obj);
		} else if (obj instanceof Short) {
			writeByte(12);
			writeShort((Short) obj);
		} else if (obj instanceof Byte) {
			writeByte(13);
			writeByte((Byte) obj);
		} else if (obj instanceof byte[]) {
			writeByte(14);
			byte[] result = (byte[]) obj;
			writeInt(result.length, true);
			writeBytes(result);
		} else {
			writeByte(6);
			@SuppressWarnings("unchecked")
			Class<Object> clazz = (Class<Object>) obj.getClass();
			kryo.writeClass(this, clazz);
//			MutableObject<Object> holder = new MutableObject<Object>(obj);
			try {
//				ProtobufIOUtil.writeDelimitedTo(this, holder, SCHEMA, buffer);
				ProtostuffIOUtil.writeDelimitedTo(this, obj, RuntimeSchema.getSchema(clazz), buffer);
			} finally {
				buffer.clear();
			}
		}
	}
}