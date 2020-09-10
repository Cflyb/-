package cn.smbms.dao.role;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;

import java.util.List;

public interface RMapper {
    List<Role> getRoleList();

    int add(Role role);

    int deleteRoleById(String delId);

    int modifyRole(Role role);
    Role getRoleById(String id);
}
