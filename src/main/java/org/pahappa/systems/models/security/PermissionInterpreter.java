package org.pahappa.systems.models.security;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.sers.webutils.model.security.Permission;

public class PermissionInterpreter {

	public static final List<Permission> reflectivelyGetPermissions() {

		List<Permission> permissions = new ArrayList<Permission>();

		for (Field field : PermissionConstants.class.getFields()) {

			if (field.isAnnotationPresent(UMAPermission.class)) {
				UMAPermission permissionAnnotation = field.getAnnotation(UMAPermission.class);
				Permission permission = new Permission();
				permission.setName(permissionAnnotation.name());
				permission.setDescription(permissionAnnotation.description());
				permissions.add(permission);
			}
		}
		return permissions;
	}
}
