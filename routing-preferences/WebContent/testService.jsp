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
	    var selected = select.options[select.selectedIndex].value;
	    
	    if(selected === 'save'){
	    	document.getElementById("text").style.display = "table-row";
	    	document.getElementById("userId").style.display = "table-row";
	    	document.getElementById("itineraryId").style.display = "table-row";
	    	document.getElementById("notes").style.display = "table-row";
	    	document.getElementById("maxWalkingDistance").style.display = "none";
	    	document.getElementById("walkingSpeed").style.display = "none";
	    	document.getElementById("maxBikingDistance").style.display = "none";
	    	document.getElementById("bikingSpeed").style.display = "none";

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
	    	document.getElementById("maxWalkingDistance").style.display = "none";
	    	document.getElementById("walkingSpeed").style.display = "none";
	    	document.getElementById("maxBikingDistance").style.display = "none";
	    	document.getElementById("bikingSpeed").style.display = "none";

	    	document.getElementById("textArea").title = "Insert a research query.";
	    	document.getElementById("textArea").value = document.getElementById("hiddenQueryExample").value;
	    	
	    } else if(selected === 'savePreferences'){
	    	document.getElementById("text").style.display = "none";
	    	document.getElementById("userId").style.display = "table-row";
	    	document.getElementById("itineraryId").style.display = "none";
	    	document.getElementById("notes").style.display = "none";
	    	document.getElementById("maxWalkingDistance").style.display = "table-row";
	    	document.getElementById("walkingSpeed").style.display = "table-row";
	    	document.getElementById("maxBikingDistance").style.display = "table-row";
	    	document.getElementById("bikingSpeed").style.display = "table-row";

	    	document.getElementById("userIdArea").title = "Insert the user's id.";
	    	document.getElementById("userIdArea").value = "1";

	    	document.getElementById("maxWalkingDistanceArea").title = "Insert the desired maximum distance to be covered on foot.";
	    	document.getElementById("maxWalkingDistanceArea").value = "1.1";

	    	document.getElementById("walkingSpeedArea").title = "Insert the desired walking speed.";
	    	document.getElementById("walkingSpeedArea").value = "1.1";

	    	document.getElementById("maxBikingDistanceArea").title = "IInsert the desired maximum distance to be covered by bike.";
	    	document.getElementById("maxBikingDistanceArea").value = "1.1";

	    	document.getElementById("bikingSpeedArea").title = "Insert the desired biking speed.";
	    	document.getElementById("bikingSpeedArea").value = "1.1";
	    	
	    } else if(selected === 'readPreferences'){
	    	document.getElementById("text").style.display = "none";
	    	document.getElementById("userId").style.display = "table-row";
	    	document.getElementById("itineraryId").style.display = "none";
	    	document.getElementById("notes").style.display = "none";
	    	document.getElementById("maxWalkingDistance").style.display = "none";
	    	document.getElementById("walkingSpeed").style.display = "none";
	    	document.getElementById("maxBikingDistance").style.display = "none";
	    	document.getElementById("bikingSpeed").style.display = "none";

	    	document.getElementById("userIdArea").title = "Insert the user's id.";
	    	document.getElementById("userIdArea").value = "1";
	    	
	    } else if(selected === 'deletePreferences'){
	    	document.getElementById("text").style.display = "none";
	    	document.getElementById("userId").style.display = "table-row";
	    	document.getElementById("itineraryId").style.display = "none";
	    	document.getElementById("notes").style.display = "none";
	    	document.getElementById("maxWalkingDistance").style.display = "none";
	    	document.getElementById("walkingSpeed").style.display = "none";
	    	document.getElementById("maxBikingDistance").style.display = "none";
	    	document.getElementById("bikingSpeed").style.display = "none";

	    	document.getElementById("userIdArea").title = "Insert the user's id.";
	    	document.getElementById("userIdArea").value = "1";
	    	
	    }
    	document.getElementById("submitButton").style.display = "table-row";
    	document.getElementById("response").style.display = "table-row";
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
			<option value="savePreferences" <%= servizio.equals("savePreferences") ? "selected=\"selected\"" : ""%>>Save preferences</option>
			<option value="readPreferences" <%= servizio.equals("readPreferences") ? "selected=\"selected\"" : ""%>>read preferences</option>
			<option value="deletePreferences" <%= servizio.equals("deletePreferences") ? "selected=\"selected\"" : ""%>>Delete preferences</option>
			<option value=""></option>
		</select></td>
	</tr>
	<tr id="text" style="display:none">	 
		<td>Text</td>
		<td><textarea id="textArea" name="text" cols="120" rows="30"><%if (request.getAttribute("text") != null)
				out.print(request.getAttribute("text"));%></textarea></td>
	</tr>
	<tr id="userId" style="display:none">	 
		<td>User id *</td>
		<td><input id="userIdArea" name="userId" <%if (request.getAttribute("userId") != null)
				out.print(request.getAttribute("userId"));%>/></td>
	</tr>
	<tr id="itineraryId" style="display:none">	 
		<td>Itinerary id</td>
		<td><input id="itineraryIdArea" name="itineraryId" <%if (request.getAttribute("itineraryId") != null)
				out.print(request.getAttribute("itineraryId"));%>/></td>
	</tr>
	<tr id="notes" style="display:none">	 
		<td>Notes</td>
		<td><input id="notesArea" name="notes" <%if (request.getAttribute("notes") != null)
				out.print(request.getAttribute("notes"));%>/></td>
	</tr>
	<tr id="maxWalkingDistance" style="display:none">	 
		<td>Max. walking distance</td>
		<td><input id="maxWalkingDistanceArea" name="maxWalkingDistance"<%if (request.getAttribute("maxWalkingDistance") != null)
				out.print(request.getAttribute("maxWalkingDistance"));%>/></td>
	</tr>
	<tr id="walkingSpeed" style="display:none">	 
		<td>Walking speed</td>
		<td><input id="walkingSpeedArea" name="walkingSpeed"<%if (request.getAttribute("walkingSpeed") != null)
				out.print(request.getAttribute("walkingSpeed"));%>/></td>
	</tr>
	<tr id="maxBikingDistance" style="display:none">	 
		<td>Max. biking distance</td>
		<td><input id="maxBikingDistanceArea" name="maxBikingDistance"<%if (request.getAttribute("maxBikingDistance") != null)
				out.print(request.getAttribute("maxBikingDistance"));%>/></td>
	</tr>
	<tr id="bikingSpeed" style="display:none">	 
		<td>Biking speed</td>
		<td><input id="bikingSpeedArea" name="bikingSpeed"<%if (request.getAttribute("bikingSpeed") != null)
				out.print(request.getAttribute("bikingSpeed"));%>/></td>
	</tr>

	<tr style="display:none" id="submitButton">
		<td></td>
		<td>
		<INPUT type="submit" value="Invia">
		</td>
	</tr>
  	<tr style="display:none" id="response">
		<td>Response</td>
		<td><textarea name="risposta" cols="120" rows="15"><%
			if (request.getAttribute("risposta") != null)
				out.print(request.getAttribute("risposta"));%></textarea></td>
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
