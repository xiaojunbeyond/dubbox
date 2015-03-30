package com.alibaba.dubbo.common.serialize.support.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * Kryo实例生成器。
 * 
 * @author xiaojunkingwolf
 */
public interface KryoGenerator {
	
	Kryo generate();
}