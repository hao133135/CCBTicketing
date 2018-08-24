package com.jilian.ccbticketing.Model;

public class transDataModel {

    private String resCode;                        //39域返回码
    private String resMsg;                        //交易处理结果信息
    private String merchantName;                //商户名称
    private String merchantID;                    //商户编号
    private String terminalID;                    //终端号
    private String operID;                        //操作员号
    private String cardIssuer;                    //卡种信息
    private String cardNo;                        //卡号
    private String cardCode;                    //卡组织信息（非CUP为外币卡）
    private String cardIputMethod;            //刷卡方式，如刷卡为02，挥卡为07、98、96，插卡为05，手输为01、91，降级为92，扫码或其他为#
    private String expDate;                        //卡有效期
    private String transName;                    //交易类型
    private String batchNo;                        //批次号
    private String traceNo;                        //凭证号
    private String checkNo;                        //凭证号（不使用）
    private String refNo;                        //参考号
    private String authCode;                    //授权码
    private String transDate;                    //交易日期
    private String transTime;                    //交易时间
    private String amt;                            //交易金额
    private String receivedAmount;                //现金实收金额
    private String changeAmount;                //现金找零金额
    private String oldAmount;                    //建行消费优惠前金额
    private String saleRate;                    //建行消费优惠折扣率（折上折惠率）
    private String referInfo;                    //备注信息（应用版本V2.0.5前返回参考号，应用版本后返回备注信息）
    private String payChannel;                    //支付渠道（CHANNEL_ALIPAY  支付宝，CHANNEL_WEPAY  微信，CHANNEL_UNIONPAY  其他银行卡交易）
    private String prefrentialAmount;            //优惠立减优惠券抵消金额
    private String jhthOriginalAmount;       //建行特惠原金额
    private String jhthDiscountAmount;        //建行特惠折后金额
    private String jhthCouponDesc;                //建行特惠活动描述
    private String jhthAwardDesc;                //建行特惠中奖描述
    private String ahnTxnAmt;                    //新一代交易（龙支付、聚合支付）等订单金额
    private String txnAmt;                        //新一代交易（龙支付、聚合支付）等实付金额
    private String saleAmt;                    //（旧版特惠）应付金额（不使用）
    private String activityAmt;                //（旧版特惠）打折金额（不使用）
    private String paidAmt;                    //（旧版特惠）实付金额（不使用）
    private String activityName;                //（旧版特惠）活动名称（不使用）
    private String couponMsg;                    //（旧版特惠）优惠券信息（不使用）
    private String qrCode;                        //快速预授权二维码
    private String duePoint;                    //权益积分本年到期积分
    private String pointBalance;                //权益积分当前积分余额
    private String numOfPeople;                //权益积分本次享受服务人数
    private String consumedPoint;               // 权益积分本次消费积分
    private String exchangePoint;               // 积分兑换消费积分
    private String exchangeMoney;           // 积分兑换消费金额
    private String orgTraceNo;                    //（撤销交易）原交易凭证号
    private String orgRefNo;                    //（退货交易）原主机参考号
    private String orgAmt;                    //（预授权撤销）原交易金额
    private String lsOrderNo;                    //第三方传入唯一订单号
    private String projectCode;              //分期交易项目编号
    private String installmentPeriod;         //分期交易分期数
    private String installmentIsBigSale;        //分期交易是否大额交易标识（1  表示大额即专用分期，0  表示普通额度即商场分期）
    private String DPDSvcID;              //龙支付交易订单号
    private String isScancodeSale;      //是否银联二维码消费
    private String unionpayQRCode;          //银联二维码信息
    private String counterNo;            //银行柜台号
    private String orderNo;            //银行订单号
    private String payWayId;          //原慧兜圈微信支付宝交易扫码支付方式码（不使用）
    private String wxAliPayOrderNo;         //应用版本v2.0.0前返回原慧兜圈平台订单号，应用版本v2.0.5前返回建行微信支付宝消费订单号，应用版本v2.0.5后统一返回建行微信支付宝订单号。
    private String wxAliPayUnionNo;        //应用版本v2.0.0前返回原慧兜圈支付联合订单号，应用版本v2.0.5前不返回，应用版本v2.0.5后统一返回建行微信支付宝订单号。
    private String wxAliPayOldOrderNo;      //应用版本v2.0.0前返回原慧兜圈原支付平台订单号，应用版本v2.0.5前微信支付宝退货订单号，应用版本v2.0.5后统一返回建行微信支付宝订单号。

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getOperID() {
        return operID;
    }

