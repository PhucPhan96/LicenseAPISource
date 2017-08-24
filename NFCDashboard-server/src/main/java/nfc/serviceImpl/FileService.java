package nfc.serviceImpl;


import java.io.File;
import javax.servlet.ServletContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import nfc.model.AttachFile;
import nfc.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;

public class FileService implements IFileService{
        @Autowired
        ServletContext context; 
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
	public AttachFile getAttachFileWithSession(int fileId,Session session) {
		session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AttachFile.class);
		criteria.add(Restrictions.eq("file_id", fileId));
		AttachFile attachFile = (AttachFile) criteria.uniqueResult();
		return attachFile;
	}
        
        public boolean deleteFile(String url){
            try{
    		File file = new File(context.getRealPath("") + url);
    		if(file.delete()){
                    return true;
    		}
                return false;

            }catch(Exception ex){
                System.err.println("Error" + ex.getMessage());
                return false;
            }
        }

}
