package org.pahappa.systems.constants;

import org.apache.commons.lang.StringUtils;

public enum TemplateType {
	/**
	 * 
	 */
	RESET_PASSWORD("Reset Password"),
	
	CHURCH_REGISTRATION("Church Registration"),
        
        AUTHOR_REGISTRATION("Author Registration"),
	
	CHURCH_APPROVAL("Church Approval"),
	
	CHURCH_REJECTION("Church Rejection"),
	
	TOKEN_RESEND("Token Resend"),
	
	USERACCOUNT_REGISTRATION("UserAccount Registration");

	private String name;

	TemplateType(String name) {
		this.name = name;
	}

	public static final TemplateType getEnumObject(String value) {
		if (StringUtils.isBlank(value))
			return null;
		for (TemplateType object : TemplateType.values()) {
			if (object.getName().equals(value))
				return object;
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
};
