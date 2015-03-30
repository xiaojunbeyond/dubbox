package com.alibaba.dubbo.common.serialize.support.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.compression.Compressor;
import com.alibaba.dubbo.common.compression.Decompressor;
import com.alibaba.dubbo.common.compression.support.SnappyJavaCompressor;
import com.alibaba.dubbo.common.serialize.support.ProtobufObjectInput;
import com.alibaba.dubbo.common.serialize.support.ProtobufObjectOutput;
import com.alibaba.dubbo.common.serialize.support.kryo.ThreadLocalKryoGenerator;
import com.alibaba.dubbo.common.util.LinkedBufferUtils;
import org.danielli.xultimate.remoting.dubbo.serialize.support.ObjectInput;
import org.danielli.xultimate.remoting.dubbo.serialize.support.ObjectOutput;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.OptimizedSerialization;

/**
 * Protobuf序列化器。
 * 
 * @author Daniel Li
 * @since 18 Jun 2013
 */
public class ProtobufSerialization implements OptimizedSerialization {
	
	protected int bufferSize = 256;
	
	protected Compressor<byte[], byte[]> compressor = SnappyJavaCompressor.COMPRESSOR;
	
	protected Decompressor<byte[], byte[]> decompressor = SnappyJavaCompressor.COMPRESSOR;
	
	protected int compressionThreshold = 512;
	
	public byte getContentTypeId() {
        return 11;
    }

    public String getContentType() {
        return "x-application/protobuf";
    }

	@Override
	public com.alibaba.dubbo.common.serialize.ObjectOutput serialize(URL url, OutputStream output) throws IOException {
		return new ObjectOutput(new ProtobufObjectOutput(bufferSize, LinkedBufferUtils.getCurrentLinkedBuffer(bufferSize), ThreadLocalKryoGenerator.INSTANCE.generate()), output, compressionThreshold, compressor);
	}

	@Override
	public com.alibaba.dubbo.common.serialize.ObjectInput deserialize(URL url, InputStream input) throws IOException {
		return new ObjectInput(new ProtobufObjectInput(bufferSize, ThreadLocalKryoGenerator.INSTANCE.generate()), input, decompressor);
	}
}