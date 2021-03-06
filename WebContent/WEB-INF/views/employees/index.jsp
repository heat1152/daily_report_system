<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>従業員 一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                    <th>フォロー状況</th>
                    <th>フォロワー</th>
                </tr>
                <c:forEach var="employee"  items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.id}"/></td>
                        <td><c:out value="${employee.name}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${employee.delete_flag == 1}">
                                    (削除済み)
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/show?id=${employee.id}'/>">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <!-- フォロー状況チェック -->
                        <td>
                            <c:set var="follow_check_1">0</c:set>
                            <c:set var="follow_check_2">0</c:set>
                            <c:forEach var="follow" items="${follows}">
                                <c:choose>
                                    <c:when test="${employee.id == login_employee.id}">

                                    </c:when>
                                    <c:when test="${employee.id == follow.id}">
                                        <form method="post" action="<c:url value='/follow/update'/>">
                                            <button type="submit" name="follow_employee_id" value="${employee.id}">フォロー解除</button>
                                            <input type="hidden" name="follow_update_check" value="1">
                                        </form>
                                        <c:set var="follow_check_1">1</c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="follow_check_2">1</c:set>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                            <c:when test="${employee.id == login_employee.id}">

                            </c:when>
                            <c:when test="${follow_check_1 == 0 and follow_check_2 == 1 and employee.delete_flag != 1 or follow_check_1 == 0 and follow_check_2 == 0 and employee.delete_flag != 1}">
                                <form method="post" action="<c:url value='/follow/update'/>">
                                    <button type="submit" name="follow_employee_id" value="${employee.id}">フォロー</button>
                                    <input type="hidden" name="follow_update_check" value="0">
                                </form>
                            </c:when>
                            </c:choose>
                            <c:if test="${employee.delete_flag == 1}">
                                (削除済み)
                            </c:if>
                        </td>



                        <!-- フォロワー状況チェック -->
                        <td>
                            <c:set var="follower_check_1">0</c:set>
                            <c:set var="follower_check_2">0</c:set>
                            <c:forEach var="follower" items="${followers}">
                                <c:choose>
                                    <c:when test="${employee.id == login_employee.id}">

                                    </c:when>
                                    <c:when test="${employee.id == follower.id}">
                                        あなたをフォローしています。
                                        <c:set var="follower_check_1">1</c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="follower_check_2">1</c:set>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${employee.id == login_employee.id}">

                                </c:when>
                                <c:when test="${follower_check_1 == 0 and follower_check_2 == 1 and employee.delete_flag != 1 or follower_check_1 == 0 and follower_check_2 == 0 and employee.delete_flag != 1}">
                                    あなたをフォローしていません。
                                </c:when>
                            </c:choose>
                            <c:if test="${employee.delete_flag == 1}">
                                (削除済み)
                            </c:if>
                        </td>
                    </c:forEach>
                </tbody>
            </table>


        <div id="pagination">
            (全${employees_count}件)<br/>
            <c:forEach var="i" begin="1" end="${((employees_count - 1)/15)+1}" step="1">
                <c:choose>
                    <c:when test="${i ==page}">
                        <c:out value="${i}"/>&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}'/>"><c:out value="${i}"/></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <c:if test="${login_employee.admin_flag == 1}">
            <p><a href="<c:url value='/employees/new'/>">新規従業員の登録</a></p>
        </c:if>
    </c:param>
</c:import>



