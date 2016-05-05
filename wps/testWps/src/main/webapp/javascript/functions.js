function sendRequest() {
	document.getElementById('sendButton').textContent = 'Loading...';
	document.getElementById('sendButton').disabled = true;
	var request = document.getElementById("textArea").value
	
	var select = document.getElementById("servizioSelect");
	if (select) {
		var selected = select.options[select.selectedIndex].value;

		if (selected === 'solarWps') {
			//devo aggiungere la mail nella richiesta
			if (document.getElementById("email").value != '') {
				var lastInput = request.lastIndexOf("</wps:Input>");
				request = replace(request, lastInput, '</wps:Input><wps:Input><ows:Identifier>email</ows:Identifier><wps:Data><wps:LiteralData>'+document.getElementById("email").value+'</wps:LiteralData></wps:Data></wps:Input>');
			}
		}
	}
	
	
	$.ajax({
		type : "POST",
		url : getUrlWithProxyAddService(urlWps),
		data : (request),
		dataType : "xml",
		contentType : "text/xml"
	}).done(
		function (xml) {
			var response = xml.documentElement.outerHTML;
			if (response.indexOf('statusLocation=') != -1) {
				document.getElementById('divStatusLocation').style = 'display: block;';
				var statusLocation = response.substring(response.indexOf('statusLocation')+'statusLocation='.length, response.indexOf(' ',response.indexOf('statusLocation=')));
				statusLocation = statusLocation.replace(/"/g, '');
				document.getElementById("statusLocation").href = statusLocation.replace(/&amp;/g, '&');
			}
			document.getElementById("response").value = xml.documentElement.outerHTML;
			document.getElementById('sendButton').textContent = 'Send';
			document.getElementById('sendButton').disabled = false;
	}).error(
			function (error) {
				alert(error.status + " " +error.statusText)
				document.getElementById('sendButton').textContent = 'Send';
				document.getElementById('sendButton').disabled = false;
			}
	);

}

function getUrlWithProxyAddService(urlServizio) {
	if (urlServizio.indexOf('http://') != -1) {
		var proxyUrl = window.location.origin + window.location.pathname + "call/";
		
		if (window.location.pathname.indexOf("index.html") != -1) {
			var proxyUrl = window.location.origin
			 + window.location.pathname.substring(0,
				window.location.pathname.indexOf("index.html", 0))
			 + "call/";
		} 
		console.log(proxyUrl + urlServizio.substring('http://'.length));
		return proxyUrl + urlServizio.substring('http://'.length);
	}
}

function replace(request, index, character) {
	return request.substr(0, index) + character + request.substr(index+'</wps:Input>'.length);
}

function showFields() {
	document.getElementById('divStatusLocation').style = 'display: none;';
	document.getElementById('divEmail').style = 'display: none;';
	document.getElementById("response").value = '';
	document.getElementById("textArea").value = '';

	var select = document.getElementById("servizioSelect");
	if (select) {
		var selected = select.options[select.selectedIndex].value;

		if (selected === 'solarWps') {
			document.getElementById('divEmail').style = 'display: block;';

			document.getElementById("textArea").style = 'height:800px';
			document.getElementById("response").style = 'height:260px';
			document.getElementById("textArea").value = wpsSolarPotentialCalculation;

		} else if (selected == 'co2Wps') {
			document.getElementById("textArea").style = 'height:800px';
			document.getElementById("response").style = 'height:800px';
			document.getElementById("textArea").value = wpsBuildingsCO2emissionsEstimation;
		
		} else if (selected == 'energyPerformance') {
			document.getElementById("textArea").style = 'height:400px';
			document.getElementById("response").style = 'height:400px';
			document.getElementById("textArea").value = wpsEnergyPerformance;
			
		} else if (selected == 'ChooseService') {
			document.getElementById("textArea").style = 'height:50px';
			document.getElementById("response").style = 'height:50px';
		}

	}
};