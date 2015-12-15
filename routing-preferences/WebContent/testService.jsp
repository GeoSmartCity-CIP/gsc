<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="it.sinergis.routingpreferences.common.PropertyReader" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<!DOCTYPE html>
<html>

<head>
<title>Pagina di Test</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function showFields() {
	    var select = document.getElementById("servizioSelect");
	    if(select) {
		    var selected = select.options[select.selectedIndex].value;
		    
		    if(selected === 'save'){
		    	document.getElementById("text").style.display = "table-row";
		    	document.getElementById("userId").style.display = "table-row";
		    	document.getElementById("itineraryId").style.display = "table-row";
		    	document.getElementById("notes").style.display = "table-row";
	
		    	document.getElementById("textArea").title = "Insert a JSON itinerary.";
		    	document.getElementById("textArea").value = document.getElementById("hiddenJSONExample").value;
		    	
		    	document.getElementById("notesArea").title = "Insert additional notes.";
		    	document.getElementById("notesArea").value = "my notes.";
		    	
		    	document.getElementById("userIdArea").title = "Insert the user's id.";
		    	document.getElementById("userIdArea").value = "1";
	
		    	document.getElementById("itineraryIdArea").title = "Insert the selected itinerary index among the various JSON itineraries.";
		    	document.getElementById("itineraryIdArea").value = "1";
		    	
		    	
		    } else if(selected === 'read'){
		    	document.getElementById("text").style.display = "table-row";
		    	document.getElementById("userId").style.display = "none";
		    	document.getElementById("itineraryId").style.display = "none";
		    	document.getElementById("notes").style.display = "none";
		    	
		    	document.getElementById("textArea").title = "Insert a research query.";
		    	document.getElementById("textArea").value = document.getElementById("hiddenQueryExample").value;
		    	
		    } else if(selected === 'saveOrUpdatePreferences'){
		    	document.getElementById("text").style.display = "table-row";
		    	document.getElementById("userId").style.display = "none";
		    	document.getElementById("itineraryId").style.display = "none";
		    	document.getElementById("notes").style.display = "none";
		    	document.getElementById("textArea").title = "Insert a valid json.";
		    	document.getElementById("textArea").value = document.getElementById("hiddenJSONExampleSaveOrUpdatePreferences").value;	
		    } else if(selected === 'getPreferences'){   	
				document.getElementById("text").style.display = "table-row";
		    	document.getElementById("userId").style.display = "none";
		    	document.getElementById("itineraryId").style.display = "none";
		    	document.getElementById("notes").style.display = "none";
		    	document.getElementById("textArea").title = "Insert a valid json.";
		    	document.getElementById("textArea").value = document.getElementById("hiddenJSONExampleGetPreferences").value;	
		    } else if(selected === 'deletePreferences'){
				document.getElementById("text").style.display = "table-row";
		    	document.getElementById("userId").style.display = "none";
		    	document.getElementById("itineraryId").style.display = "none";
		    	document.getElementById("notes").style.display = "none";
		    	document.getElementById("textArea").title = "Insert a valid json.";
		    	document.getElementById("textArea").value = document.getElementById("hiddenJSONExampleDeletePreferences").value;  	
		    }
	    	document.getElementById("submitButton").style.display = "table-row";
	    	document.getElementById("response").style.display = "table-row";
	    }
	};
</script>
<STYLE type="text/css">
body,tr,td {
	font-size: 12px;
	font-family: Verdana
}
</STYLE>
</head>
<body>
<table>
	<tr>
		<td><b>
		<h2>Test page</h2>
		</b></td>
	</tr>
