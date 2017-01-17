package nfc.serviceImpl;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Address;
import nfc.model.AttachFile;
import nfc.model.Category;
import nfc.model.Code;
import nfc.model.Product;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.model.SupplierFavorite;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.SupplierImage;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.User;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.ICategoryService;
import nfc.service.ICodeService;
import nfc.service.IFileService;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class SupplierService implements ISupplierService {
	/*@Autowired
	private ICommonService commonDAO;*/
	@Autowired 
	private IUserService userDAO;
	@Autowired
	private IRoleService roleDAO;
	@Autowired
	private ICodeService codeDAO;
	@Autowired
	private IFileService fileDAO;
	@Autowired
	private ICategoryService categoryDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Supplier> getListSupplier(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Supplier.class);
		List<Supplier> list = (List<Supplier>) criteria.list();
		trans.commit();
		return list;
	}
	public Supplier getSupplier(String supplId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Supplier.class);
		criteria.add(Restrictions.eq("suppl_id", Integer.parseInt(supplId)));
		Supplier supplier = (Supplier) criteria.uniqueResult();
		trans.commit();
		return supplier;
	}
	public SupplierView getSupplierView(int supplId) {
		SupplierView supplierView = new SupplierView();
		supplierView.setSupplier(getSupplier(supplId+""));
		supplierView.setSupplierWork(getSupplierWork(supplId));
		//get image of supplier
		List<SupplierImage> supplImgs = getListSupplierImage(supplId);
		List<AttachFile> supplAttachFiles = new ArrayList<AttachFile>();
		for(SupplierImage supImg: supplImgs)
		{
			supplAttachFiles.add(fileDAO.getAttachFile(supImg.getImg_id()));
		}
		supplierView.setImages(supplAttachFiles);
		//get categories of supplier
		List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
		List<Category> supplCategories = new ArrayList<Category>();
		for(SupplierCategories supCate: supplCates)
		{
			supplCategories.add(categoryDAO.getCategory(supCate.getCate_id()+""));
		}
		supplierView.setCategories(supplCategories);
		//get address of supplier
		List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
		List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
		for(SupplierAddress supplAddr: supplAddresses)
		{
			SupplierAddressView suppAddrView = new SupplierAddressView();
			suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
			suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
			suppAddrView.setIs_main(supplAddr.getIs_main());
			supplAddressViewLst.add(suppAddrView);
		}
		supplierView.setLstSupplAddressView(supplAddressViewLst);
		//get supplier manager
		//supplierView.setSupplierManage(getSupplier(supplId + ""));
		return supplierView;
	}
	public SupplierWork getSupplierWork(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierWork.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		SupplierWork supplierWork = (SupplierWork) criteria.uniqueResult();
		trans.commit();
		return supplierWork;
	}
	public List<SupplierImage> getListSupplierImage(int supplId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierImage.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierImage> list = (List<SupplierImage>) criteria.list();
		trans.commit();
		return list;
	}
	public List<SupplierCategories> getListSupplierCategory(int supplId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierCategories.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
		trans.commit();
		return list;
	}
	public List<SupplierAddress> getListSupplierAddress(int supplId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierAddress.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierAddress> list = (List<SupplierAddress>) criteria.list();
		trans.commit();
		return list;
	}
	public Address getAddress(int addrId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Address.class);
		criteria.add(Restrictions.eq("addr_id", addrId));
		Address role = (Address) criteria.uniqueResult();
		trans.commit();
		return role;
	}
	public boolean insertSupplierView(SupplierView supplierView, String username){
		User user = userDAO.findUserByUserName(username);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			//save supplier
			int supplIdDesc = 0;
			supplierView.getSupplier().setApp_id(Utils.appId);
			Serializable ser = session.save(supplierView.getSupplier());
	        if (ser != null) {
	        	supplIdDesc = (Integer) ser;
	        }
	        //save supplierWork
			supplierView.getSupplierWork().setSuppl_id(supplIdDesc);
			session.save(supplierView.getSupplierWork());
			
			//save supplier address
			for(SupplierAddressView addrView: supplierView.getLstSupplAddressView())
			{
				int addressIdDesc = 0;
				addrView.getAddressOfSuppl().setApp_id(Utils.appId);
				Serializable serAdd = session.save(addrView.getAddressOfSuppl());
		        if (serAdd != null) {
		        	addressIdDesc = (Integer) serAdd;
		        }
				session.save(addrView.getAddressOfSuppl());
				SupplierAddress supplAddr = new SupplierAddress();
				supplAddr.setAddr_id(addressIdDesc);
				supplAddr.setApp_id(Utils.appId);
				supplAddr.setSuppl_id(supplIdDesc);
				supplAddr.setIs_deliver(addrView.isIs_deliver());
				supplAddr.setIs_main(addrView.isIs_main());
				session.save(supplAddr);
			}
			//save supplier category
			insertSupplierCategory(supplierView.getCategories(),supplIdDesc,session);
			/*for(Category category: supplierView.getCategories())
			{
				SupplierCategories supplCate = new SupplierCategories();
				supplCate.setCate_id(category.getCate_id());
				supplCate.setCate_name(category.getCate_name());
				supplCate.setSuppl_id(supplIdDesc);
				session.save(supplCate);
			}*/
			//save supplier image
			insertSupplierImage(supplierView.getImages(),supplIdDesc, session);
			/*for(AttachFile attFile: supplierView.getImages())
			{
				SupplierImage supplImg = new SupplierImage();
				supplImg.setImg_id(attFile.getFile_id());
				supplImg.setImg_name(attFile.getFile_name());
				supplImg.setSuppl_id(supplIdDesc);
				session.save(supplImg);
			}*/
			
			//insert supplier user
			SupplierUser supplUser = new SupplierUser();
			supplUser.setApp_id(Utils.appId);
			supplUser.setSuppl_id(supplIdDesc);
			supplUser.setUser_id(user.getUser_id());
			session.save(supplUser);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		}
		finally{
			if(session.isOpen())
				session.close();
		}
	}
	private void insertSupplierCategory(List<Category> lstCategory, int supplIdDesc, Session session){
		for(Category category: lstCategory)
		{
			SupplierCategories supplCate = new SupplierCategories();
			supplCate.setCate_id(category.getCate_id());
			supplCate.setCate_name(category.getCate_name());
			supplCate.setSuppl_id(supplIdDesc);
			session.save(supplCate);
		}
	}
	private void insertSupplierImage(List<AttachFile> lstAttachFile, int supplIdDesc, Session session){
		for(AttachFile attFile: lstAttachFile)
		{
			SupplierImage supplImg = new SupplierImage();
			supplImg.setImg_id(attFile.getFile_id());
			supplImg.setImg_name(attFile.getFile_name());
			supplImg.setSuppl_id(supplIdDesc);
			session.save(supplImg);
		}
	}
	private void deleteReferenceOfSupplier(Session session, int supplId, String table)
	{
		String deleteQuery = "delete from "+table+" where suppl_id = " + supplId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public boolean updateSupplierView(SupplierView supplierView){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int supplId = supplierView.getSupplier().getSuppl_id();
			session.update(supplierView.getSupplier());
			session.update(supplierView.getSupplierWork());
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
			insertSupplierCategory(supplierView.getCategories(),supplId,session);
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
			insertSupplierImage(supplierView.getImages(),supplId, session);
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
			for(SupplierAddressView addrView: supplierView.getLstSupplAddressView())
			{
				int addressIdDesc = 0;
				if(addrView.getAddressOfSuppl().getAddr_id() == 0)
				{
					addrView.getAddressOfSuppl().setApp_id(Utils.appId);
					Serializable serAdd = session.save(addrView.getAddressOfSuppl());
			        if (serAdd != null) {
			        	addressIdDesc = (Integer) serAdd;
			        }
				}
				else
				{
					session.update(addrView.getAddressOfSuppl());
				}
				
				
				SupplierAddress supplAddr = new SupplierAddress();
				if(addressIdDesc == 0)
				{
					supplAddr.setAddr_id(addrView.getAddressOfSuppl().getAddr_id());
				}
				else
				{
					supplAddr.setAddr_id(addressIdDesc);
				}
				supplAddr.setApp_id(Utils.appId);
				supplAddr.setSuppl_id(supplId);
				supplAddr.setIs_deliver(addrView.isIs_deliver());
				supplAddr.setIs_main(addrView.isIs_main());
				session.save(supplAddr);
			}
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
	public boolean deleteSupplierView(int supplId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_users");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_work");
			deleteReferenceOfSupplier(session, supplId, "fg_suppliers");
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}
	public List<SupplierUser> getListSupplierUser(String username){
		User user = userDAO.findUserByUserName(username);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierUser.class);
		criteria.add(Restrictions.eq("user_id", user.getUser_id()));
		List<SupplierUser> list = (List<SupplierUser>) criteria.list();
		trans.commit();
		return list;
	}
	public List<SupplierView> getListSupplierView(String username) {
		// TODO Auto-generated method stub
		List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();		
		List<SupplierUser> supplierUsers = getListSupplierUser(username);
		System.out.println("list la: "+supplierUsers.size());
		for(SupplierUser supplUser: supplierUsers){
			lstSupplierView.add(getSupplierView(supplUser.getSuppl_id()));
		}
		return lstSupplierView;
	}
	@Override
	public Supplier getSupplierFromUser(String username) {
		List<SupplierUser> lstSupplierUser = getListSupplierUser(username);
		int supplierId = 0;
		if(lstSupplierUser.size() > 0)
		{
			supplierId = lstSupplierUser.get(0).getSuppl_id();
		}
		Supplier supplier = getSupplier(supplierId+"");
		return supplier;
	}
	public List<SupplierUser> getListSupplierUserId(String userId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierUser.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<SupplierUser> list = (List<SupplierUser>) criteria.list();
		trans.commit();
		return list;
	}
	private List<SupplierCategories> getListSupplierCategoryFromCategory(int categoryId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierCategories.class);
		criteria.add(Restrictions.eq("cate_id", categoryId));
		List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
		trans.commit();
		return list;
	}
	public List<SupplierAppView> getListSupplierViewOfCategory(int categoryId){
		List<SupplierAppView> lstSupplierAppView = new ArrayList<SupplierAppView>();
		List<SupplierCategories> lstSupplierCategory = getListSupplierCategoryFromCategory(categoryId);
		for(SupplierCategories supplierCategory: lstSupplierCategory){
			SupplierWork supplierWork = getSupplierWork(supplierCategory.getSuppl_id());
			if(Integer.parseInt(supplierWork.getSuppl_role()) == 21)
			{
				SupplierAppView supplierAppView = new SupplierAppView();
				supplierAppView.setSupplier(getSupplier(supplierCategory.getSuppl_id() + ""));
				supplierAppView.setSupplierWork(supplierWork);
				List<SupplierImage> supplImgs = getListSupplierImage(supplierCategory.getSuppl_id());
				List<AttachFile> supplAttachFiles = new ArrayList<AttachFile>();
				for(SupplierImage supImg: supplImgs)
				{
					supplAttachFiles.add(fileDAO.getAttachFile(supImg.getImg_id()));
				}
				supplierAppView.setImages(supplAttachFiles);
				lstSupplierAppView.add(supplierAppView);
			}
		}
		return lstSupplierAppView;
	}

	public String getSupplierFavorite(int supplierId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierFavorite.class);
		criteria.add(Restrictions.eq("suppl_id", supplierId));
		criteria.setProjection(Projections.rowCount());
		List rowCount = criteria.list();
		String favoCount = rowCount.get(0).toString();
		trans.commit();
		System.out.println(favoCount);
		return favoCount;
	}
	public List<SupplierView> getListSupplierView1(String username) {
		// TODO Auto-generated method stub
		List<SupplierView> lstSupplierView = new ArrayList<SupplierView>();		
		List<SupplierUser> supplierUsers = getListSupplierUser(username);
		
		System.out.println("list la: "+supplierUsers.size());
		for(SupplierUser supplUser: supplierUsers){
			lstSupplierView.add(getSupplierView(supplUser.getSuppl_id()));
		}
		return lstSupplierView;
	}
	
}
