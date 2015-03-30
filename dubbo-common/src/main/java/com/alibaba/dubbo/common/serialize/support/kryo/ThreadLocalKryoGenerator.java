package com.alibaba.dubbo.common.serialize.support.kryo;

import com.esotericsoftware.kryo.Kryo;

public class ThreadLocalKryoGenerator implements KryoGenerator {

	public static final ThreadLocalKryoGenerator INSTANCE = new ThreadLocalKryoGenerator();
	
	private KryoGenerator kryoGenerator = DefaultKryoGenerator.INSTANCE;
	
	private ThreadLocalKryoGenerator() {
	}
	
	@Override
	public Kryo generate() {
		Kryo currentKryo = KryoContext.currentKryo();
		if (currentKryo == null) {
			currentKryo = kryoGenerator.generate();
			KryoContext.setCurrentKryo(currentKryo);
		}
		return currentKryo;
	}

	public KryoGenerator getKryoGenerator() {
		return kryoGenerator;
	}

	public void setKryoGenerator(KryoGenerator kryoGenerator) {
		this.kryoGenerator = kryoGenerator;
	}

}