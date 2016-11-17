package com.renke.core.annotations;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.renke.core.enums.Order;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	public String colName() default "";
	public Order order() default Order.NONE;
//	public MysqlType sqlType() default MysqlType.UNKNOWN;
	public boolean unColumn() default false;
	public boolean isPrimary() default false;
}
