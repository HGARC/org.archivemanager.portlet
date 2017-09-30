package org.archivemanager.portal.listener;

import org.heed.openapps.SystemModel;
import org.heed.openapps.entity.Entity;
import org.heed.openapps.entity.EntityImpl;
import org.heed.openapps.entity.EntityQuery;
import org.heed.openapps.entity.EntityResultSet;
import org.heed.openapps.entity.EntityService;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.UserGroup;

public class UserGroupListener implements ModelListener<UserGroup> {
	private static Log log = LogFactoryUtil.getLog(UserGroupListener.class);
	private EntityService entityService;
	
	
	@Override
	@SuppressWarnings("unchecked")	
	public void onAfterCreate(UserGroup group) throws ModelListenerException {
		try {
			Entity roleEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.ROLE);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(group.getGroupId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(roleEntity == null) roleEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else roleEntity = userEntities.getResults().get(0);
			} else roleEntity = new EntityImpl(SystemModel.ROLE);
			if(roleEntity != null) {
				roleEntity.setXid(group.getGroupId());
				roleEntity.setName(group.getName());				
				if(roleEntity.getId() > 0) getEntityService().updateEntity(roleEntity);
				else getEntityService().addEntity(roleEntity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAfterRemove(UserGroup group) throws ModelListenerException {		
		try {
			log.info("User:"+group.getGroupId()+" removed");
			Entity roleEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.USER);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(group.getGroupId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(roleEntity == null) roleEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else roleEntity = userEntities.getResults().get(0);
			}
			if(roleEntity != null) {
				getEntityService().removeEntity(roleEntity.getQName(), roleEntity.getId());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAfterUpdate(UserGroup group) throws ModelListenerException {
		try {
			Entity roleEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.USER);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(group.getGroupId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(roleEntity == null) roleEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else roleEntity = userEntities.getResults().get(0);
			} else roleEntity = new EntityImpl(SystemModel.USER);
			if(roleEntity != null) {
				roleEntity.setXid(group.getGroupId());
				roleEntity.setName(group.getName());				
				if(roleEntity.getId() > 0) getEntityService().updateEntity(roleEntity);
				else getEntityService().addEntity(roleEntity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onAfterAddAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onAfterRemoveAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBeforeAddAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeCreate(UserGroup arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeRemove(UserGroup arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeRemoveAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeforeUpdate(UserGroup arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}

	public EntityService getEntityService() {
		if(entityService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null) entityService = (EntityService)locator.locate("entityService");
		}
		return entityService;
	}
}
