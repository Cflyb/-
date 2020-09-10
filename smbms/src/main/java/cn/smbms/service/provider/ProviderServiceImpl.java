package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BiMapper;

import cn.smbms.dao.provider.PMapper;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {
	@Autowired
	private PMapper pMapper;
	@Autowired
	private BiMapper biMapper;
	@Override
	public boolean add(Provider provider) {
		// TODO Auto-generated method stub
		boolean flag = false;

		try {
			if(pMapper.add(provider) > 0)
				flag = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("rollback==================");

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return flag;
	}

	@Override
	public List<Provider> getProviderList(String proName,String proCode) {
		// TODO Auto-generated method stub

		List<Provider> providerList = null;
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		try {
			providerList = pMapper.getProviderList(proName,proCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return providerList;
	}

    @Override
    public Provider selectProviderExist(String proCode) {

       Provider provider=null;
        try {

            provider=pMapper.getLoginProvider(proCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return provider;
    }

    /**
	 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
	 * 若订单表中无该供应商的订单数据，则可以删除
	 * 若有该供应商的订单数据，则不可以删除
	 * 返回值billCount
	 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
	 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
	 * 
	 * ---判断
	 * 如果billCount = -1 失败
	 * 若billCount >= 0 成功
	 */
	@Override
	public int deleteProviderById(String delId) {
		// TODO Auto-generated method stub
		int billCount = -1;
		try {
			billCount = biMapper.getBillCountByProviderId(delId);
			if(billCount == 0){
				pMapper.deleteProviderById(delId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			billCount = -1;
		}
		return billCount;
	}

	@Override
	public Provider getProviderById(String id) {
		// TODO Auto-generated method stub
		Provider provider = null;

		try{

			provider = pMapper.getProviderById( id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			provider = null;
		}
		return provider;
	}

	@Override
	public boolean modify(Provider provider) {
		// TODO Auto-generated method stub

		boolean flag = false;
		try {

			if(pMapper.modify(provider) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

}
