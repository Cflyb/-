package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;

public interface RoleService {
	
	public List<Role> getRoleList();

    public boolean add(Role role);
    public boolean deleteRoleById(String delId);
    public boolean modify(Role role);
    public Role getRoleById(String id);
}
