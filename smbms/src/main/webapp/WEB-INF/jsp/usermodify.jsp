<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <div class="right">
        <div class="location">
            <strong>你现在所在的位置是:</strong>
            <span>用户管理页面 >> 用户修改页面</span>
        </div>
        <div class="providerAdd">
        <form id="userForm"  name="userForm" enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath }/user/modifysave.html">
			<input type="hidden" name="method" value="modifyexe">
			<input type="hidden" name="id" value="${user.id }"/>
			 <div>
                    <label for="userName">用户名称：</label>
                    <input type="text" name="userName" id="userName" value="${user.userName }"> 
					<font color="red"></font>
             </div>
			 <div>
                    <label >用户性别：</label>
                    <select name="gender" id="gender">
						<c:choose>
							<c:when test="${user.gender == 1 }">
								<option value="1" selected="selected">男</option>
					    		<option value="2">女</option>
							</c:when>
							<c:otherwise>
								<option value="1">男</option>
					    		<option value="2" selected="selected">女</option>
							</c:otherwise>
						</c:choose>
					 </select>
             </div>
			 <div>
                    <label for="data">出生日期：</label>

                    <input type="text" Class="Wdate" id="birthday" name="birthday"value= <fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>
					readonly="readonly" onclick="WdatePicker();">
                    <font color="red"></font>
              </div>
			
		       <div>
                    <label for="userphone">用户电话：</label>
                    <input type="text" name="phone" id="phone" value="${user.phone }"> 
                    <font color="red"></font>
               </div>
                <div>
                    <label for="userAddress">用户地址：</label>
                    <input type="text" name="address" id="address" value="${user.address }">
                </div>
				<div>
                    <label >用户角色：</label>
                    <!-- 列出所有的角色分类 -->
					<input type="hidden" value="${user.userRole }" id="rid" />
					<select name="userRole" id="userRole">
                        <option value="1" <c:if test="${user.userRole eq 1}">selected</c:if>>系统管理员</option>
                        <option value="2" <c:if test="${user.userRole eq 2}">selected</c:if>>经理</option>
                        <option value="3" <c:if test="${user.userRole eq 3}">selected</c:if>>员工</option>
                    </select>
        			<font color="red"></font>
                </div>
           <c:if test="${str==null}">
                <div>
                    <label for="a_idPicPath">证件照：</label>
                    <input type="file" name="a_idPicPath" id="a_idPicPath"/>
                    <font color="red">${uploadFileError}</font>
                </div>
            </c:if>
            <c:if test="${str!=null}">
            <div>
                <label>证件照：</label>
               <img src="../statics/upload/${str}">
            </div>
            </c:if>

			 <div class="providerAddBtn">
                    <input type="button" name="save" id="save" value="保存" />
                    <input type="button" id="back" name="back" value="返回"/>
                </div>
            </form>
        </div>
    </div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/usermodify.js"></script>
