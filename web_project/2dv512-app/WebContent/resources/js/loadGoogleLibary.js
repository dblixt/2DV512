function loadScript(src, callback) {
	var script = document.createElement("script");
	script.type = "text/javascript";
	if (callback)
		script.onload = callback;
	document.getElementsByTagName("head")[0].appendChild(script);
	script.src = src;
}

loadScript('https://maps.googleapis.com/maps/api/js?key=AIzaSyAZNbZP60oDu6EqMO6pQbV9n6YjjOYYsbw&signed_in=true&callback=initMap',
	function() {
		log('google-loader has been loaded, but not the maps-API ');
	});

function log(str) {
	document.getElementsByTagName('pre')[0].appendChild(document
			.createTextNode('[' + new Date().getTime() + ']\n' + str + '\n\n'));
}