package kr.co.takeit.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * OGNL 인터프리터에서는 위 구문의 'Y' 를 char 형으로 인식하고,
 * 'YY' 나 "Y" 는 String으로 인식한다. (따옴표를 잘보자)
 * 그래서 <if test="stringValue == 'Y'"> 이와 같은 구문을 비교할 때 NumberFormat으로 비교를 시도하여 Exception이 발생한다.
 * 이유는 java의 char형은 실제로 문자의 코드값을 저장하기 때문이다
 *
 * 사용법 : <if test="@kr.co.seahsp.util.MybatisUtil@notEmpty(REQ_EMP)">
 *
 * @author glorial
 *
 */
public class MybatisUtil {
	/**
	 * Object type 변수가 비어있는지 체크
	 *
	 * @param obj
	 * @return Boolean : true / false
	 */
	public static Boolean empty(Object obj) {
		if (obj instanceof String)
			return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List)
			return obj == null || ((List<?>) obj).isEmpty();
		else if (obj instanceof Map)
			return obj == null || ((Map<?, ?>) obj).isEmpty();
		else if (obj instanceof Object[])
			return obj == null || Array.getLength(obj) == 0;
		else
			return obj == null;
	}

	/**
	 * Object type 변수가 비어있지 않은지 체크
	 *
	 * @param obj
	 * @return Boolean : true / false
	 */
	public static Boolean notEmpty(Object obj) {
		return !empty(obj);
	}

	/**
	 * 현재날짜 long
	 *
	 * @return
	 */
	public static long getCurrentTime() {
		//'${@kr.co.takeit.util.MybatisUtil@getCurrentTime(param)}'
		return System.currentTimeMillis();
	}
}
