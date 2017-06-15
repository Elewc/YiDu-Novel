package org.yidu.novel.action.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.yidu.novel.action.base.AbstractPublicBaseAction;
import org.yidu.novel.bean.PayReturnBean;
import org.yidu.novel.constant.YiDuConstants;
import org.yidu.novel.entity.TChargeOrder;
import org.yidu.novel.entity.TUser;
import org.yidu.novel.utils.DateUtils;
import org.yidu.novel.utils.LoginManager;
import org.yidu.novel.utils.MD5;
import org.yidu.novel.utils.Utils;



/**
 * <p>
 * 章节编辑Action
 * </p>
 * Copyright(c) 2013 YiDu-Novel. All rights reserved.
 * 
 * @version 1.1.9
 * @author shinpa.you
 */
@Action(value = "publicpay")
public class PublicPayAction extends AbstractPublicBaseAction {
    /**
     * 串行化版本统一标识符
     */
    private static final long serialVersionUID = -6064353669030314155L;
    /**
     * 功能名称。
     */
    public static final String NAME = "publicpay";
	public static final String SCAN_REQUEST_URL = "http://paytest.bhwjq.com/lpay/pay/gateway";
	public static final String MCH_ID = "10009";
	public static final String CALLBACK_URL = "http://m.qpk8.cn/paycallback";
    
    /**
     * 访问URL。
     */
    public static final String URL = NAMESPACE + "/" + NAME;
    private int chapterno;
    private Integer articleno;
    private String articlename;
    private String volumeid;
    private String chaptername;
    private Date postdate;
    private Integer chargefee;
    private String paytype;
    private String codeimgurl;
    private String orderno;    
    public int getChapterno() {
        return chapterno;
    }

    public void setChapterno(int chapterno) {
        this.chapterno = chapterno;
    }

    public Integer getArticleno() {
        return articleno;
    }

    public void setArticleno(Integer articleno) {
        this.articleno = articleno;
    }

    public void setArticleno(String articleno) {
        this.articleno = Integer.parseInt(articleno);
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public String getVolumeid() {
        return volumeid;
    }

    public void setVolumeid(String volumeid) {
        this.volumeid = volumeid;
    }


	public String getChaptername() {
		return chaptername;
	}

	public void setChaptername(String chaptername) {
		this.chaptername = chaptername;
	}

	public Date getPostdate() {
		return postdate;
	}

	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}

	public Integer getChargefee() {
		return chargefee;
	}

	public void setChargefee(Integer chargefee) {
		this.chargefee = chargefee;
	}
	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getCodeimgurl() {
		return codeimgurl;
	}

	public void setCodeimgurl(String codeimgurl) {
		this.codeimgurl = codeimgurl;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	@Override
    public String execute() {
        logger.debug("execute start.");
		String rand = Utils.getRandomString(0, 999999, 6);
		String time = DateUtils.getTimes();
		orderno = paytype.toString() + time + rand;
		String url= SCAN_REQUEST_URL+"?mch_id="+MCH_ID;
		url+="&total_fee="+String.valueOf(chargefee*100);
		url+="&out_trade_no="+orderno;
		url+="&pay_type="+paytype;
		url+="&callback_url="+CALLBACK_URL;
		//this.setCodeimgurl(payReturnBean.getCode_img_url());
		TChargeOrder tChargeOrder = new TChargeOrder();
		tChargeOrder.setFee(chargefee * 100);
		tChargeOrder.setOrderno(orderno);
		tChargeOrder.setUserno(LoginManager.getLoginUser().getUserno());
		tChargeOrder.setStatus(-1);
		tChargeOrder.setModifytime(new Date());
		orderService.save(tChargeOrder);
			  
        HttpSession session =  LoginManager.getSession(false);
        if (Utils.isDefined(session)) {
        	session.setAttribute("paypreurl", "/vip/"+getSubDir()+"/"+articleno+"/"+chapterno+".html");
        }
			   this.setForwardUrl(url);
        logger.debug("web pay normally end.");
        return GOTO_REDIRECT;
    }



	@Override
	public int getPageType() {
		// TODO Auto-generated method stub
		return YiDuConstants.Pagetype.PAGE_USER_CHARGE;
	}

	  @Override
	    public String getTempName() {
	        
	        if(chapterno==0){
		  		return "user/centerscanpay";
		  	} else {
		  		return "user/scanpay";
		  	}
	    }
    /**
     * 获取小说子目录
     * 
     * @return 小说子目录
     */
    @Deprecated
    public int getSubDir() {
        return articleno / YiDuConstants.SUB_DIR_ARTICLES;
    }

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		
	}  
}