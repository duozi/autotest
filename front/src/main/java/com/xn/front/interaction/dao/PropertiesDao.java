<<<<<<< HEAD
package com.xn.front.interaction.dao;

import com.xn.common.model.ServiceDesc;
import org.apache.ibatis.annotations.Param;

/**
 * Created by xn056839 on 2016/12/8.
 */

public interface PropertiesDao {

    public void createServiceDesc(ServiceDesc serviceDesc) throws Exception;

    public ServiceDesc getServiceDesc(@Param("group") String group);
}
=======
package com.xn.front.interaction.dao;

import com.xn.common.model.ServiceDesc;

import java.util.List;

/**
 * Created by xn056839 on 2016/12/8.
 */

public interface PropertiesDao {
    List<String> getAllPlaneName();
    void createServiceDesc(ServiceDesc serviceDesc) throws Exception;
}
>>>>>>> ad98a7382e1771849640bd83355d6dee312caeb8
