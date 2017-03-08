package nfc.serviceImpl;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Address;
import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.model.Category;
import nfc.model.Code;
import nfc.model.Order;
import nfc.model.OrderDetail;
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
import nfc.model.ViewModel.BillHistory;
import nfc.model.ViewModel.ProductAttachFileView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.IBoardService;
import nfc.service.ICategoryService;
import nfc.service.ICodeService;
import nfc.service.IFileService;
import nfc.service.IOrderService;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.Util;

import nfc.model.ViewModel.SupplierAttachFileView;

public class SupplierService implements ISupplierService {
	/*
	 * @Autowired private ICommonService commonDAO;
	 */
	@Autowired
	private IUserService userDAO;
	@Autowired
	private IRoleService roleDAO;
	@Autowired
	private ICodeService codeDAO;
	@Autowired
	private IFileService fileDAO;
	@Autowired
	private IOrderService orderDAO;
	@Autowired
	private ICategoryService categoryDAO;
	@Autowired
	private IBoardService boardDAO;

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Supplier> getListSupplier() {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Supplier.class);
		List<Supplier> list = (List<Supplier>) criteria.list();
		trans.commit();
		return list;
	}

	public Supplier getSupplier(String supplId) {
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
		supplierView.setSupplier(getSupplier(supplId + ""));
		supplierView.setSupplierWork(getSupplierWork(supplId));
		// get image of supplier
		List<SupplierImage> supplImgs = getListSupplierImage(supplId);
		List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
		for (SupplierImage supImg : supplImgs) {
			SupplierAttachFileView supplierAttachView = new SupplierAttachFileView();
			supplierAttachView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
			supplierAttachView.setImageType(supImg.getImg_type());
			supplAttachFiles.add(supplierAttachView);
		}
		supplierView.setImages(supplAttachFiles);
		// get categories of supplier
		List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
		List<Category> supplCategories = new ArrayList<Category>();
		for (SupplierCategories supCate : supplCates) {
			supplCategories.add(categoryDAO.getCategory(supCate.getCate_id() + ""));
		}
		supplierView.setCategories(supplCategories);
		// get address of supplier
		List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
		List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
		for (SupplierAddress supplAddr : supplAddresses) {
			SupplierAddressView suppAddrView = new SupplierAddressView();
			suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
			suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
			suppAddrView.setIs_main(supplAddr.getIs_main());
			supplAddressViewLst.add(suppAddrView);
		}
		supplierView.setLstSupplAddressView(supplAddressViewLst);
		// get supplier manager
		// supplierView.setSupplierManage(getSupplier(supplId + ""));
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

	public List<SupplierImage> getListSupplierImage(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierImage.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierImage> list = (List<SupplierImage>) criteria.list();
		trans.commit();
		return list;
	}

	public List<SupplierCategories> getListSupplierCategory(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierCategories.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
		trans.commit();
		return list;
	}

	public List<SupplierAddress> getListSupplierAddress(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierAddress.class);
		criteria.add(Restrictions.eq("suppl_id", supplId));
		List<SupplierAddress> list = (List<SupplierAddress>) criteria.list();
		trans.commit();
		return list;
	}

	public Address getAddress(int addrId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Address.class);
		criteria.add(Restrictions.eq("addr_id", addrId));
		Address role = (Address) criteria.uniqueResult();
		trans.commit();
		return role;
	}

	public int insertBoard(String supplierName, String ownerId) {
		Board board = new Board();
		board.setBoard_name(supplierName);
		board.setCreated_date(new Date());
		board.setApp_id(Utils.appId);
		board.setOwner_id(ownerId);
		boardDAO.insertBoard(board);
		return board.getBoard_id();
	}

	public boolean insertSupplierView(SupplierView supplierView, String username) {
		int boardIdDesc = 0;
		User user = userDAO.findUserByUserName(username);
		boardIdDesc = insertBoard(supplierView.getSupplier().getSupplier_name(), user.getUser_id());
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			// save supplier
			int supplIdDesc = 0;
			supplierView.getSupplier().setApp_id(Utils.appId);
			Serializable ser = session.save(supplierView.getSupplier());
			if (ser != null) {
				supplIdDesc = (Integer) ser;
			}
			// save supplierWork
			supplierView.getSupplierWork().setSuppl_id(supplIdDesc);
			supplierView.getSupplierWork().setBoard_id(boardIdDesc);
			session.save(supplierView.getSupplierWork());

			// save supplier address
			for (SupplierAddressView addrView : supplierView.getLstSupplAddressView()) {
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
			// save supplier category
			insertSupplierCategory(supplierView.getCategories(), supplIdDesc, session);
			/*
			 * for(Category category: supplierView.getCategories()) {
			 * SupplierCategories supplCate = new SupplierCategories();
			 * supplCate.setCate_id(category.getCate_id());
			 * supplCate.setCate_name(category.getCate_name());
			 * supplCate.setSuppl_id(supplIdDesc); session.save(supplCate); }
			 */
			// save supplier image
			insertSupplierImage(supplierView.getImages(), supplIdDesc, session);
			/*
			 * for(AttachFile attFile: supplierView.getImages()) { SupplierImage
			 * supplImg = new SupplierImage();
			 * supplImg.setImg_id(attFile.getFile_id());
			 * supplImg.setImg_name(attFile.getFile_name());
			 * supplImg.setSuppl_id(supplIdDesc); session.save(supplImg); }
			 */

			// insert supplier user
			/*
			 * SupplierUser supplUser = new SupplierUser();
			 * supplUser.setApp_id(Utils.appId);
			 * supplUser.setSuppl_id(supplIdDesc);
			 * supplUser.setUser_id(user.getUser_id()); session.save(supplUser);
			 * 
			 */
			trans.commit();
			return true;
		} catch (Exception ex) {
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		} finally {
			if (session.isOpen())
				session.close();
		}
	}

	private void insertSupplierCategory(List<Category> lstCategory, int supplIdDesc, Session session) {
		for (Category category : lstCategory) {
			SupplierCategories supplCate = new SupplierCategories();
			supplCate.setCate_id(category.getCate_id());
			supplCate.setCate_name(category.getCate_name());
			supplCate.setSuppl_id(supplIdDesc);
			session.save(supplCate);
		}
	}

	private void insertSupplierImage(List<SupplierAttachFileView> lstAttachFile, int supplIdDesc, Session session) {
		for (SupplierAttachFileView attFile : lstAttachFile) {
			SupplierImage supplImg = new SupplierImage();
			supplImg.setImg_id(attFile.getAttachFile().getFile_id());
			supplImg.setImg_name(attFile.getAttachFile().getFile_name());
			supplImg.setImg_type(attFile.getImageType());
			supplImg.setSuppl_id(supplIdDesc);
			session.save(supplImg);
		}
	}

	private void deleteReferenceOfSupplier(Session session, int supplId, String table) {
		String deleteQuery = "delete from " + table + " where suppl_id = " + supplId;
		Query query = session.createSQLQuery(deleteQuery);
		query.executeUpdate();
	}

	public boolean updateSupplierView(SupplierView supplierView) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			int supplId = supplierView.getSupplier().getSuppl_id();
			session.update(supplierView.getSupplier());
			session.update(supplierView.getSupplierWork());
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
			insertSupplierCategory(supplierView.getCategories(), supplId, session);
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
			insertSupplierImage(supplierView.getImages(), supplId, session);
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
			for (SupplierAddressView addrView : supplierView.getLstSupplAddressView()) {
				int addressIdDesc = 0;
				if (addrView.getAddressOfSuppl().getAddr_id() == 0) {
					addrView.getAddressOfSuppl().setApp_id(Utils.appId);
					Serializable serAdd = session.save(addrView.getAddressOfSuppl());
					if (serAdd != null) {
						addressIdDesc = (Integer) serAdd;
					}
				} else {
					session.update(addrView.getAddressOfSuppl());
				}

				SupplierAddress supplAddr = new SupplierAddress();
				if (addressIdDesc == 0) {
					supplAddr.setAddr_id(addrView.getAddressOfSuppl().getAddr_id());
				} else {
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
		} catch (Exception ex) {
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		}
	}

	private void deleteReferenceOfOder(Session session, int orderId, String table) {
		String deleteQuery = "delete from " + table + " where order_id = " + orderId;
		Query query = session.createSQLQuery(deleteQuery);
		query.executeUpdate();
	}

	public boolean deleteSupplierView(int supplId, String username) {
		User user = userDAO.findUserByUserName(username);
		List<Order> lstOrder = orderDAO.getListOrder(supplId);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			/*
			 * deleteReferenceOfSupplierFavorite(session, supplId,
			 * user.getUser_id(), "fg_favorite_suppliers"); for(Order order:
			 * lstOrder){ deleteReferenceOfOder(session, order.getOrder_id(),
			 * "fg_order_details"); }
			 */
			// deleteReferenceOfSupplier(session, supplId, "fg_orders");
			// deleteReferenceOfSupplier(session, supplId, "fg_products");
			deleteReferenceOfSupplier(session, supplId, "fg_favorite_suppliers");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_categories");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_imgs");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_address");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_users");
			deleteReferenceOfSupplier(session, supplId, "fg_supplier_work");
			deleteReferenceOfSupplier(session, supplId, "fg_suppliers");

			trans.commit();
			return true;
		} catch (Exception ex) {
			trans.rollback();
			return false;
		}
	}

	private User getDirectorSuplier(int supplierId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"SELECT p.* FROM fg_users p INNER JOIN fg_supplier_users pc ON p.user_id = pc.user_id WHERE is_director = 1 and pc.suppl_id = "
						+ supplierId)
				.addEntity(User.class);
		User result = (User) query.uniqueResult();
		trans.commit();
		return result;
	}

	public List<SupplierUser> getListSupplierUser(String username) {
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
		if (username.equals("sysadmin")) {
			Session session = this.sessionFactory.getCurrentSession();
			Transaction trans = session.beginTransaction();
			Query query = session.createSQLQuery("CALL GetListSupplierWorkTree(:managerId)")
					.addEntity(SupplierWork.class).setParameter("managerId", 0);
			List<SupplierWork> result = query.list();
			trans.commit();
			for (SupplierWork supplWork : result) {
				SupplierView supplView = getSupplierView(supplWork.getSuppl_id());
				supplView.setDirector(getDirectorSuplier(supplWork.getSuppl_id()));
				supplView.setCode(codeDAO.getCode("0001", supplWork.getSuppl_rank()));
				lstSupplierView.add(supplView);
			}
		} else {
			if (supplierUsers.size() > 0) {
				for (SupplierUser suppUse : supplierUsers) {
					Session session = this.sessionFactory.getCurrentSession();
					Transaction trans = session.beginTransaction();
					Query query = session.createSQLQuery("CALL GetListSupplierWorkTree(:managerId)")
							.addEntity(SupplierWork.class).setParameter("managerId", suppUse.getSuppl_id());
					List<SupplierWork> result = query.list();
					trans.commit();
					for (SupplierWork supplWork : result) {
						SupplierView supplView = getSupplierView(supplWork.getSuppl_id());
						supplView.setDirector(getDirectorSuplier(supplWork.getSuppl_id()));
						supplView.setCode(codeDAO.getCode("0001", supplWork.getSuppl_rank()));
						lstSupplierView.add(supplView);
					}
				}

			}
		}

		return lstSupplierView;
	}

	public Supplier getSupplierFromUser(String username) {
		List<SupplierUser> lstSupplierUser = getListSupplierUser(username);
		int supplierId = 0;
		if (lstSupplierUser.size() > 0) {
			supplierId = lstSupplierUser.get(0).getSuppl_id();
		}
		Supplier supplier = getSupplier(supplierId + "");
		return supplier;
	}

	public List<SupplierUser> getListSupplierUserId(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierUser.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<SupplierUser> list = (List<SupplierUser>) criteria.list();
		trans.commit();
		return list;
	}

	private List<SupplierCategories> getListSupplierCategoryFromCategory(int categoryId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierCategories.class);
		criteria.add(Restrictions.eq("cate_id", categoryId));
		List<SupplierCategories> list = (List<SupplierCategories>) criteria.list();
		trans.commit();
		return list;
	}

	public List<SupplierAppView> getListSupplierViewOfCategory(int categoryId) {
		List<SupplierAppView> lstSupplierAppView = new ArrayList<SupplierAppView>();
		List<SupplierCategories> lstSupplierCategory = getListSupplierCategoryFromCategory(categoryId);
		for (SupplierCategories supplierCategory : lstSupplierCategory) {
			SupplierWork supplierWork = getSupplierWork(supplierCategory.getSuppl_id());
			if (Integer.parseInt(supplierWork.getSuppl_role()) == 21) {
				SupplierAppView supplierAppView = new SupplierAppView();
				supplierAppView.setSupplier(getSupplier(supplierCategory.getSuppl_id() + ""));
				supplierAppView.setSupplierWork(supplierWork);
				List<SupplierImage> supplImgs = getListSupplierImage(supplierCategory.getSuppl_id());
				List<AttachFile> supplAttachFiles = new ArrayList<AttachFile>();
				for (SupplierImage supImg : supplImgs) {
					supplAttachFiles.add(fileDAO.getAttachFile(supImg.getImg_id()));
				}
				supplierAppView.setImages(supplAttachFiles);
				lstSupplierAppView.add(supplierAppView);
			}
		}
		return lstSupplierAppView;
	}

	public String getSupplierFavorite(int supplierId) {
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

		System.out.println("list la: " + supplierUsers.size());
		for (SupplierUser supplUser : supplierUsers) {
			lstSupplierView.add(getSupplierView(supplUser.getSuppl_id()));
		}
		return lstSupplierView;
	}

	public SupplierFavorite isSupplierFavorite(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierFavorite.class);
		criteria.add(Restrictions.eq("user_id", userId));
		SupplierFavorite supplierFavorite = (SupplierFavorite) criteria.uniqueResult();
		trans.commit();
		return supplierFavorite;
	}

	public boolean insertSupplierFavorite(int supplId, String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			SupplierFavorite supplFavo = new SupplierFavorite();
			supplFavo.setApp_id("e6271952-d4b9-4ed3-b83b-63a56d47a713");
			supplFavo.setSuppl_id(supplId);
			supplFavo.setUser_id(userId);
			session.save(supplFavo);
			trans.commit();
			return true;
		} catch (Exception ex) {
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		} finally {
			if (session.isOpen())
				session.close();
		}
	}

	private void deleteReferenceOfSupplierFavorite(Session session, int supplId, String userId, String table) {
		String deleteQuery = "delete from " + table + " where suppl_id = " + "'" + supplId + "'" + " and user_id = "
				+ "'" + userId + "'";
		Query query = session.createSQLQuery(deleteQuery);
		query.executeUpdate();
	}

	public boolean deleteSupplierFavorite(int supplId, String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			deleteReferenceOfSupplierFavorite(session, supplId, userId, "fg_favorite_suppliers");
			trans.commit();
			return true;
		} catch (Exception ex) {
			trans.rollback();
			return false;
		}
	}

	public SupplierView getSupplierView2(int supplId) {
		System.out.println("run toi day 2");
		SupplierView supplierView = new SupplierView();
		supplierView.setSupplier(getSupplier(supplId + ""));
		supplierView.setSupplierWork(getSupplierWork(supplId));
		// get image of supplier
		List<SupplierImage> supplImgs = getListSupplierImage(supplId);
		List<SupplierAttachFileView> supplAttachFiles = new ArrayList<SupplierAttachFileView>();
		for (SupplierImage supImg : supplImgs) {
			SupplierAttachFileView supAttachView = new SupplierAttachFileView();
			supAttachView.setImageType(supImg.getImg_type());
			supAttachView.setAttachFile(fileDAO.getAttachFile(supImg.getImg_id()));
			supplAttachFiles.add(supAttachView);

		}
		supplierView.setLstAttachFileView(supplAttachFiles);
		// get categories of supplier
		List<SupplierCategories> supplCates = getListSupplierCategory(supplId);
		List<Category> supplCategories = new ArrayList<Category>();
		for (SupplierCategories supCate : supplCates) {
			supplCategories.add(categoryDAO.getCategory(supCate.getCate_id() + ""));
		}
		supplierView.setCategories(supplCategories);
		// get address of supplier
		List<SupplierAddress> supplAddresses = getListSupplierAddress(supplId);
		List<SupplierAddressView> supplAddressViewLst = new ArrayList<SupplierAddressView>();
		for (SupplierAddress supplAddr : supplAddresses) {
			SupplierAddressView suppAddrView = new SupplierAddressView();
			suppAddrView.setAddressOfSuppl(getAddress(supplAddr.getAddr_id()));
			suppAddrView.setIs_deliver(supplAddr.getIs_deliver());
			suppAddrView.setIs_main(supplAddr.getIs_main());
			supplAddressViewLst.add(suppAddrView);
		}
		supplierView.setLstSupplAddressView(supplAddressViewLst);
		// get supplier manager
		// supplierView.setSupplierManage(getSupplier(supplId + ""));
		return supplierView;
	}

	public SupplierUser checkUserIsStore(String username) {
		User user = userDAO.findUserByUserName(username);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"select su.* from fg_supplier_users su inner join fg_user_roles ur on su.user_id=ur.user_id inner join fg_roles r on ur.role_id=r.role_id where r.role_name='Store' and su.user_id='"
						+ user.getUser_id() + "' limit 1 ")
				.addEntity(SupplierUser.class);
		SupplierUser supplierUser = (SupplierUser) query.uniqueResult();
		trans.commit();
		return supplierUser;
	}
	// Get list favorite store for user

	public List<Supplier> getListSupplierFavoriteByUser(String userID) {
		List<Supplier> results = new ArrayList<Supplier>();
		// Get supplierID
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		String sql = "SELECT f.suppl_id FROM fg_favorite_suppliers f WHERE f.user_id = '" + userID + "'";
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> entities = query.list();
		System.out.println("show entities " + entities);
		// Get supplier
		for (Object entity : entities) {
			session = this.sessionFactory.openSession();
			System.out.println(" " + entity);
			String sql3 = "SELECT * FROM fg_suppliers s WHERE s.suppl_id='" + entity + "'";
			System.out.println(sql3);
			SQLQuery query3 = session.createSQLQuery(sql3);
			query3.addEntity(Supplier.class);
			results.add((Supplier) query3.uniqueResult());
			System.out.println("show result" + results);
		}
		trans.commit();
		System.out.println("show result count:" + results.size());
		return results;
	}

	// Delete favorite store
	public void deleteFavoriteStore(Session session, int suppl_id) {
		String deleteQuery = "delete from fg_favorite_suppliers where suppl_id = " + suppl_id;
		Query query = session.createSQLQuery(deleteQuery);
		query.executeUpdate();
	}

	public String deleteStoreFavorite(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
			deleteFavoriteStore(session, supplId);
			trans.commit();
			return "true";
		} catch (Exception ex) {
			trans.rollback();
			return "false";
		}
	}

	public List<BillHistory> getListBillHistory(String userID) {		
		 Session session = this.sessionFactory.getCurrentSession();
		 Transaction trans = session.beginTransaction();
		 String sql = "SELECT o.order_id, o.order_date, o.deliver_date,	 o.order_status, o.prod_amt, o.app_id,od.prod_id,od.prod_qty,prod_name,s.suppl_id, s.supplier_name,p.unit_price FROM fg_orders o  INNER JOIN fg_order_details od ON o.order_id = od.order_id INNER JOIN	fg_products p ON od.prod_id = p.prod_id INNER JOIN fg_suppliers s ON	o.suppl_id = s.suppl_id WHERE o.user_id = '"+ userID +"'";
		 SQLQuery query = session.createSQLQuery(sql);
		 List<BillHistory> results = query.list();
		 query.addEntity(BillHistory.class);
		 trans.commit();
		 return results;
	}
	public List<BillHistory> getListSearchBillHistory(String userID, String dateFrom, String dateTo){
		Session session = this.sessionFactory.getCurrentSession();
		 Transaction trans = session.beginTransaction();
		 String sql = "SELECT o.order_id, o.order_date, o.deliver_date,	 o.order_status, o.prod_amt, o.app_id,od.prod_id,od.prod_qty,prod_name,s.suppl_id, s.supplier_name,p.unit_price FROM fg_orders o  INNER JOIN fg_order_details od ON o.order_id = od.order_id INNER JOIN	fg_products p ON od.prod_id = p.prod_id INNER JOIN fg_suppliers s ON	o.suppl_id = s.suppl_id WHERE o.user_id = '"+ userID +"' AND o.order_date >= '"+dateFrom+"' AND o.order_date<='"+dateTo+"'";
		 SQLQuery query = session.createSQLQuery(sql);
		 List<BillHistory> results = query.list();
		 query.addEntity(BillHistory.class);
		 trans.commit();
		 System.out.println("show result count:" + results.size());
		 return results;
	}
}
