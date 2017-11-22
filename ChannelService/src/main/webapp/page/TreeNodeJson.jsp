<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="len" value="${fn:length(list)-1}" ></c:set>
[
<c:forEach items="${list}" var="obj" varStatus="i">
{
	id:'${obj.id}',
	text:'${obj.name}',
	leaf: ${obj.leaf},
	singleClickExpand:true,
	items:[
	{
		id:'${obj.id}@url_S0',
		text:'${obj.url}'
	},{
		id:'${obj.id}@level_S1',
		text:'${obj.level}'
	}]
}
	<c:if test="${i.index<len}">,</c:if>
</c:forEach>
]