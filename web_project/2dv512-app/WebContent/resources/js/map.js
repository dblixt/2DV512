// This example adds a search box to a map, using the Google Place Autocomplete
// feature. People can enter geographical searches. The search box will return a
// pick list containing a mix of places and predicted search terms.

var marker;

function initAutocomplete() {

	// freegeoip.net/{json}/{77.105.199.52}
	
	console.log("initAutocomplete() called");

	var map = new google.maps.Map(document.getElementById('map'), {
		center : {
			lat : 56.87900440,
			lng : 14.58612563
		},
		zoom : 8,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	});
	
	var searchBox = document.getElementById('pac-input');
	searchBox.style.display='none';

	marker = new google.maps.Marker({
		map : map,
		draggable : true,
		animation : google.maps.Animation.DROP,
	});

	marker.addListener('dragend', markerChangeLocation);

	// var infoWindow = new google.maps.InfoWindow({map: map});

	// Try HTML5 geolocation.
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {

			var lat = position.coords.latitude;
			var lng = position.coords.longitude;

			console.log("lat: " + lat);
			console.log("lng: " + lng);

			document.getElementById("formId:x").value = lat;
			document.getElementById("formId:y").value = lng;

			var pos = {
				lat : position.coords.latitude,
				lng : position.coords.longitude
			};

			marker.setPosition(pos);
			map.setCenter(pos);
		},

		// If geolocation is blocked
		function() {
			console.log("Geolocation is blocked");
			createSearchBox(map);
		});
	} else {
		// If geolocation is not available
		console.log("Geolocation is not available");
		createSearchBox(map);
	}

}

function createSearchBox(map) {

	console.log("createSearchBox called");
	
	
	var searchBox = document.getElementById('pac-input');
	searchBox.style.display='block';
	
	
	// Create the search box and link it to the UI element.
	var input = document.getElementById('pac-input');
	var searchBox = new google.maps.places.SearchBox(input);
	map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

	// Bias the SearchBox results towards current map's viewport.
	map.addListener('bounds_changed', function() {
		searchBox.setBounds(map.getBounds());
	});

	// Listen for the event fired when the user selects a prediction and
	// retrieve
	// more details for that place.
	searchBox.addListener('places_changed', function() {
		var places = searchBox.getPlaces();

		if (places.length == 0) {
			return;
		}

		// For each place, get the icon, name and location.
		var bounds = new google.maps.LatLngBounds();
		places.forEach(function(place) {
			var icon = {
				url : place.icon,
				size : new google.maps.Size(71, 71),
				origin : new google.maps.Point(0, 0),
				anchor : new google.maps.Point(17, 34),
				scaledSize : new google.maps.Size(25, 25)
			};

			marker.setPosition(place.geometry.location);
			markerChangeLocation();

			if (place.geometry.viewport) {
				// Only geocodes have viewport.
				bounds.union(place.geometry.viewport);
			} else {
				bounds.extend(place.geometry.location);
			}
		});
		map.fitBounds(bounds);
	});

}

function markerChangeLocation() {

	console.log("Marker Moved");

	var latlng = marker.getPosition();
	var lat = latlng.lat();
	var lng = latlng.lng();

	document.getElementById("formId:x").value = lat;
	document.getElementById("formId:y").value = lng;

	console.log(latlng.toString());

	// Marker Bonuceces
	marker.setAnimation(google.maps.Animation.BOUNCE);
	window.setTimeout(function() {
		if (marker.getAnimation() !== null) {
			marker.setAnimation(null);
		}
	}, 500);

}

// jQuery(document).ready(function($) {
// jQuery.getScript('http://www.geoplugin.net/javascript.gp', function()
// {
// var country = geoplugin_countryName();
// var zone = geoplugin_region();
// var district = geoplugin_city();
// console.log("Your location is: " + country + ", " + zone + ", " + district);
// });
// });
