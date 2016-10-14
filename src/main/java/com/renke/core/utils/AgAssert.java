package com.renke.core.utils;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.renke.core.exception.CodeException;
import com.renke.core.exception.NoLoginException;
import com.renke.core.exception.SysException;

/**
 * same as  org.springframework.util.Assert, but throw CodeException 
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 * @author Rob Harrop
 * @author JoinClub004
 * @since 1.1.2
 * 
 * @see org.springframework.util.Assert
 */
public abstract class AgAssert {

	/**
	 * Assert a boolean expression, throwing {@code CodeException}
	 * if the test result is {@code false}.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if expression is {@code false}
	 */
	public static void isTrue(boolean expression, int code, String message ) {
		if (!expression) {
			throw new CodeException(code, message);
		}
	}

	public static void isTrue(boolean expression, int code) {
		isTrue(expression, code, "[Assertion failed] - this expression must be true");
	}


	public static void isTrue(boolean expression, Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (!expression) {
			passCodeException(codeExceptionClazz, args);
		}
	}


	/**
	 * Assert that an object is {@code null} .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the object is not {@code null}
	 */
	public static void isNull(Object object, int code, String message ) {
		if (object != null) {
			throw new CodeException(code, message);
		}
	}

	public static void isNull(Object object, int code) {
		isNull(object, code, "[Assertion failed] - the object argument must be null");
	}

	public static void isNull(Object object,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (object != null) {
			passCodeException(codeExceptionClazz, args);
		}
	}


	/**
	 * Assert that an object is not {@code null} .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the object is {@code null}
	 */
	public static void notNull(Object object, int code, String message) {
		if (object == null) {
			throw new CodeException(code, message);
		}
	}

	public static void notNull(Object object, int code) {
		notNull(object, code, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void notNull(Object object,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (object == null) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be {@code null} and not the empty String.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(String text, int code, String message) {
		if (!StringUtils.hasLength(text)) {
			throw new CodeException(code, message);
		}
	}

	public static void hasLength(String text, int code) {
		hasLength(text, code,
				"[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasLength(String text,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (!StringUtils.hasLength(text)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be {@code null} and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(String text, int code, String message) {
		if (!StringUtils.hasText(text)) {
			throw new CodeException(code, message);
		}
	}

	public static void hasText(String text, int code) {
		hasText(text, code,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}
	
	public static void hasText(String text,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (!StringUtils.hasText(text)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 */
	public static void doesNotContain(String textToSearch, String substring, int code, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			throw new CodeException(code, message);
		}
	}

	public static void doesNotContain(String textToSearch, String substring, int code) {
		doesNotContain(textToSearch, substring, code,
				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	public static void doesNotContain(String textToSearch, String substring, Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.contains(substring)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array, int code, String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new CodeException(code, message);
		}
	}

	public static void notEmpty(Object[] array, int code) {
		notEmpty(array, code, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}
	
	public static void notEmpty(Object[] array,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (ObjectUtils.isEmpty(array)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array, int code, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new CodeException(code, message);
				}
			}
		}
	}

	public static void noNullElements(Object[] array, int code) {
		noNullElements(array, code, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void noNullElements(Object[] array,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					passCodeException(codeExceptionClazz, args);
				}
			}
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection, int code, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new CodeException(code, message);
		}
	}

	public static void notEmpty(Collection<?> collection, int code) {
		notEmpty(collection, code,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static void notEmpty(Collection<?> collection,  Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (CollectionUtils.isEmpty(collection)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null}
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param code the errorCode
	 * @param message the exception message to use if the assertion fails
	 * @throws CodeException if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map, int code, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new CodeException(code, message);
		}
	}

	public static void notEmpty(Map<?, ?> map, int code) {
		notEmpty(map, code, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	public static void notEmpty(Map<?, ?> map, Class<? extends CodeException> codeExceptionClazz, Object... args) {
		if (CollectionUtils.isEmpty(map)) {
			passCodeException(codeExceptionClazz, args);;
		}
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param code the errorCode
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws CodeException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> type, Object obj, int code, String message) {
		notNull(type, code, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new CodeException(
					code, (StringUtils.hasLength(message) ? message + " " : "") +
					"Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
					"] must be an instance of " + type);
		}
	}

	public static void isInstanceOf(Class<?> clazz, Object obj, int code) {
		isInstanceOf(clazz, obj, code, "");
	}
	
	public static void isInstanceOf(Class<?> type, Object obj, Class<? extends CodeException> codeExceptionClazz, Object... args) {
		notNull(type, codeExceptionClazz, args);
		if (!type.isInstance(obj)) {
			passCodeException(codeExceptionClazz, args);
		}
	}

	/**
	 * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check against
	 * @param subType the sub type to check
	 * @param code the errorCode
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws CodeException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, int code, String message) {
		notNull(superType, code, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new CodeException(code, message + subType + " is not assignable to " + superType);
		}
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, int code) {
		isAssignable(superType, subType, code, "");
	}

	public static void isAssignable(Class<?> superType, Class<?> subType, Class<? extends CodeException> codeExceptionClazz, Object... args) {
		notNull(superType, codeExceptionClazz, args);
		if (subType == null || !superType.isAssignableFrom(subType)) {
			passCodeException(codeExceptionClazz, args);
		}
	}
	
	private static void passCodeException(Class<? extends CodeException> codeExceptionClazz, Object... args) {
		try {
			if (args != null && args.length > 0) {
				@SuppressWarnings("rawtypes")
				Class[] parameterTypes = new Class[args.length];
				for (int i = 0; i < args.length; i++) {
					parameterTypes[i] = args[i].getClass();
				}
				Constructor<? extends CodeException> constructor = codeExceptionClazz.getConstructor(parameterTypes);
				throw constructor.newInstance(args);
			} else {
				throw codeExceptionClazz.newInstance();
			}
		} catch (ReflectiveOperationException roe) {
			roe.printStackTrace();
			throw new SysException(roe.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		try{
		AgAssert.isTrue(1+2 == 2, 1, "sfsdf" );
		}catch(CodeException e){
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getCode());
			System.out.println(e.getDetailMsg());
			System.out.println(e.getMessage());
		}
		try {
			AgAssert.isTrue(1 + 2 == 2, NoLoginException.class);
		} catch (CodeException e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getCode());
			System.out.println(e.getDetailMsg());
			System.out.println(e.getMessage());
		} 
	}
	
//	public static void isImage(MultipartFile file) {
//		try {
//			InputStream inputStream = file.getInputStream();
//			byte[] data = new byte[10];
//			inputStream.read(data);
//			String fastParseFileType = ImageUtil.fastParseFileType(data);
//			if(fastParseFileType == null) {
//				throw new SysTipsException("不支持的文件类型上传.");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SysTipsException("文件类型判断失败.");
//		}
//	}
//
//	public static void isImageByList(MultipartFile[] files) {
//		if(com.rpframework.utils.CollectionUtils.isNotEmpty(files)) {
//			for (MultipartFile multipartFile : files) {
//				isImage(multipartFile);;
//			}
//		}
//	}
}
