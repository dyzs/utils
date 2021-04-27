package com.weway.common.resp;

/**
 * TODO 描述
 *
 * @auther: zhoufd
 * @date: 2020/3/10
 **/
public enum RespError {

    /**
     * 成功的响应码
     */
    NORMAL("000000", "NORMAL"),
    LOGIN_FAIL("000001", "登录失败"),
    ERROR_PARAMS("000002", "缺少参数"),
    SERVICE_ERROR("000003", "请求异常，请联系管理员"),
    TOKEN_NO_PARAMS("000005", "token不存在"),
    TOKEN_FAIL("000006", "token失效"),
    NO_COMPANY_LIST("000008", "没有可登录的公司或不是管理员"),
    COMPANY_VIDEO_OVERFLOW("000009", "视频大小超出限制"),
    FACE_TOKEN_NOT_EXISTS("000010", "人脸识别token失效"),
    COMPANY_SCAN_FAILED("000004", "微信扫码失败"),
    OPERRATOR_SALT_ERROR("000020", "获取加密盐失败"),
    GET_BUSINESS_ID_ERROR("000021", "获取业务id失败"),
    GET_PHONE_EXIST_ERROR("000022", "获取平台号码库失败"),
    CREATE_USER_ERROR("000023", "创建用户失败"),
    SYNC_USER_ERROR("000024", "同步用户失败"),

    /**
     * 签名错误
     */
    ERROR_SIGN("999999", "签名错误"),

    NOT_AUTO("888888", "无操作权限"),


    /**
     * 验证码
     */
    NUMBER_EMPTY("400001", "收取验证码号码不能为空!"),
    VERIFY_CODE_EMPTY("400002", "收取验证码号码不能为空!"),
    VALIDATE_CODE_FAIL("400003", "验证码输入错误!"),
    EMAIL_EMPTY("400004", "邮箱不能为空！"),
    EMAIL_SEND_FAIL("400005", "邮箱发送失败！"),
    EMAIL_LINE_OVERDUE("400006", "验证码已过期"),
    IS_PUBLIC_EMAIL("400007", "不支持公共邮箱，请使用企业邮箱注册"),
    EMAIL_EXIST("400008", "该邮箱已经注册"),
    SEND_SMS_ERROR("400009", "短信发送失败!"),
    IdCardImgError("400010", "请上传本人的清晰照"),
    IdCardAndPhone_Mismatch("400011", "手机号码跟身份信息不一致"),
    NAME_ERROR("400012", "姓名有误"),
    IDCARD_ERROR("400013", "身份证号码有误"),
    PERSON_EXIST("400014", "已有实名认证身份,请先解绑"),
    UPLOAD_IDCARDIMG("400015", "上传的身份证人像面有误"),
    UPLOAD_IDCARDBACKIMG("400016", "上传的身份证国徽面有误"),
    PHONE_NAME_IDCARD_ERROR("400017", "信息不一致,请确认后重新提交"),
    PERSON_NOT_EXIST("400018", "个人身份不存在或已删除"),
    PERSON_SAVE_ERROR("400019", "保存个人用户失败"),
    IDCARD_TYPE_ERROR("400020", "身份类型不正确"),
    PHONE_NOVERIFY("400021", "手机号码未实名认证"),
    FACEID_NOT_EXIST("400022", "未做人脸识别，不能操作独立密码"),
    NOT_SET_PASSWORD("400023", "未设置独立密码,请用验证码登录"),
    /**
     * 员工白名单
     */

