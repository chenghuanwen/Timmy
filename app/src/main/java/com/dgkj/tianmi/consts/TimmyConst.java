package com.dgkj.tianmi.consts;

/**
 * Created by Android004 on 2017/2/13.
 */

public class TimmyConst {
    public static final int TM_REQUEST_PERMISSION_CODE = 101;
    public static final int TM_REQUEST_PICTURE_CODE = 0;
    public static final int TM_REQUEST_CAMERA_CODE = 1;
    public static final String SHANGCHENG_NET_URL = "http://123.206.17.177:9090/shopping/";

    //商城
    public static final String MAIN_URL = SHANGCHENG_NET_URL +"posterAction_getShoppingIndexList?fromApp=true";//主入口
    public static final String SHANGCHENG_URL = SHANGCHENG_NET_URL;//商城入口
    public static final String FIRSTPAGE_URL= SHANGCHENG_NET_URL +"index.jsp";//首页url
    public static final String LOGIN_RUL = SHANGCHENG_NET_URL + "login.jsp";//登录界面url
    public static final String SHOUJI_RUL = SHANGCHENG_NET_URL +"productAction_getProductBrowseList";//手机产品界面url
    public static final String PEIJIAN_RUL = SHANGCHENG_NET_URL +"giftAction_getGiftBrowseList";//手机配件界面url
    public static final String JIFEN_RUL = SHANGCHENG_NET_URL +"integralAction_getIntegralBrowseList";//积分界面url
    public static final String JIFEN_DUIHUAN_RUL = SHANGCHENG_NET_URL +"exchange.jsp";//兑换界面url
    public static final String FUWU_RUL = SHANGCHENG_NET_URL +"server.jsp";//服务界面url
    public static final String GOUWUCHE_RUL = SHANGCHENG_NET_URL +"shoppingcartAction_getShoppingcartList";//购物车界面url
    public static final String PRODUCT_RUL = SHANGCHENG_NET_URL +"product.jsp";//手机产品界面首页url
    public static final String PARTS_RUL = SHANGCHENG_NET_URL +"parts.jsp";//配件首页界面url
    public static final String INTEGRAL_RUL = SHANGCHENG_NET_URL +"integral.jsp";//积分首页界面url
    public static final String ORDERHOME_RUL = SHANGCHENG_NET_URL +"order_home.jsp";//个人中心首页界面url
    public static final String USERORDER_RUL = SHANGCHENG_NET_URL +"user_order.jsp";//我的订单中心首页界面url
    public static final String COMMIT_ORDER_RUL = SHANGCHENG_NET_URL +"paying.jsp";//提交订单界面url
    public static final String GOUWUCHE_ENTER_RUL = SHANGCHENG_NET_URL +"shopping_cart.jsp";//购物车界入口面url

    //论坛
    public static final String LUNTAN_MAIN_URL = "http://123.206.17.177:9090/Forum/";//论坛入口

    public static final String LUNTAN_URL = LUNTAN_MAIN_URL+"?device=1";//论坛入口
    public static final String TIEBA_RUL = LUNTAN_MAIN_URL+"postlist.jsp?device=1";//贴吧界面url
    public static final String BANKUAI_RUL = LUNTAN_MAIN_URL+"section.jsp?device=1";//板块界面url
    public static final String HUODONG_RUL = LUNTAN_MAIN_URL+"essence.jsp?device=1";//活动界面url
    public static final String JINGHUA_RUL = LUNTAN_MAIN_URL+"essence.jsp?device=1";//精华界面url
    public static final String GUANWANG_RUL = "http://timmymobile.com/m/Index.aspx";//官网界面url
    public static final String SHOUCANG_RUL = LUNTAN_MAIN_URL+"collection.jsp?device=1";//收藏界面url
    public static final String SHEZHI_RUL = LUNTAN_MAIN_URL+"personal.jsp?device=1";//设置界面url
    public static final String USERCENTER_RUL = LUNTAN_MAIN_URL+"userinfo.jsp?device=1";//个人中心界面url
    public static final String FABIAO_RUL = LUNTAN_MAIN_URL+ "posting.jsp?device=1";//发帖界面url
    public static final String NOTIFY_RUL = LUNTAN_MAIN_URL+"inform.jsp?device=1";//通知界面url
    public static final String POST_RUL = LUNTAN_MAIN_URL+"posting.jsp?id=";//发表界面url



    public static final String FORUML_POST_RUL = LUNTAN_MAIN_URL+"postAction_addPost";//论坛发帖接口
    public static final String FORUML_GET_THEME_RUL = LUNTAN_MAIN_URL+"postThemeAction_getPostTheme";//论坛获取主题分类接口

    public static final String FORUMLOGIN_RUL = LUNTAN_MAIN_URL+"userInfoAction_appLogin";//论坛登录接口
    public static final String FORUM_REGIST_RUL = LUNTAN_MAIN_URL+"userInfoAction_addUserInfo";//论坛注册接口
    public static final String CREATE_VERIFICATION_URL=LUNTAN_MAIN_URL+"userInfoAction_getCaptcha";//生成验证码接口

    public static final String SHANGCHENG_REGIST_RUL = SHANGCHENG_NET_URL +"userInfoAction_register";//商城注册接口
    public static final String LOGIN_ENTER_RUL = SHANGCHENG_NET_URL +"userInfoAction_login";//登录接口
    public static final String LOGOUT_RUL = SHANGCHENG_NET_URL +"userInfoAction_exitLogin";//退出登录接口
    public static final String CHECK_VERIFICATION_URL="http://192.168.3.210:8080/DingGao_007/checkCode";//检测验证码接口
    public static final String CHECK_USER_EXIST_URL=SHANGCHENG_NET_URL+"userInfoAction_checkUsrename";//检测注册用户是否已存在接口
    public static final String CHECK_EMAIL_CODE_URL=SHANGCHENG_NET_URL+"userInfoAction_checkCode";//验证邮箱验证码接口
    public static final String GET_EMAIL_CODE_URL=SHANGCHENG_NET_URL+"userInfoAction_getEmailCode";//获取找回密码邮箱验证码接口
    public static final String RESET_PASSWORD_URL=SHANGCHENG_NET_URL+"userInfoAction_resetPassword";//重置密码接口




}
