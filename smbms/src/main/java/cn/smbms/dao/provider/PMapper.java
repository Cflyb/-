package cn.smbms.dao.provider;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PMapper {
    /**
     * 增加供应商
     * @param provider
     * @return
     * @throws Exception
     */
    int add(Provider provider);
    /**
     * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param proName
     * @return
     * @throws Exception
     */
    List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode);

    List<Provider> getProList();
    /**
     * 通过proId删除Provider
     * @param delId
     * @return
     */
    int deleteProviderById(String delId);
    /**
     * 通过proId获取Provider
     * @param id
     * @return
     * @throws Exception
     */
    Provider getProviderById(String id)throws Exception;
    /**
     * 修改供应商信息
     * @param provider
     * @return
     * @throws Exception
     */
    int modify(Provider provider);

    Provider getLoginProvider(String proCode);
}
