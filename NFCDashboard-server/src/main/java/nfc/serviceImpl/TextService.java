/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nfc.model.Text;
import nfc.model.User;
import nfc.model.ViewModel.TextView;
import nfc.service.ICodeService;
import nfc.service.ITextService;
import nfc.service.IUserService;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 *
 */
public class TextService implements ITextService {

    @Autowired
    private IUserService userDAO;
    @Autowired
    private ICodeService codeDAO;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Text> getAllText() {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Text> list = new ArrayList<>();
        try {
            Criteria criteria = session.createCriteria(Text.class);
            list = (List<Text>) criteria.list();
            trans.commit();
        } catch (HibernateException ex) {
            trans.rollback();
        }
        return list;
    }

    @Override
    public List<Text> getListText() {
        List<Text> lstText = getAllText();
        System.out.println("VAO lstText:" + lstText);
        for (Text text : lstText) {
            text.setText_createdUser(userDAO.getUser(text.getText_createdUser()).getUser_name());
            text.setText_type(codeDAO.getCode("0006", text.getText_type()).getCode_name());
            System.out.println("Value listtext:" + lstText);
        }
        return lstText;
    }

    @Override
    public List<Text> getListTextByType(String text_type) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Text.class);
        criteria.add(Restrictions.eq("text_type", text_type));
        List<Text> list = (List<Text>) criteria.list();
        trans.commit();
        for (Text text : list) {
            text.setText_createdUser(userDAO.getUser(text.getText_createdUser()).getUser_name());
            text.setText_type(codeDAO.getCode("0006", text.getText_type()).getCode_name());
        }
        return list;
    }

    @Override
    public Text getTextbyId(int text_id) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Text.class);
        criteria.add(Restrictions.eq("text_id", text_id));
        Text text = (Text) criteria.uniqueResult();
        trans.commit();
        return text;
    }

    @Override
    public boolean updateText(Text text) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            text.setText_updatedDate(new Date());
            session.update(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    @Override
    public boolean insertText(Text text) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            text.setText_createdDate(new Date());
            session.save(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean deleteText(int text_id) {
        Text text = getTextbyId(text_id);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.delete(text);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

}
