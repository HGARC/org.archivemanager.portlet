package org.archivemanager.portal.events;

import com.liferay.portal.model.Role;

/**
 * POJO class used to define a collection of Roles and the corresponding actions that can 
 * be performed on them. This class is used to create expando content.
 */
public class ExpandoRoleDefinition {
	Role role;
	String[] actionKeys;
	
	public Role getRole() {
		return role;
	}

	public String[] getActionKeys() {
		return actionKeys;
	}
	
	/** 
	 * Creates an instance of the class using the input variables
	 * 
	 * @param role the {@link Role} object associated with the class
	 * @param actionKeys  the list of actions that can be performed by the role
	 */
	public ExpandoRoleDefinition(Role role, String[] actionKeys)
	{
		this.role = role;
		this.actionKeys = actionKeys;
	}
}
