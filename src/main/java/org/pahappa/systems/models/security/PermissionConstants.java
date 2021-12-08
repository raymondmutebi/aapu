package org.pahappa.systems.models.security;

public final class PermissionConstants {
	private PermissionConstants() {

	}
	
	@UMAPermission(name = "Manage Contacts", description = "Has permission to manage contacts")
	public static final String PERM_MANAGE_COMPANIES = "Manage Companies";
	
	
	@UMAPermission(name = "View Dashboard", description = "Has permission to view dashboard")
	public static final String PERM_VIEW_DASHBOARD = "View Dashboard";
	
	
	
}
