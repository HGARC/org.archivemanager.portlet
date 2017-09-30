package org.archivemanager.portal.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;


/**
 * This startup action is invoked whenever the server is being started.
 * It will attempt to create the Expando columns representing Custom
 * Fields in Liferay. These Expando columns allows us to extend the
 * capabilities of Liferay by adding additional column to the portal
 * objects such as Pages (Layout) which can in turn be examined to perform
 * specific logic based on the set values.
 * <br/>
 * Note: Expando columns that have been previously created will
 * be ignored.
 *
 */
public class StartupAction extends SimpleAction {
	private Log log = LogFactoryUtil.getLog(StartupAction.class);

	
	@Override
	public void run(String[] ids) throws ActionException {
		try {
			long companyId = GetterUtil.getLong(ids[0]);			
			createUserExpandos(companyId);
			createMBCategoryExpandos(companyId);
			createSubscriptionExpandos(companyId);			
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}
	
	private void createMBCategoryExpandos(long companyId) throws Exception {
		/*
		ExpandoColumnDefinition[] categoryColumnDefinitions = {
			new ExpandoColumnDefinition("Private", ExpandoColumnConstants.BOOLEAN, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			)
		};
		setExpandos(companyId, MBCategory.class.getName(), categoryColumnDefinitions);
		*/
	}
	private void createSubscriptionExpandos(long companyId) throws Exception {
		/*
		ExpandoColumnDefinition[] subscriptionColumnDefinitions = {
			new ExpandoColumnDefinition("Level", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			)
		};
		setExpandos(companyId, Subscription.class.getName(), subscriptionColumnDefinitions);
		*/
	}
	/**
	 * Creates Expandos for User (User)
	 *
	 * @param companyId is used to retrieve the appropriate role that will be 
	 * 				associated with the expando column being created.
	 * 
	 * @throws Exception if an error occurs while creating the expando column.
	 */
	private void createUserExpandos(long companyId) throws Exception {
		
		//Define a collection of ExpandoColumnDefinition objects. These
		//represent the name and the type of column to be created
		
		ExpandoColumnDefinition[] userColumnDefinitions = {
			new ExpandoColumnDefinition("api_key", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			)
			/*
			new ExpandoColumnDefinition("State", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			),
			new ExpandoColumnDefinition("Message_Update_Frequency", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
							RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			),
			
			new ExpandoColumnDefinition("District", ExpandoColumnConstants.STRING, null, null,
					new ExpandoRoleDefinition[]{
						new ExpandoRoleDefinition(
							RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
						new ExpandoRoleDefinition(
								RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
					}
				),
			new ExpandoColumnDefinition("Geographic_Area", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
							RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			),
			new ExpandoColumnDefinition("District_Size", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			),
			new ExpandoColumnDefinition("GFOA_Member", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			),
			new ExpandoColumnDefinition("Population_Description", ExpandoColumnConstants.STRING, null, null,
					new ExpandoRoleDefinition[]{
						new ExpandoRoleDefinition(
							RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
						new ExpandoRoleDefinition(
							RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
					}
				),
			new ExpandoColumnDefinition("Membership_Number", ExpandoColumnConstants.STRING, null, null,
				new ExpandoRoleDefinition[]{
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST), new String[] { ActionKeys.VIEW, ActionKeys.UPDATE }),
					new ExpandoRoleDefinition(
						RoleLocalServiceUtil.getRole(companyId, RoleConstants.POWER_USER), new String[] { ActionKeys.UPDATE })
				}
			)
			*/
		};
		setExpandos(companyId, User.class.getName(), userColumnDefinitions);		
	}

	/**
	 * Utility class to create expando definitions based on the input parameters.
	 *
	 * @param companyId the associated company
	 * @param columnDefinitions a list of columns that need to be created
	 * @param roleDefinitions a list of roles that need to be associated with the
	 * 			specified columns
	 * 
	 * @throws Exception is an unexpected error occurs
	 */
	private void setExpandos(long companyId, String className, ExpandoColumnDefinition[]  columnDefinitions) throws Exception {
		ExpandoTable expandoTable = null;
		// get the expando table definition
		try {
			expandoTable = ExpandoTableLocalServiceUtil.addTable(
					companyId, className,
					ExpandoTableConstants.DEFAULT_TABLE_NAME);
		}
		catch (DuplicateTableNameException dtne) {
			log.info("Table corresponding to " + className + " already exists, getting existing table info.");
			expandoTable = ExpandoTableLocalServiceUtil.getTable(
					companyId, className,
					ExpandoTableConstants.DEFAULT_TABLE_NAME);
		}
		// do the expando column creation magic...
		for (ExpandoColumnDefinition columnDefinition : columnDefinitions){
			try {
				ExpandoColumn newCol = null;
				Object defaultData = columnDefinition.getDefaultData();
				try {
					// add column
					newCol = ExpandoColumnLocalServiceUtil.addColumn(
							expandoTable.getTableId(),
							columnDefinition.getName(),
							columnDefinition.getType());
					//Set the default data, if available. Performing a seperate update here because
					//of a liferay bug, LP-15070
					if (defaultData != null) {
						ExpandoColumnLocalServiceUtil.updateColumn(
								newCol.getColumnId(), newCol.getName(), newCol.getType(), defaultData);
					}
					//Set the type settings, if available
					updateExpandoColumnProperties(companyId, className, newCol, columnDefinition);
				}
				catch (DuplicateColumnNameException dcne) {
					log.info(columnDefinition.getName() + " already exists, getting existing column info.");
					newCol = ExpandoColumnLocalServiceUtil.getColumn(
							companyId, className, ExpandoTableConstants.DEFAULT_TABLE_NAME,
							columnDefinition.getName());
					//Update the existing column
					if (defaultData != null) {
						ExpandoColumnLocalServiceUtil.updateColumn(
								newCol.getColumnId(), newCol.getName(), newCol.getType(), defaultData);
					}
				}
				// add permissions for each role definition associated with the expando column 
				//definition
				ExpandoRoleDefinition[] roleDefinitions = columnDefinition.getRoleDefinitions();

				//Iterate through list of role definitions, if no role definitions are specified
				//the default permissions will be automatically assigned by Liferay
				for(ExpandoRoleDefinition roleDefinition : roleDefinitions) {
					ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId,
							ExpandoColumn.class.getName(),
							ResourceConstants.SCOPE_INDIVIDUAL,
							Long.toString(newCol.getColumnId()),
							roleDefinition.getRole().getRoleId(),
							roleDefinition.getActionKeys());
				}
			}
			catch (Exception e) {
				log.warn("Error occured while trying to create expando for column named: " +
						columnDefinition.getName(), e);
			}
		}
	}
	
	
	/**
	 * Invoked to update the {@link ExpandoColumn}'s attributes which translates to the
	 * typeSettings string of the {@link ExpandoColumn}. The attributes dictate
	 * attributes such as if the column is searchable or not.  
	 *   
	 * @param expandoBridge the {@link ExpandoBridge} object representing the 
	 * 			{@link ExpandoColumn}.
	 * @param name the name of the {@link ExpandoColumn}
	 * @param typeSettings a {@link String} representing the {@link ExpandoColumn}'s
	 * 			attributes. The field is of the format: "name1=value1 name2=value2 ...."
	 */
	private void updateExpandoColumnProperties(long companyId, String className, ExpandoColumn newCol,
		ExpandoColumnDefinition columnDefinition) throws Exception {
		String typeSettings = columnDefinition.getTypeSettings();
		if (typeSettings != null && (typeSettings.trim().length() > 0)) {
			//Get the ExpandoBridge representing the current ExpandoColumn
			ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
				companyId, className, newCol.getColumnId());
			//Construct UnicodeProperties object based on the expando bridge 
			//representing the ExpandoColumn
			UnicodeProperties properties = expandoBridge.getAttributeProperties(newCol.getName());			
			//Fetch the property pairs, tokenized by spaced in the 
			//input typeSettings string
			String[] propertyPairs = typeSettings.trim().split(" ");
			if (propertyPairs != null && propertyPairs.length > 0) {
				//First clear the existing type settings from the map
				properties.clear();
				
				//For each property pair, add to the UnicodeProperties object 
				//(property pair is of format name=value)
				for (String propertyPair : propertyPairs) {
					String[] nameValue = propertyPair.split("=");
					properties.put(nameValue[0], nameValue[1]);
				}
			}
			ExpandoColumnLocalServiceUtil.updateTypeSettings(
					expandoBridge.getClassPK(), properties.toString());
		}
	}

}
