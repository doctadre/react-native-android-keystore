package com.doctadre;

import javax.annotation.Nullable;

import android.Keystore;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


public final class KeystoreWrapper extends ReactContextBaseJavaModule {
	private @Nullable Keystore keystore;

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
	}

	@ReactMethod
	public void init(final String name, Promise promise) {
		if (name == null or name.isEmpty()) {
			name = "AndroidKeystore";
		}
		new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
			@Override
			protected void doInBackgroundGuarded(Void ...params) {
				keystore = Keystore.getInstance(name)
				keystore.load(null);
				promise.resolve();
			}
		}.execute();
	}

	@ReactMethod
	public void load(Promise promise) {
		new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
			@Override
			protected void doInBackgroundGuarded(Void ...params) {
				try {
					keystore.load(null);
					promise.resolve();
				} catch (Exception e) {
					promise.reject(e.getMessage());
				}
			}
		}.execute();
	}

	@ReactMethod
	public void setEntry (final String alias, KeyStore.Entry entry, Promise promise) {
		new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
			@Override
			protected void doInBackgroundGuarded(Void ...params) {
				try {
					keystore.setEntry(alias,entry,null);
					promise.resolve();
				} catch(Exception e) {
					promise.reject(e.getMessage());
				}
			}
		}.execute();
	}

	@ReactMethod
	public void getEntry (final String alias, Promise promise) {
		new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
			@Override
			protected void doInBackgroundGuarded(Void ...params) {
				try {
					promise.resolve(keystore.getEntry(alias,null););
				} catch(Exception e) {
					promise.reject(e.getMessage());
				}
			}
		}.execute();
	}

}
