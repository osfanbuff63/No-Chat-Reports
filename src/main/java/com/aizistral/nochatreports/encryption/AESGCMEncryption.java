package com.aizistral.nochatreports.encryption;

import java.security.InvalidKeyException;

public class AESGCMEncryption extends AESEncryption {

	protected AESGCMEncryption() {
		super("GCM", "NoPadding", true);
	}

	@Override
	public AESGCMEncryptor getProcessor(String key) throws InvalidKeyException {
		return new AESGCMEncryptor(key);
	}

	@Override
	public AESGCMEncryptor getRandomProcessor() {
		try {
			return this.getProcessor(this.getRandomKey());
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}
