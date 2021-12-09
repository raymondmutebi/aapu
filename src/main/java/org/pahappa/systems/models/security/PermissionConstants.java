package org.pahappa.systems.models.security;

public final class PermissionConstants {
	private PermissionConstants() {

	}
	
	@CustomPermission(name = "Manage members", description = "Has permission to manage members")
	public static final String PERM_MANAGE_MEMBERS = "Manage members";
	
	
	@CustomPermission(name = "Manage payments", description = "Has permission to manage payments")
	public static final String PERM_MANAGE_PAYMENTS = "Manage payments";
        
        
        @CustomPermission(name = "Manage communications", description = "Has permission to manage communications")
	public static final String PERM_MANAGE_COMMUNICATIONS = "Manage communications";
        
        @CustomPermission(name = "Normal member perm", description = "Has permission of normal member")
	public static final String PERM_MEMBER = "Normal member perm";
        
         @CustomPermission(name = "Manage datasets", description = "Has permission to manage datasets")
	public static final String PERM_MANAGE_DATASETS = "Manage datasets";
        
        
	
	
	
}
