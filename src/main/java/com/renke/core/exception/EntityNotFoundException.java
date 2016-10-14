package com.renke.core.exception;

public class EntityNotFoundException extends CodeException {
	final static String TIPS = "找不到实体";
	public EntityNotFoundException() {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, TIPS);
	}
	
	public EntityNotFoundException(Integer id) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, TIPS, "根据主键" + id + "找不到实体");
	}
	
	public EntityNotFoundException(Long id) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, TIPS,"根据主键" + id + "找不到实体");
	}
	
	public EntityNotFoundException(String id) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, TIPS,"根据主键" + id + "找不到实体");
	}
	
	
	public EntityNotFoundException(String msg, Long obj) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, msg, "根据主键" + obj + "找不到实体");
	}
	
	public EntityNotFoundException(String msg, Integer obj) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, msg, "根据主键" + obj + "找不到实体");
	}
	
	public EntityNotFoundException(String msg, String obj) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, msg, "根据主键" + obj + "找不到实体");
	}
	
	public EntityNotFoundException(Object obj) {
		super(CommonCode.ENTITY_NOTFOUND_EXCEPTION, "找不到实体", "根据主键" + obj + "找不到实体");
	}

	/**描述*/  
	private static final long serialVersionUID = 1L;
}
