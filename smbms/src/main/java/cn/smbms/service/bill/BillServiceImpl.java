package cn.smbms.service.bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BiMapper;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {
	@Autowired
	private BiMapper biMapper;
	@Override
	public boolean add(Bill bill) {
		// TODO Auto-generated method stub
		boolean flag = false;

		try {


			if(biMapper.add(bill) > 0)
				flag = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return flag;
	}

	@Override
	public List<Bill> getBillList(Bill bill) {
		// TODO Auto-generated method stub

		List<Bill> billList = null;
		System.out.println("query productName ---- > " + bill.getProductName());
		System.out.println("query providerId ---- > " + bill.getProviderId());
		System.out.println("query isPayment ---- > " + bill.getIsPayment());
		
		try {

			billList = biMapper.getBillList(bill);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billList;
	}

	@Override
	public boolean deleteBillById(String delId) {
		// TODO Auto-generated method stub

		boolean flag = false;
		try {

			if(biMapper.deleteBillById(delId) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public Bill getBillById(String id) {
		// TODO Auto-generated method stub
		Bill bill = null;

		try{
			bill = biMapper.getBillById(id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bill = null;
		}
		return bill;
	}

	@Override
	public boolean modify(Bill bill) {
		// TODO Auto-generated method stub

		boolean flag = false;
		try {

			if(biMapper.modify(bill) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

}
