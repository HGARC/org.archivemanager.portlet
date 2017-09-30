package org.archivemanager.portal.events;

import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.util.ExpandoBridgeIndexer;

/**
 * POJO class used to defines a columns to be created with expandos
 */
public class ExpandoColumnDefinition {
	/** 
	 * The name of the expando column. This is the value that will be used
	 * to update the column on the associated object (Page, User etc.) 
	 */
	private String name;
	
	/**
	 * This is the data type of the exapando column. The acceptable values
	 * are in the class {@link ExpandoColumnConstants}
	 */
	private int type;
	
	/**
	 * This attributes allow for specifying the default data that should be 
	 * used for an expando column. It also serves the purpose providing 
	 * selection options in the case of when the expando column's values is 
	 * selected from a selection dropdown box. The values in this scenario 
	 * should be comma delimited. Additionally, this column's data type needs
	 * to match the type attribute. This attribute is optional and can be null.
	 */
	private Object defaultData;
	
	/**
	 * This represents additional settings that can be assigned to the expando
	 * column. The setting determine behaviors such as whether the column will
	 * be indexable, whether it's hidden from view etc. The value specified 
	 * should be of the format: "settings1=X setting2=Y ..." . Name-value pairs
	 * are delimited spaces.
	 * 
	 * The values to be used for the settings key are specified as constants 
	 * in {@link ExpandoColumnConstants}.PROPERTY_XXX and {@link ExpandoBridgeIndexer} 
	 * 
	 * These settings should be used with caution. If in doubt, edit the 
	 * setting from within Liferay's Control Panel under Custom Fields. 
	 * This attribute is optional and can be null.
	 */
	private String typeSettings;
	
	/**
	 * Represents a collection role definitions that should be assigned to the 
	 * expando column. The role definition comprises of the role type 
	 * (Guest, User, Power User) as specified by the {@link RoleConstants}
	 * class and the associated actions that they can perform as defined 
	 * by the {@link ActionKeys} class.
	 */
	private ExpandoRoleDefinition[] roleDefinitions;
	
	
	public String getName() { 
		return name; 
	}
	
	public int getType() { 
		return type; 
	}
	
	public Object getDefaultData() { 
		return defaultData; 
	}
	
	public String getTypeSettings() { 
		return typeSettings; 
	}
	
	public ExpandoRoleDefinition[] getRoleDefinitions() {
		return roleDefinitions;
	}

	
	/**
	 * Creates and instance of the class. For more information about the input parameters
	 * please refer to the corresponding member variable JavaDoc comments.
	 * 
	 * @param name
	 * @param type
	 * @param defaultData
	 * @param typeSettings
	 * @param roleDefinitions
	 */
	public ExpandoColumnDefinition(String name, int type, 
			Object defaultData, String typeSettings,
			ExpandoRoleDefinition[] roleDefinitions) {
		this.name = name;
		this.type=type;
		this.defaultData = defaultData;
		this.typeSettings = typeSettings;
		this.roleDefinitions = roleDefinitions;
	}

}