    ADD_EMPLOYEE_FAIL("200001", "添加员工白名单失败"),
    EMPLOYEE_ID_EMPTY("200002", "员工id不能为空"),
    COMPANY_ID_EMPTY("200003", "公司id不能为空"),
    IMPORT_FAIL("200004", "导入员工信息失败!"),
    ADD_COMPANY_FAIL("200005", "添加企业失败"),
    OPEN_COMPANY_FAIL("200006", "企业申请开通失败"),
    EXISTS_COMPANY("200007", "企业已经申请"),
    EMPLOYEE_HAS_EXISTS("200008", "员工已存在"),
    MOBILE_ERROR("200009", "手机号号码格式错误"),
    ID_CARD_NO_ERROR("200010", "身份证号码格式错误"),
    EMPLOYEE_NOT_EXISTS("200011", "员工不存在"),
    APPLYCOMPANY_FAIL("200012", "同一手机号码只能认证一家企业"),
    COMPANY_NOT_EXISTS("200013", "公司不存在或已刪除"),
    WECHAT_PHONE_EXISTS("200014", "该手机号码已在其它微信上认证"),
    COMPANY_SOURCE_PASSWORD_ERROR("200015", "原始密码不正确"),
    LOGIN_PASSWORD_ERROR("200016", "密码不正确"),
    SCAN_RECORD_NOT_EXISTS("200017", "扫码记录不存在"),
    WECHAT_NOT_OPNEID("200018", "openId不能为空或不正确"),
    PHONE_NOT_EXISTS("200019", "手机号码不在系统内"),
    OPENID_NOT_MATCH_PHONE("200020", "openId对应的手机号不一致,请重新登录查看"),
    IMPORT_MAX_LENGTH("2000023", "单次批量导入不可超过1万条!"),
    IMPORT_EMPTY_DATA("2000024", "导入数据不能为空!"),

    /**
     * 运营平台用户管理
     */
    COMPANY_NO_AUTH_LOGIN("500001", "公司未审核通过，无权限登录"),
    COMPANY_NO_AUTH_OPER("500002", "公司未审核通过，无权限操作"),
    ADD_USER_FAIL("500003", "操作用户失败"),

    /**
     * CA接口
     */
    NOT_USER_KEY("600001", "缺少操作人签名(usb-key)"),
    SIGNED_ERROR("600002", "签证失败"),
    DOWNLOAD_CERT_ERROR("600003", "证书下载失败"),
    NO_REQ_ID_ERROR("600004", "无CA业务流水号"),
    QUERY_CERT_PROGRESS_ERROR("600005", "查询证书进度失败"),
    REQUEST_CA_ERROR("600006", "查询证书进度失败"),
    CA_COMPANY_USER_ERROR("600007", "申请人信息不正确"),
    CA_COMPANY_ERROR("600008", "企业统一社会信用代码不正确"),
    NO_DOWNLOAD_CERT("600009", "没有可下载的证书"),
    CA_CERT_NOT_MATCH("600010", "证书不匹配"),
    CA_CERT_NOT_EXISTS("600011", "证书不存在"),
    CA_INVOKE_CERT_ERROR("600012", "证书注销失败"),
    CA_REISSUE_CERT_ERROR("600013", "证书补办失败"),
    CA_APPLY_CERT_PETTING("600014", "公司已提交认证或认证成功"),

    /**
     * 查詢接口
     */
    RECORD_EXISTS("7000001", "记录已存在"),

    /**
     * 上传文件
     */
    UPLOAD_FILE_FAIL("300001", "上传文件失败"),

    //SDK --------------------------------
    ERROR_APP_KEY("800001", "应用id和应用密码不匹配"),
    PHONE_USER_KEY("800002", "手机号码没有绑定用户"),

    /**
     * 终端产商
     */
    SDK_ACCOUNT_ERROR("900001", "账号不存在"),

    /**
     * 商品
     */
    COMMODITY_SAVE_ERROR("1000001", "生成商品失败，名称重复"),
    COMMODITY_DELETE_ERROR("1000002", "删除商品失败"),
    COMMODITY_STATE_ERROR("1000003", "上、下架商品失败"),


    PAY_FAIL("100001", "订单未支付"),
    PAY_SUCESS("100002", "支付成功"),
    DIAL_LACK("100004", "公共拨号次数不够"),
    IMAGE_LACK("100005", "公共图片拨号次数不够"),
    STAFF_LESS("100006", "员工添加可用数不够,请充值"),

    NOT_FREE("1000012", "领取失败，已有套餐正在使用或当月已经领取"),
    BUY_FALL("100007", "当前不支持套餐降级，请选择更高级别的套餐"),
    BUY_FALL1("100009", "需要先购买套餐才能购买单品"),
    NOT_AVERAGE("100008", "可分配次数少于员工数，无法平均分配"),

