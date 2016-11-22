package com.xn.common.sign;/**
 * Created by xn056839 on 2016/11/22.
 */

import com.xn.common.Exception.CaseErrorEqualException;
import com.xn.common.service.GetPara;
import com.xn.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.TreeMap;

import static com.xn.common.util.SignUtil.mapToString;
import static com.xn.common.util.SignUtil.md5;

public class MessageAddSign {
    private static final Logger logger = LoggerFactory.getLogger(MessageAddSign.class);

    public String MessageHttpAddSing(TreeMap<String, String> treeMap, boolean useSign, String signType) throws CaseErrorEqualException {
        String key = "";
        //只有useSign为true&&传入sign为空才会计算sign
        if (useSign) {
            String oldSign = treeMap.get("sign");
            if (StringUtils.isEmpty(oldSign)) {
                String type = "";
                if (treeMap.containsKey("partnerId")) {
                    type = treeMap.get("partnerId");
                }

                GetPara getPara = new GetPara();
                String path = getPara.getPath();
                File file = new File(path + "suite/key.properties");
                key = StringUtil.getConfig(file, signType + ".key." + type, "");
                if (StringUtils.isEmpty(key)) {
                    throw new CaseErrorEqualException("no key");
                }

                treeMap.remove("signature");
                String sign_sb = mapToString(treeMap);
                String signPara = sign_sb + "&key=" + key;
                String sign = md5(signPara,null);
                sign_sb += "&signature=" + sign;
                return sign_sb;
            }
        }


        String sign_sb = mapToString(treeMap);
        return sign_sb;

    }

}
