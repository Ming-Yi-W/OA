package org.ming.oa.util.pager;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

//标签处理类，用于处理业务逻辑
public class PageTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageIndex;
	private int pageSize;
	private int totalNum;
	private String submitUrl; //index.action?pageIndex={0}
	private String pageStyle="digg";
	

	@Override
	public int doStartTag() throws JspException {
	// TODO Auto-generated method stub
//		System.out.println("doStartTag()-----");
		JspWriter jspwriter=this.pageContext.getOut();
		try {
			StringBuffer sb=new StringBuffer();
			
			//如果总记录数大于0， 显示两行信息
			if (this.totalNum>0) {
				
				String jumpurl="";
				StringBuffer sb1=new StringBuffer();
				//计算总页码 999 10 需要100页
				int totalPageNum=this.totalNum % this.pageSize == 0 ?this.totalNum / this.pageSize : (this.totalNum/ this.pageSize)+1;
				
				//当前页码在首页
				if(this.pageIndex==1) {
					//第一页上一页不能点击
					sb1.append("<span class='disabled'>上一页</span>");
					//处理中间页码
					funMiddlePage(sb1,totalPageNum);
					
					//如果总共只有一页，那么下一页也不能点击
					if (totalPageNum==1) {
						sb1.append("<span class='disabled'>下一页</span>");
					}else {
						//index.action?pageIndex={0}  {0}替换掉
						jumpurl=this.submitUrl.replace("{0}", String.valueOf(pageIndex+1));
						sb1.append("<a href='"+jumpurl+"'>下一页</a>");
					} 
					//当前页码在尾页
				}else if (this.pageIndex==totalPageNum) {
					//上一页一定可以点击，因为如果只有一页，则会进入上一个if条件里面
					jumpurl=this.submitUrl.replace("{0}", String.valueOf(pageIndex-1));
					sb1.append("<a href='"+jumpurl+"'>上一页</a>");
					
					//处理中间代码
					funMiddlePage(sb1,totalPageNum);
					//下一页不能点击
					sb1.append("<span class='disabled'>下一页</span>");
				}
				//当前页在中间页
				else {
					//上一页一定可以点击
					jumpurl=this.submitUrl.replace("{0}", String.valueOf(pageIndex-1));
					sb1.append("<a href='"+jumpurl+"'>上一页</a>");
					//中间页
					
					funMiddlePage(sb1,totalPageNum);
					//下一页一定可以点击
					jumpurl=this.submitUrl.replace("{0}", String.valueOf(pageIndex+1));
					sb1.append("<a href='"+jumpurl+"'>下一页</a>");
				}
				int startPageIndex=(pageIndex-1)*pageSize+1;	
				int endPageIndex=startPageIndex+pageSize-1>=totalNum?totalNum:startPageIndex+pageSize-1;
				
					sb.append("<table style='text-align:center; width:100%;'   class='"+this.pageStyle+"'><tr><td>"+sb1.toString());
					sb.append("&nbsp;跳转到  <input type='text' id='numInput' style='width:50px;'/>页<input type='button' onclick='abc()' value='跳转' /></td></tr>");
					sb.append("<tr><td>总共<font color='red'>"+totalNum+"</font>条记录，当前显示"+startPageIndex+"-"+endPageIndex+"条记录</td></tr></table>");
					
					sb.append("<script type='text/javascript'>");
					sb.append("function abc(){");
					sb.append("var num=document.getElementById('numInput').value;");
					//正则表达式不能有空格！！！！！
					sb.append("if (/^[1-9]\\d*$/.test(num) && num<="+totalPageNum+")");
					sb.append("{var submitUrl='"+this.submitUrl+"';");
					sb.append("submitUrl=submitUrl.replace('{0}',num);");
					sb.append("window.location=submitUrl;}");
					sb.append("else{alert('输入不正确');}");
					sb.append("document.getElementById('numInput').value='';");
					sb.append("}");
					sb.append("</script>");
			
			
			}else {
				//如果总记录数为0，  总共0条记录，当前0-0条记录
				sb.append("<table><tr><td>总共<font color='red'>0</font>条记录，当前显示0-0条记录</td></tr></table>");
			}
		
//			jspwriter.write("上一页 1 2 3 4 5 6 7 8 下一页");
			jspwriter.write(sb.toString());
			jspwriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	private void funMiddlePage(StringBuffer sb1, int totalPageNum) {
		//当在总页码数量小于10的 时候显示不需要...    eg.1 2 3 4 5 6 7 8 9 10
		String jumpurl="";
		if (totalPageNum<=10) {
			for(int i=1; i<=totalPageNum; i++) {
				if(i==this.pageIndex) {
					sb1.append("<span class='current'>"+i+"</span>");
				}else {
				jumpurl=this.submitUrl.replace("{0}", String.valueOf(i));
				sb1.append("<a href='"+jumpurl+"'>"+i+"</a>");
				}
			}
		}
		//当前页码靠近首页时，eg.1 2 3 4 5 6 7 8 9  ... 100
		else if(this.pageIndex<=8) {
			for (int i=1; i<=9;i++) {
				if (this.pageIndex==i) {
					sb1.append("<span class='current'>"+i+"</span>");
				}else {
				jumpurl=this.submitUrl.replace("{0}", String.valueOf(i));
				sb1.append("<a href='"+jumpurl+"'>"+i+"</a>");
				}
			}
			sb1.append("...");
			jumpurl=this.submitUrl.replace("{0}", String.valueOf(totalPageNum));
			sb1.append("<a href='"+jumpurl+"'>"+totalPageNum+"</a>");
		}
		//当前页码靠近尾页时，eg.1 ...91 92 93 94 95 96 97 98 99 100
		else if (this.pageIndex+8>=totalPageNum){
			
			jumpurl=this.submitUrl.replace("{0}", String.valueOf(1));
			sb1.append("<a href='"+jumpurl+"'>"+1+"</a>");
			sb1.append("...");
			
			for (int i =totalPageNum-9; i<=totalPageNum ;i++) {
				if (this.pageIndex==i) {
					sb1.append("<span class='current'>"+i+"</span>");
				}else {
				
				jumpurl=this.submitUrl.replace("{0}", String.valueOf(i));
				sb1.append("<a href='"+jumpurl+"'>"+i+"</a>");
				}
			}
			
		}
		//当前页码靠近中间，eg. 1... 44 45 46 47 48 49 50 51 52 ... 100 ,此时当前页为48
		else {
			jumpurl=this.submitUrl.replace("{0}", String.valueOf(1));
			sb1.append("<a href='"+jumpurl+"'>"+1+"</a>");
			
			sb1.append("...");
			for (int i=this.pageIndex-4; i<=this.pageIndex+4;i++) {
				if (i==this.pageIndex) {
					sb1.append("<span class='current'>"+i+"</span>");
				}else {
				jumpurl=this.submitUrl.replace("{0}", String.valueOf(i));
				sb1.append("<a href='"+jumpurl+"'>"+i+"</a>");
				}
			}
			
			sb1.append("...");
			jumpurl=this.submitUrl.replace("{0}", String.valueOf(totalPageNum));
			sb1.append("<a href='"+jumpurl+"'>"+totalPageNum+"</a>");
			
			
		}
		
		
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
//		System.out.println("set-->pageIndex->"+pageIndex);
		if (pageIndex==0){
			this.pageIndex=1;
		}else {
		this.pageIndex = pageIndex;
		}
	}

	public int getPageSize() {
//		System.out.println("get-->  pageIndex->"+pageIndex);
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(String pageStyle) {
		if (pageStyle!=null && !("".equals(pageStyle))) {
			this.pageStyle = pageStyle;
		}
		
	}
		
}