</table>
<% PropertyReader pr = new PropertyReader("routingpreferences.properties"); %>
<input type="hidden" id="hiddenJSONExample" value="<%  out.print(StringEscapeUtils.escapeHtml4(pr.getValue("JSONinputExample").replace("\\","\\\\").replaceAll("\\p{Cntrl}", "")));%>"></input>
<input type="hidden" id="hiddenJSONExampleSaveOrUpdatePreferences" value="<%  out.print(StringEscapeUtils.escapeHtml4(pr.getValue("JSONinputExampleSaveOrUpdatePreferences").replace("\\","\\\\").replaceAll("\\p{Cntrl}", "")));%>"></input>
<input type="hidden" id="hiddenJSONExampleGetPreferences" value="<%  out.print(StringEscapeUtils.escapeHtml4(pr.getValue("JSONinputExampleGetPreferences").replace("\\","\\\\").replaceAll("\\p{Cntrl}", "")));%>"></input>
<input type="hidden" id="hiddenJSONExampleDeletePreferences" value="<%  out.print(StringEscapeUtils.escapeHtml4(pr.getValue("JSONinputExampleDeletePreferences").replace("\\","\\\\").replaceAll("\\p{Cntrl}", "")));%>"></input>

<input type="hidden" id="hiddenQueryExample" value="<% out.print(pr.getValue("queryExample"));%>"></input>
<form action="saveJSONServlet" method="post" id="serviceForm" accept-charset="UTF-8">
<%
String servizio=(String)request.getAttribute("servizio");
if (servizio==null) {
	servizio="";
} else {
	{ %> <script type="text/javascript"> showFields(); </script> <% }
}

%>
<table>
	<tr>
		<td>Service</td>
		<td><select name="servizio" id="servizioSelect" onchange="showFields()">
			<option value="ChooseService" >Choose service...</option>
			<option value="save" <%= servizio.equals("save") ? "selected=\"selected\"" : ""%>>Save itinerary</option>
			<option value="read" <%= servizio.equals("read") ? "selected=\"selected\"" : ""%>>Read itinerary</option>
			<option value="saveOrUpdatePreferences" <%= servizio.equals("saveOrUpdatePreferences") ? "selected=\"selected\"" : ""%>>Save/Update preferences</option>
			<option value="getPreferences" <%= servizio.equals("getPreferences") ? "selected=\"selected\"" : ""%>>Get preferences</option>
			<option value="deletePreferences" <%= servizio.equals("deletePreferences") ? "selected=\"selected\"" : ""%>>Delete preferences</option>
			<option value=""></option>
		</select></td>
	</tr>
	<tr id="text" style="display:none">	 
		<td>Text *</td>
		<td><textarea id="textArea" name="text" cols="120" rows="30"><%if (request.getAttribute("text") != null)
				out.print(request.getAttribute("text"));%></textarea></td>
	</tr>
	<tr id="userId" style="display:none">	 
		<td>User id *</td>
		<td><input id="userIdArea" name="userId" <%if (request.getAttribute("userId") != null)
				out.print(request.getAttribute("userId"));%>/></td>
	</tr>
	<tr id="itineraryId" style="display:none">	 
		<td>Itinerary id *</td>
		<td><input id="itineraryIdArea" name="itineraryId" <%if (request.getAttribute("itineraryId") != null)
				out.print(request.getAttribute("itineraryId"));%>/></td>
	</tr>
	<tr id="notes" style="display:none">	 
		<td>Notes *</td>
		<td><input id="notesArea" name="notes" <%if (request.getAttribute("notes") != null)
				out.print(request.getAttribute("notes"));%>/></td>
	</tr>
	<tr style="display:none" id="submitButton">
		<td></td>
		<td>
		<INPUT type="submit" value="Invia">
		</td>
	</tr>
  	<tr style="display:none" id="response">
		<td>Response</td>
		<td><textarea name="responseJSON" cols="120" rows="15"><%
			if (request.getAttribute("responseJSON") != null)
				out.print(request.getAttribute("responseJSON"));%></textarea></td>
	</tr> 
</table>
<%
servizio=(String)request.getAttribute("servizio");
if (servizio != "") {
	{ %> <script type="text/javascript"> showFields(); </script> <% }
}

%>
</form>
</body>
</html>
