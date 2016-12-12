package nfc.serviceImpl;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import nfc.model.AttachFile;
import nfc.service.IFileService;

public class FileService implements IFileService{
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public boolean saveFile(AttachFile file) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(file);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		}
	}
	public AttachFile getAttachFile(int fileId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(AttachFile.class);
		criteria.add(Restrictions.eq("file_id", fileId));
		AttachFile attachFile = (AttachFile) criteria.uniqueResult();
		trans.commit();
		return attachFile;
	}

}