    public void setOperID(String operID) {
        this.operID = operID;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardIputMethod() {
        return cardIputMethod;
    }

    public void setCardIputMethod(String cardIputMethod) {
        this.cardIputMethod = cardIputMethod;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(String oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getReferInfo() {
        return referInfo;
    }

    public void setReferInfo(String referInfo) {
        this.referInfo = referInfo;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPrefrentialAmount() {
        return prefrentialAmount;
    }

    public void setPrefrentialAmount(String prefrentialAmount) {
        this.prefrentialAmount = prefrentialAmount;
    }

    public String getJhthOriginalAmount() {
        return jhthOriginalAmount;
    }

    public void setJhthOriginalAmount(String jhthOriginalAmount) {
        this.jhthOriginalAmount = jhthOriginalAmount;
    }

    public String getJhthDiscountAmount() {
        return jhthDiscountAmount;
    }

    public void setJhthDiscountAmount(String jhthDiscountAmount) {
        this.jhthDiscountAmount = jhthDiscountAmount;
    }

    public String getJhthCouponDesc() {
        return jhthCouponDesc;
    }

    public void setJhthCouponDesc(String jhthCouponDesc) {
        this.jhthCouponDesc = jhthCouponDesc;
    }

    public String getJhthAwardDesc() {
        return jhthAwardDesc;
    }

    public void setJhthAwardDesc(String jhthAwardDesc) {
        this.jhthAwardDesc = jhthAwardDesc;
    }

    public String getAhnTxnAmt() {
        return ahnTxnAmt;
    }

    public void setAhnTxnAmt(String ahnTxnAmt) {
        this.ahnTxnAmt = ahnTxnAmt;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(String saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getActivityAmt() {
        return activityAmt;
    }

    public void setActivityAmt(String activityAmt) {
        this.activityAmt = activityAmt;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCouponMsg() {
        return couponMsg;
    }

    public void setCouponMsg(String couponMsg) {
        this.couponMsg = couponMsg;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDuePoint() {
        return duePoint;
    }

    public void setDuePoint(String duePoint) {
        this.duePoint = duePoint;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(String pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(String numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public String getConsumedPoint() {
        return consumedPoint;
    }

    public void setConsumedPoint(String consumedPoint) {
        this.consumedPoint = consumedPoint;
    }

    public String getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(String exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public String getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(String exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }

    public String getOrgTraceNo() {
        return orgTraceNo;
    }

    public void setOrgTraceNo(String orgTraceNo) {
        this.orgTraceNo = orgTraceNo;
    }

    public String getOrgRefNo() {
        return orgRefNo;
    }

    public void setOrgRefNo(String orgRefNo) {
        this.orgRefNo = orgRefNo;
    }

    public String getOrgAmt() {
        return orgAmt;
    }

    public void setOrgAmt(String orgAmt) {
        this.orgAmt = orgAmt;
    }

    public String getLsOrderNo() {
        return lsOrderNo;
    }

    public void setLsOrderNo(String lsOrderNo) {
        this.lsOrderNo = lsOrderNo;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getInstallmentPeriod() {
        return installmentPeriod;
    }

    public void setInstallmentPeriod(String installmentPeriod) {
        this.installmentPeriod = installmentPeriod;
    }

    public String getInstallmentIsBigSale() {
        return installmentIsBigSale;
    }

    public void setInstallmentIsBigSale(String installmentIsBigSale) {
        this.installmentIsBigSale = installmentIsBigSale;
    }

    public String getDPDSvcID() {
        return DPDSvcID;
    }

    public void setDPDSvcID(String DPDSvcID) {
        this.DPDSvcID = DPDSvcID;
    }

    public String getIsScancodeSale() {
        return isScancodeSale;
    }

    public void setIsScancodeSale(String isScancodeSale) {
        this.isScancodeSale = isScancodeSale;
    }

    public String getUnionpayQRCode() {
        return unionpayQRCode;
    }

    public void setUnionpayQRCode(String unionpayQRCode) {
        this.unionpayQRCode = unionpayQRCode;
    }

    public String getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(String counterNo) {
        this.counterNo = counterNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayWayId() {
        return payWayId;
    }

    public void setPayWayId(String payWayId) {
        this.payWayId = payWayId;
    }

    public String getWxAliPayOrderNo() {
        return wxAliPayOrderNo;
    }

    public void setWxAliPayOrderNo(String wxAliPayOrderNo) {
        this.wxAliPayOrderNo = wxAliPayOrderNo;
    }

    public String getWxAliPayUnionNo() {
        return wxAliPayUnionNo;
    }

    public void setWxAliPayUnionNo(String wxAliPayUnionNo) {
        this.wxAliPayUnionNo = wxAliPayUnionNo;
    }

    public String getWxAliPayOldOrderNo() {
        return wxAliPayOldOrderNo;
    }

    public void setWxAliPayOldOrderNo(String wxAliPayOldOrderNo) {
        this.wxAliPayOldOrderNo = wxAliPayOldOrderNo;
    }
}
