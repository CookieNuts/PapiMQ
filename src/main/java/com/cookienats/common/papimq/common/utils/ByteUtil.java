package com.cookienats.common.papimq.common.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteUtil {

	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 * int到byte[]
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static int byteToInt(byte[] bb) {
		int iOutcome = 0;
		byte bLoop;
		iOutcome += (bb[0] & 0xFF) << 24;
		iOutcome += (bb[1] & 0xFF) << 16;
		iOutcome += (bb[2] & 0xFF) << 8;
		iOutcome += (bb[3] & 0xFF);

		// for (int i = 0; i < bb.length; i++) {
		// bLoop = bb[i];
		// iOutcome += (bLoop & 0xFF) << (8 * (3 - i));
		// }
		return iOutcome;
	}

	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	// byte数组转成long
	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * int到byte[]
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] shortToByteArray(int i) {
		byte[] result = new byte[2];
		// 由高位到低位
		result[0] = (byte) ((i >> 8) & 0xFF);
		result[1] = (byte) (i & 0xFF);
		return result;
	}

	public static int byteToShort(byte[] bb) {
		int iOutcome = 0;
		byte bLoop;
		iOutcome += (bb[0] & 0xFF) << 8;
		iOutcome += (bb[1] & 0xFF);

		// for (int i = 0; i < bb.length; i++) {
		// bLoop = bb[i];
		// iOutcome += (bLoop & 0xFF) << (8 * (3 - i));
		// }
		return iOutcome;
	}
	

	public static byte[] toGetUnsignedByte(byte[] a) {
		byte[] tempByteU = null;
		int len = a.length;
		tempByteU = new byte[len];
		for (int i = 0; i < len; i++) {// 如果数据小于0，就用short类型数据与之与运算，负数在内存中以补码存放
			if (a[i] < 0) {
				tempByteU[i] =  (byte) (a[i] & 0xf);
			} else { // 大于0，不需要变
				tempByteU[i] = a[i];
			}
		}
		return tempByteU;
	}

	public static void main(String[] args) {
		byte[] b = shortToByteArray(170);
		b = new byte[]{-16, 46, 32, 14};
		
		System.out.println(ByteBuffer.wrap(b).asIntBuffer().get());
		System.out.println(byteToInt(toGetUnsignedByte(b)));
	}

	public static byte[] arrayToByte(long[] playerIds) {
		StringBuilder sb = new StringBuilder();
		for(long playerId : playerIds){
			sb.append(",").append(playerId);
		}
		return sb.substring(1).getBytes();
	}
//	public static byte[] arrayToByte(List<Long> playerIds) {
//		if(playerIds == null || playerIds.size() <= 0)
//		{
//			return new byte[0];
//		}
//		StringBuilder sb = new StringBuilder();
//		for(long playerId : playerIds){
//			sb.append(",").append(playerId);
//		}
//		return sb.substring(1).getBytes();
//	}

	public static long[] byteToArray(byte[] b) {
		String str = new String(b);
		long[] playerIds = new long[4];
		String[] s = str.split(",");
		for(int i = 0;i < 4;i++){
			playerIds[i] = Long.parseLong(s[i]);
		}
		return playerIds;
	}
}
