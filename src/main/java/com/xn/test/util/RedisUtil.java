package com.xn.test.util;

import cn.xn.cache.domain.Param;
import cn.xn.cache.service.ICommonRedisService;
import cn.xn.cache.service.IUserRedisService;

import cn.xn.user.enums.SourceType;
import cn.xn.user.enums.SystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.xn.test.command.RedisCommand.PREFIX_LOGINPWD;

@Service("redisUtil")
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Resource
    private IUserRedisService userService;
    
    @Resource
    private ICommonRedisService commonService;
    
    /**
     * 登录token保存时间
     */
    private static int loginExpire;
    
    /**
     * 登录密码失败次数保存时间
     */
    private static int loginPwdErrorExpire;
    
    /**
     * 支付密码失败次数保存时间
     */
    private static int payPwdErrorExpire;
    public static final int LOGIN_EXPIRE = 30 * 24 * 60 * 1000;// 登录默认失效三十天
    public static final int PWD_EXPIRE = 24 * 60 * 1000;// 密码默认失效1天
    public static final String BID = "UNIUSER";//缓存的业务id
    public static final String PREFIX_LOGIN = "login";
    static {
        String obj = StringUtil.getPro("redis.login.expire","2");
        loginExpire = null == obj ? LOGIN_EXPIRE : Integer.parseInt(obj);
        obj = StringUtil.getPro("redis.loginpwderror.expire","2");
        loginPwdErrorExpire = null == obj ? PWD_EXPIRE : Integer.parseInt(obj);
        obj = StringUtil.getPro("redis.paypwderror.expire","2");
        payPwdErrorExpire = null == obj ? PWD_EXPIRE : Integer.parseInt(obj);
    }
    
    /**
     * token保存到redis中
     * @param memberNo 用户id（用户数据分离后加上了systemType）
     * @param tokenId token
     * @param source 来源（ios和安卓统一为app）
     * @throws Exception
     */
    public void setTokenId(String memberNo, String tokenId, String source) throws Exception {
        Param p = new Param();
        p.setBid(BID);
        p.setUid(PREFIX_LOGIN + "-" + memberNo);
        p.setTokenId(tokenId);
        p.setDate(new Date());
        p.setSource(source);
        userService.set(p);
    }
    
    /**
     * 删除token
     * @param memberNo 用户id（用户数据分离后加上了systemType）
     * @param tokenId token
     * @param source 来源（ios和安卓统一为app）
     * @throws Exception
     */
    public void delTokenId(String memberNo, String tokenId, String source) throws Exception {
        Param p = new Param();
        p.setBid(BID);
        p.setUid(PREFIX_LOGIN + "-" + memberNo);
        p.setTokenId(tokenId);
        p.setSource(source);
        userService.del(p);
    }
    public void logout(String systemType, String sourceType, String tokenId,String memberNo) throws Exception {
        String tmpSource = getSourceType(systemType, sourceType);
        String tmpSystemType = convertSystemType(systemType);
        delTokenId(tmpSystemType + "-" + memberNo, tokenId, tmpSource);
    }
    
    /**
     * 获取token
     * @param memberNo 用户id（用户数据分离后加上了systemType）
     * @param tokenId token
     * @return
     * @throws Exception
     */
    public boolean getTokenId(String memberNo, String tokenId) throws Exception {
        Param p = new Param();
        p.setBid(BID);
        p.setUid(PREFIX_LOGIN + "-" + memberNo);
        p.setTokenId(tokenId);
        p.setDate(new Date());
        return userService.get(p, loginExpire);
    }
    
    /**
     * 延长token过期时间
     * @param memberNo 用户id（用户数据分离后加上了systemType）
     * @param tokenId token
     * @param source 来源（ios和安卓统一为app）
     * @throws Exception
     */
    public void delayToken(String memberNo, String tokenId, String source) throws Exception {
        Param p = new Param();
        p.setBid(BID);
        p.setUid(PREFIX_LOGIN + "-" + memberNo);
        p.setTokenId(tokenId);
        p.setDate(new Date());
        p.setSource(source);
        userService.delayToken(p);
    }
    
    /**
     * 获取登录失败次数
     * @param key 登录手机号（用户数据分离后加上了systemType）
     * @return
     * @throws Exception
     */
    public int getErrorTime(String key) throws Exception {
        int count = 0;
        String value = commonService.get(BID, key);
        if (!StringUtil.isEmpty(value)) {
            count = Integer.parseInt(value);
        }
        return count;
    }
    
    /**
     * 保存登录失败次数
     * @param key 登录手机号（用户数据分离后加上了systemType）
     * @param count 次数
     * @param flag
     * @throws Exception
     */
    public void putErrorTime(String key, int count, String flag) throws Exception {
        if (flag.equals("login")) {
            commonService.set(BID, key, String.valueOf(count), loginPwdErrorExpire);
        }
        if (flag.equals("pay")) {
            commonService.set(BID, key, String.valueOf(count), payPwdErrorExpire);
        }
    }
    
    /**
     * 删除登录失败次数
     * @param key 登录手机号（用户数据分离后加上了systemType）
     * @throws Exception
     */
    public void deleteErrorTime(String key) throws Exception {
        commonService.del(BID, key);
    }
    /** 登录令牌存入redis中并返回给前置系统 */
    public void saveToken2Redis(String systemType, String sourceType, String loginName, String tokenId, String memberNo) {
        try {
            String tmpSystemType = convertSystemType(systemType);
            String tmpSource = getSourceType(systemType, sourceType);
            delTokenId(tmpSystemType + "-" + memberNo, tokenId, tmpSource);
            // redis中存入token信息
            setTokenId(tmpSystemType + "-" + memberNo, tokenId, tmpSource);
            // 删除错误次数记录
            deleteErrorTime(PREFIX_LOGINPWD + "-" + systemType + "-" + loginName);
        } catch (Exception e) {
            logger.error(String.format("~~~[%s]-[%s]用户token存入redis中报错：[%s]~~~~", memberNo, tokenId, e));
        }
    }
    /** 处理特殊的系统类型（业务编码) */
    private String convertSystemType(String systemType) {
        if (SystemType.CHANNEL2.getText().equals(systemType)) {
            return SystemType.CHANNEL.getText();
        }
        return systemType;
    }
    /** 获取用户的登录来源 */
    private String getSourceType(String systemType, String source) {
        StringBuffer sb = new StringBuffer();

        if (!SystemType.QGZ.getText().equals(systemType)) {
            sb.append(systemType).append("-");
        }
        if (SourceType.ANDROID.getText().equalsIgnoreCase(source) || SourceType.IOS.getText().equalsIgnoreCase(source)) {
            // 如果来源类型是安卓或ios，则统一认为是app（客户端），这样做是要实现踢人的功能
            sb.append("app");
        } else {
            sb.append(source);
        }
        return sb.toString();
    }
}
