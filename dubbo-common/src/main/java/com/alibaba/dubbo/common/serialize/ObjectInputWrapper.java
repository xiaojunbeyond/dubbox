package com.alibaba.dubbo.common.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import com.alibaba.dubbo.common.compression.Decompressor;

/**
 * 对象输入流。
 * 
 * @author Daniel Li
 * @since 18 Jun 2013
 */
public class ObjectInputWrapper implements ObjectInput {

	protected AbstractObjectInput objectInput;

	public ObjectInputWrapper(AbstractObjectInput objectInput) {
		this.objectInput = objectInput;
	}
	
	public ObjectInputWrapper(AbstractObjectInput objectInput, InputStream inputStream, Decompressor<byte[], byte[]> decompressor) throws IOException {
		this.objectInput = objectInput;
		byte[] hasCompress = new byte[1];
		inputStream.read(hasCompress);
		if (hasCompress[0] == 1) {
			this.objectInput.setInputStream(decompressor.wrapper(inputStream));
		} else {
			this.objectInput.setInputStream(inputStream);
		}
	}

	@Override
	public boolean readBool() throws IOException {
		return objectInput.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return objectInput.readByte();
	}

	@Override
	public short readShort() throws IOException {
		return objectInput.readShort();
	}

	@Override
	public int readInt() throws IOException {
		return objectInput.readInt(true);
	}

	@Override
	public long readLong() throws IOException {
		return objectInput.readLong(true);
	}

	@Override
	public float readFloat() throws IOException {
		return objectInput.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return objectInput.readDouble();
	}

	@Override
	public String readUTF() throws IOException {
		return objectInput.readString();
	}

	@Override
	public byte[] readBytes() throws IOException {
        int len = objectInput.readInt(true);
        if (len == -1) {
        	return null;
        } else {
            return objectInput.readBytes(len);
        }
	}

	@Override
	public Object readObject() throws IOException, ClassNotFoundException {
		return objectInput.readObject();
	}

	@Override
	public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
		return objectInput.readObject(cls);
	}

	@Override
	public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
		return objectInput.readObject(cls);
	}
}