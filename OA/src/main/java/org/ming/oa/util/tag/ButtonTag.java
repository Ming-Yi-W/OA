package org.ming.oa.util.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ButtonTag extends TagSupport {
	
	
	private String permiss;

	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		
		//从session中获取用户的权限信息
		List<String> permissions = (List<String>)this.pageContext.getSession().getAttribute("userOperas");
		if(permissions.indexOf(permiss) != -1) {
			
			return EVAL_BODY_INCLUDE;
		}else {
			//SKIP_BODY：忽略开始标签和结束标签之间的内容  
			return SKIP_BODY;
		}
	}

	public String getPermiss() {
		return permiss;
	}

	public void setPermiss(String permiss) {
		this.permiss = permiss;
	}
	
	
}
