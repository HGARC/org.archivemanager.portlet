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
import com.liferay.portal.model.User;


public class UserListener implements ModelListener<User> {
	private static Log log = LogFactoryUtil.getLog(UserListener.class);
	private EntityService entityService;
		
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAfterCreate(User user) throws ModelListenerException {
		try {
			Entity userEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.USER);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(user.getUserId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(userEntity == null) userEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else userEntity = userEntities.getResults().get(0);
			} else userEntity = new EntityImpl(SystemModel.USER);
			if(userEntity != null) {
				userEntity.setName(user.getScreenName());
				userEntity.addProperty(SystemModel.FIRSTNAME, user.getFirstName());
				userEntity.addProperty(SystemModel.LASTNAME, user.getLastName());
				userEntity.addProperty(SystemModel.EMAIL, user.getEmailAddress());
				
				if(userEntity.getId() > 0) getEntityService().updateEntity(userEntity);
				else getEntityService().addEntity(userEntity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onAfterRemove(User user) throws ModelListenerException {
		log.info("User:"+user.getUserId()+" removed");
		try {
			Entity userEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.USER);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(user.getUserId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(userEntity == null) userEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else userEntity = userEntities.getResults().get(0);
			}
			if(userEntity != null) {
				getEntityService().removeEntity(userEntity.getQName(), userEntity.getId());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onAfterUpdate(User user) throws ModelListenerException {
		//getLoggingService().info("User:"+user.getUserId()+" updated");
		addUpdateUser(user);
	}
	@SuppressWarnings("unchecked")
	protected void addUpdateUser(User user) {
		try {
			Entity userEntity = null;
			EntityQuery query = new EntityQuery(SystemModel.USER);
			query.setType(EntityQuery.TYPE_LUCENE_EXACT);
			query.setXid(user.getUserId());
			EntityResultSet<Entity> userEntities = getEntityService().search(query);
			if(userEntities != null && userEntities.getResults().size() > 0) {
				if(userEntities.getResults().size() > 1) {
					for(Entity u : userEntities.getResults()) {
						if(userEntity == null) userEntity = u;
						else getEntityService().removeEntity(u.getQName(), u.getId());
					}
				} else userEntity = userEntities.getResults().get(0);
			} else userEntity = new EntityImpl(SystemModel.USER);
			if(userEntity != null) {
				userEntity.setName(user.getScreenName());
				userEntity.addProperty(SystemModel.FIRSTNAME, user.getFirstName());
				userEntity.addProperty(SystemModel.LASTNAME, user.getLastName());
				userEntity.addProperty(SystemModel.EMAIL, user.getEmailAddress());
				
				if(userEntity.getId() > 0) getEntityService().updateEntity(userEntity);
				else getEntityService().addEntity(userEntity);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public EntityService getEntityService() {
		if(entityService == null) {
			BeanLocator locator = PortletBeanLocatorUtil.getBeanLocator("archivemanager-portlet");
			if(locator != null) entityService = (EntityService)locator.locate("entityService");
		}
		return entityService;
	}
	
	@Override
	public void onBeforeCreate(User arg0) throws ModelListenerException {}
	@Override
	public void onBeforeRemove(User arg0) throws ModelListenerException {}
	@Override
	public void onBeforeUpdate(User arg0) throws ModelListenerException {}	
	@Override
	public void onAfterAddAssociation(Object arg0, String arg1, Object arg2)throws ModelListenerException {}
	@Override
	public void onAfterRemoveAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {}
	@Override
	public void onBeforeAddAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {}
	@Override
	public void onBeforeRemoveAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {}
	
}
