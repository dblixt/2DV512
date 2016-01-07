var map;
var marker;

var latitude;
var longitude;

var allowEditLocation = false;
var allowGeoLocation = true;
var locationSet = true;

var allowControl = true;

function initMap() {
	console.log("initMap()");
	
	var mycenter = {
			lat: latitude, 
			lng: longitude
	};
	
	var prop = {
		center:  mycenter,
		zoom: (locationSet ? 6 : 1),
		mapTypeControl: false,
		streetViewControl: false,
		scrollwheel: allowControl
	};

	map = new google.maps.Map(document.getElementById('map'), prop);
	
	
	if(allowEditLocation) {
		createSearchBox();
	}
	
	if(locationSet) {
		createMarker();
		setLocation(latitude, longitude); // refresh marker location.
	}
	
	if(!locationSet && allowEditLocation && allowGeoLocation) {
		requestGeoLocation();
	}
}

function createMarker() {
	marker = new google.maps.Marker({
		map: map,
		draggable : allowEditLocation,
		animation : google.maps.Animation.DROP,
	});

	if(allowEditLocation) {
		marker.addListener('dragend', onMarkerMoved);
	}
}


function requestGeoLocation() {
	console.log("requestGeoLocation()");
	
	// Try HTML5 geolocation.
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {

			latitude = position.coords.latitude;
			longitude = position.coords.longitude;

			console.log("lat: " + latitude);
			console.log("lng: " + longitude);

			// no location was previously set, create new marker.
			if(!marker) {
				createMarker(); 
			}
			updateLocation();
		},
		function() {
			console.log("Geolocation is blocked.");
		});
	} 
	else {
		console.log("Geolocation is not available.");
	}
}

function createSearchBox() {
	console.log("createSearchBox()");
		
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

			// no location was previously set, create new marker.
			if(!marker) {
				createMarker(); 
			}
			
			marker.setPosition(place.geometry.location);
			onMarkerMoved();

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

function onMarkerMoved() {
	console.log("onMarkerMoved()");

	var latlng = marker.getPosition();

	document.getElementById("lat").value = latlng.lat();
	document.getElementById("lng").value = latlng.lng();
	
	// Marker bounce animation
	marker.setAnimation(google.maps.Animation.BOUNCE);
	window.setTimeout(function() {
		if (marker.getAnimation() !== null) {
			marker.setAnimation(null);
		}
	}, 500);

}


function updateLocation() {	
	if(marker) {
		var pos = {
			lat: latitude,
			lng: longitude
		};

		marker.setPosition(pos);
		map.setCenter(pos);
		
		if(allowEditLocation) {
			document.getElementById("lat").value = latitude;
			document.getElementById("lng").value = longitude;
		}		
	}
}

function setLocation(lat, lng) {
	if(lat == 0 && lng == 0) {
		locationSet = false;
	}
	else {
		locationSet = true;
	}
	
	latitude = lat;
	longitude = lng;
	
	updateLocation();
}

function setAllowControl(allowCont) {
	allowControl = allowCont;
}

function setAllowEdit(allowEdit) {
	allowEditLocation = allowEdit;
}

function setAllowGeoLocation(allowGeo) {
	allowGeoLocation = allowGeo;
}
