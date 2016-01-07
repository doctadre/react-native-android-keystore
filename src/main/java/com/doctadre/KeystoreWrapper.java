package com.doctadre;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import javax.crypto.Mac;

import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.SecretKeyEntry;
import android.security.keystore.KeyProtection;
import android.security.keystore.KeyProperties;
import javax.crypto.KeyGenerator;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;


public final class KeystoreWrapper extends ReactContextBaseJavaModule {
	private @Nullable KeyStore keystore;
	private final String ANDROIDDEFAULTKEYSTORE="AndroidKeystore";
	private final KeyProtection PASSWORD = 	new KeyProtection.Builder(KeyProperties.PURPOSE_SIGN).build();

	public KeystoreWrapper(ReactApplicationContext reactContext) {
		super(reactContext);
	}

	@Override
	public String getName() {
		return "KeystoreWrapper";
	}

	@Override
	public void initialize() {
		super.initialize();
		try {
			keystore = KeyStore.getInstance(ANDROIDDEFAULTKEYSTORE);
			keystore.load(null);
		} catch (Exception e) {

		}
	}

	@ReactMethod
	public void init(final String name, final Promise promise) {
				try {
					if (name == null || name.isEmpty()) {
						keystore = KeyStore.getInstance(ANDROIDDEFAULTKEYSTORE);
					} else {
						keystore = KeyStore.getInstance(name);
					}
					keystore.load(null);
					promise.resolve(null);
				} catch (Exception e) {
					promise.reject(e.getMessage());
				}

	}

	@ReactMethod
	public void load(final Promise promise) {
				try {
					keystore.load(null);
					promise.resolve(null);
				} catch (Exception e) {
					promise.reject(e.getMessage());
				}
	}

	@ReactMethod
	public void setEntry (final String alias, final Promise promise) {
				try {
					SecretKey key = KeyGenerator.getInstance("HmacSHA512").generateKey();
					Entry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
					keystore.setEntry(alias,secretKeyEntry,PASSWORD);
					WritableMap map = Arguments.createMap();
		      map.putString("argument", alias);
		      map.putString("generated", new String(key.getEncoded()));
					System.out.print("Test");
					promise.resolve(map);
				} catch(Exception e) {
					promise.reject(e.getMessage());
				}
	}

	@ReactMethod
	public void getEntry (final String alias, final Promise promise) {
				try {
					keystore.load(null);
					// KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)keystore.getEntry(alias,null);
				 SecretKey keyStoreKey = (SecretKey) keystore.getKey(alias, null);
				 Mac mac = Mac.getInstance("HmacSHA512");
				 mac.init(keyStoreKey);
					WritableMap map = Arguments.createMap();
		      map.putString("argument", alias);
		      map.putString("generated", new String(mac.doFinal()));
					System.out.print("Test2");
					promise.resolve(map);
				} catch(Exception e) {
					promise.reject(e.getMessage());
				}
	}

}
