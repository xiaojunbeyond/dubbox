package com.alibaba.dubbo.common.compression;

import org.springframework.core.NestedRuntimeException;

/**
 * 压缩异常。
 * 
 * @author xiaojunkingwolf
 * @see NestedRuntimeException
 */
public class CompressorException extends NestedRuntimeException {
	
	private static final long serialVersionUID = 8862452193888553128L;
	
    public CompressorException(String message) {
    	super(message);
    }
	
    public CompressorException(String message, Throwable cause) {
        super(message, cause);
    }
}