    SYSUSER_REPEAT("100010", "用户名已经存在"),
    USER_NOT_EXISTS("100011", "用户不存在"),


    /**
     * -------------------------- 新CA接口--------------------------------------
     */
    CERT_HAS_EXISTS("110001", "证书已提交,不可删除!"),

    UPLOAD_P10_ERROR("110002", "上传身份信息失败!"),

    CERT_ALREADY_SUBMIT("110003", "身份信息已经提交"),

    UNIQUE_KEY_ERROR("110004", "唯一序列号不正确"),

    P10_NOT_BLANK("110005", "p10不能为空!"),;

    /**
     * 状态结果
     */
    SERVICE_OK(1, "成功"),
    UNKNOWN(1000, "未知错误"),
    PARSE_ERROR(1001, "解析失败"),
    NETWORK_ERROR(1002, "连接失败"),
    UDP_NETWORK_ERROR(1006, "UDP连接失败"),
    HTTP_ERROR(1003, "协议错误"),
    SSL_ERROR(1005, "证书出错"),
    SOCKET_TIMEOUT_ERROR(10060, "连接超时"),
    UDP_SOCKET_TIMEOUT_ERROR(1016, "UDP连接超时"),
    LOGGED_IN_ERROR(1015, "未登录"),
    PIN_ERROR(1006, "PIN码错误"),
    INSTALL_CERTIFICATE_ERROR(1007, "证书安装失败"),
    PHONE_ERROR(1008, "手机号码格式不正确"),
    CODE_ERROR(1009, "验证码格式不正确"),
    LOGOUT_ERROR(1010, "退出登录失败"),
    NAME_ERROR(1011, "姓名格式不对"),
    ID_NO_ERROR(1012, "身份证号码格式不正确"),
    EMAIL_FORMAT_ERROR(1013, "邮箱格式不正确"),
    EXT_ERROR(1014, "扩展字段限制100个字节"),
    CTAUTH_ERROR(1015, "免密登录失败"),
    IDENTITY_ERROR(3000, "票据身份不存在"),
    DOWNLOAD_TOKEN_ERROR(3001, "下载通信令牌失败"),
    DOWNLOAD_TOKEN_ID_ERROR(3003, "下载通信令牌id失败"),
    VERIFY_ERROR(3004, "验签错误"),
    UPLOAD_TOKEN_ERROR(3002, "上传通信令牌失败"),
    REQ_ID_ERROR(3005, "业务ID为空"),
    CREDENTIAL_ERROR(3006, "证书下载失败"),
    PIN_IS_EMPTY(3007, "PIN码不能为空"),
    IDENTITY_FAILURE(3008, "身份不可用"),
    IDENTITY_BANNED(3009, "当前身份被封禁"),
    COMPLAINT_REASON(30010, "举报内容不能为空"),
    ID_IS_BANK(30011, "id不能为空"),

    TYPE_IS_BANK(30012, "类型不能为空"),
    CANCEL(30013, "取消"),
    AVATAR_BANK(30014, "头像地址不能为空"),
    READ_PHONE_STATE_PERMISSION(30015, "permission denied"),
    DEVICE_NOT_EXIST(30016, "device type 67 not found!"),
    KI_ERROR(30018, "KI错误"),
    CALLER_CERT_PHONE_NO_MATCH(30017, "主叫号码与证书不匹配"),
    NAME_IS_BANK(30019, "姓名不能为空"),
    ID_CARD_IS_BANK(30020, "身份证号码不能为空"),
    INSTALL_CERT_ERROR(30021, "安装证书失败"),
    USER_ID_IS_BANK(30022, "用户id错误"),
    PASSWORD_IS_BANK(30023, "密码为空"),
    REISSUE_CERTIFICATE_ERROR(30025, "补办证书失败"),
    SERVICE_DATA_ERROR(30026, "服务器数据错误"),
    TOKEN_ERROR(30027, "token错误"),
    THE_TWO_PASSWORDS_ARE_DIFFERENT(30024, "两次密码不同");


    String errorCode;

    String errorMsg;

    RespError(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}