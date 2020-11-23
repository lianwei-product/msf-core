package cn.com.connext.msf.server.management.repository;

import cn.com.connext.msf.data.mongo.ConnextMongoRepository;
import cn.com.connext.msf.server.management.entity.ZonedServiceSetting;
import org.springframework.stereotype.Repository;

/**
 * 服务可用区配置 - 数据访问接口定义
 * 开发人员: 程瀚
 * 修订日期: 2020-06-16 16:35:34
 */
@Repository
public interface ServiceZoneSettingRepository extends ConnextMongoRepository<ZonedServiceSetting, String> {

    /**
     * 该方法用于检测“托管服务”中“服务名称”属性的值，是否已经被其它记录所使用。
     *
     * @param name 服务名称
     * @param id   唯一标识
     * @return 返回与指定“服务名称”相同的记录总数。
     */
    long countByNameAndIdNot(String name, String id);

}
