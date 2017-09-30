package org.archivemanager.portal.listener;

import com.liferay.portal.ModelListenerException;
import com.liferay.portal.model.ModelListener;
import com.liferay.portlet.journal.model.JournalArticle;

public class JournalArticleListener implements ModelListener<JournalArticle> {

	

	@Override
	public void onAfterCreate(JournalArticle arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAfterRemove(JournalArticle arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAfterUpdate(JournalArticle article) throws ModelListenerException {
		System.out.println("Journal article:"+article.getArticleId()+" updated");
	}
	@Override
	public void onBeforeCreate(JournalArticle arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBeforeRemove(JournalArticle arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBeforeUpdate(JournalArticle arg0) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onAfterAddAssociation(Object arg0, String arg1, Object arg2)throws ModelListenerException {
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
	public void onBeforeRemoveAssociation(Object arg0, String arg1, Object arg2) throws ModelListenerException {
		// TODO Auto-generated method stub
		
	}
}
