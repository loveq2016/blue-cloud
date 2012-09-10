package com.yonyouhealth.uaph.framework.comm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class StringUtils {
	private static final String[] NullStringArray2 = new String[2];

	public static String formatString(String src, char s2, int length) {
		String retValue = src;
		if ((src == null) || (length <= 0)) {
			return null;
		}

		if (src.length() > length) {
			retValue = src.substring(0, length);
		}

		for (int i = 0; i < length - src.length(); ++i) {
			retValue = s2 + retValue;
		}

		return retValue;
	}

	public static String replaceByPos(String src, int start, int end, String dst) {
		if ((src == null) || (dst == null) || (end < start)) {
			return null;
		}

		return src.substring(0, start) + dst + src.substring(end + 1, src.length());
	}

	public static int countOccurrencesOf(String s, String sub) {
		if ((s == null) || (sub == null) || ("".equals(sub))) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx = 0;
		while ((idx = s.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if ((oldPattern == null) || (newPattern == null)) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();

		int pos = 0;
		int index = inString.indexOf(oldPattern);

		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		return sbuf.toString();
	}

	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	public static String deleteAny(String inString, String chars) {
		if ((inString == null) || (chars == null)) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); ++i) {
			char c = inString.charAt(i);
			if (chars.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	public static String[] tokenizeToStringArray(String s, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {
		StringTokenizer st = new StringTokenizer(s, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if ((!ignoreEmptyTokens) || (token.length() != 0)) {
				tokens.add(token);
			}
		}
		return (String[]) (String[]) tokens.toArray(new String[tokens.size()]);
	}

	public static String[] delimitedListToStringArray(String s, String delim) {
		if (s == null) {
			return new String[0];
		}
		if (delim == null) {
			return new String[] { s };
		}

		List l = new LinkedList();
		int pos = 0;
		int delPos = 0;
		while ((delPos = s.indexOf(delim, pos)) != -1) {
			l.add(s.substring(pos, delPos));
			pos = delPos + delim.length();
		}
		if (pos <= s.length()) {
			l.add(s.substring(pos));
		}

		return (String[]) (String[]) l.toArray(new String[l.size()]);
	}

	public static String[] commaDelimitedListToStringArray(String s) {
		return delimitedListToStringArray(s, ",");
	}

	public static Set commaDelimitedListToSet(String s) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(s);
		for (int i = 0; i < tokens.length; ++i) {
			set.add(tokens[i]);
		}
		return set;
	}

	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (arr == null) {
			return "null";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String collectionToDelimitedString(Collection c, String delim) {
		if (c == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();
		Iterator itr = c.iterator();
		int i = 0;
		while (itr.hasNext()) {
			if (i++ > 0) {
				sb.append(delim);
			}
			sb.append(itr.next());
		}
		return sb.toString();
	}

	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	public static String collectionToCommaDelimitedString(Collection c) {
		return collectionToDelimitedString(c, ",");
	}

	public static String[] addStringToArray(String[] arr, String s) {
		String[] newArr = new String[arr.length + 1];
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		newArr[arr.length] = s;
		return newArr;
	}

	public static boolean hasLength(String str) {
		return (str != null) && (str.length() > 0);
	}

	public static boolean hasText(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return false;
		// int strLen;
		for (int i = 0; i < strLen; ++i) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, ".");
	}

	public static String unqualify(String qualifiedName, String separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	public static String uncapitalize(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return str;
		// int strLen;
		StringBuffer buf = new StringBuffer(strLen);
		buf.append(Character.toLowerCase(str.charAt(0)));
		buf.append(str.substring(1));
		return buf.toString();
	}

	public static final boolean isEmptyString(String s) {
		if (s == null) {
			return true;
		}

		return "".equals(s.trim());
	}

	public static final String[] split(String string, String delim) {
		StringTokenizer token = new StringTokenizer(string, delim);
		String[] result = new String[token.countTokens()];
		List tmp = new ArrayList();
		while (token.hasMoreTokens()) {
			tmp.add(token.nextToken());
		}
		tmp.toArray(result);
		return result;
	}

	public static final String[] splitStringForOracle(String s) {
		if (s == null) {
			return NullStringArray2;
		}
		int length = s.length();
		if (length <= 650) {
			return new String[] { s, null };
		}

		String a = s.substring(0, 650);
		String b = s.substring(650, length);
		return new String[] { a, b };
	}

	public static String charSetConvert(String src, String fromCharSet, String toCharSet) {
		if (src == null)
			return src;
		try {
			return new String(src.getBytes(fromCharSet), toCharSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String isoToUTF8(String src) {
		return charSetConvert(src, "iso-8859-1", "UTF-8");
	}

	public static String isoToGBK(String src) {
		return charSetConvert(src, "iso-8859-1", "GBK");
	}

	public static String gbkToISO(String src) {
		return charSetConvert(src, "GBK", "iso-8859-1");
	}

	public static String gbkToUTF8(String src) {
		return charSetConvert(src, "GBK", "UTF-8");
	}

	public static String utftoGBK(String src) {
		return charSetConvert(src, "UTF-8", "GBK");
	}

	public static String utftoISO(String src) {
		return charSetConvert(src, "UTF-8", "iso-8859-1");
	}

	public static String gb2312ToISO(String src) {
		return charSetConvert(src, "GB2312", "iso-8859-1");
	}

	public static String gb2312ToUTF8(String src) {
		return charSetConvert(src, "GB2312", "UTF-8");
	}

	public static boolean notNull(String str) {
		boolean result = false;
		if ((str != null) && (!"".equals(str.trim()))) {
			result = true;
		}
		return result;
	}

	public static ArrayList splitStr2AL(String input, String delim) {
		if (input == null) {
			return null;
		}
		StringTokenizer myst = null;

		if (delim == null) {
			myst = new StringTokenizer(input);
		} else {
			myst = new StringTokenizer(input, delim);
		}

		ArrayList myAL = new ArrayList();

		while (myst.hasMoreTokens()) {
			myAL.add(myst.nextToken());
		}
		return myAL;
	}

	public static String trimToEmpty(String str) {
		return (str != null) ? str : "";
	}
}