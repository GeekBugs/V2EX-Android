package com.f1reking.v2ex.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * AndroidManifest配置文件类
 * Created by F1ReKing on 2016/1/2.
 */
public class ManifestConfig {

	/**
	 * 获取配置在AndroidMinifest指定key的metaData值
	 * 
	 * @param context
	 * @param metaKey
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getMetaValue(Context context, String metaKey,
			Class<T> cls) {
		Bundle metaData = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				if ("String".equals(cls.getSimpleName())) {
					return (T) metaData.getString(metaKey);
				} else if ("Integer".equals(cls.getSimpleName())) {
					Integer intValue = metaData.getInt(metaKey);
					return (T) intValue;
				} else if ("Boolean".equals(cls.getSimpleName())) {
					Boolean booleanValue = metaData.getBoolean(metaKey, true);
					return (T) booleanValue;
				} else if ("Double".equals(cls.getSimpleName())) {
					Double doubleValue = metaData.getDouble(metaKey, 0);
					return (T) doubleValue;
				} else if ("Float".equals(cls.getSimpleName())) {
					Float floatValue = metaData.getFloat(metaKey, 0);
					return (T) floatValue;
				} else {
					throw new RuntimeException(
							cls.getSimpleName()
									+ ":Unsupport data type for meta-data which named '"
									+ metaKey + "' in AndroidManifest");
				}
			}
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Undefined meta-data which named "
					+ metaKey + " in AndroidManifest");
		}
		return null;
	}

	/**
	 * @param context
	 * @param metaKey
	 * @return 获取String类型的值
	 */
	public static String getStringMetaValue(Context context, String metaKey) {
		return getMetaValue(context, metaKey, String.class);
	}

	/**
	 * @param context
	 * @param metaKey
	 * @return 获取int类型的值
	 */
	public static int getIntMetaValue(Context context, String metaKey) {
		return getMetaValue(context, metaKey, Integer.class);
	}

	/**
	 * @param context
	 * @param metaKey
	 * @return 获取boolean类型的值
	 */
	public static boolean getBooleanMetaValue(Context context, String metaKey) {
		return getMetaValue(context, metaKey, Boolean.class);
	}

	/**
	 * @param context
	 * @param metaKey
	 * @return 获取Double类型的值
	 */
	public static double getDoubleMetaValue(Context context, String metaKey) {
		return getMetaValue(context, metaKey, Double.class);
	}

	/**
	 * @param context
	 * @param metaKey
	 * @return 获取Double类型的值
	 */
	public static float getFloatMetaValue(Context context, String metaKey) {
		return getMetaValue(context, metaKey, Float.class);
	}
}
