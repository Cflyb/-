package cn.smbms.service.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RMapper;
import cn.smbms.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
    private RMapper rMapper;
	
	@Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		List<Role> roleList = null;
		try {
			roleList = rMapper.getRoleList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}

    @Override
    public boolean add(Role role) {
        boolean flag = false;

        try {
            if(rMapper.add(role) > 0)
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
    public boolean deleteRoleById(String roleId) {
        boolean flag =false;
        try{

            if(rMapper.deleteRoleById(roleId)>0)
                flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean modify(Role role) {
        boolean flag = false;
        if(rMapper.modifyRole(role)>0)
            flag = true;
        return flag;

    }

    @Override
    public Role getRoleById(String id) {
        Role role =null;
        role = rMapper.getRoleById(id);
        return role;
    }
}
