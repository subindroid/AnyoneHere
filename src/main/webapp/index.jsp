<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<html>
<head>
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<title>차쟁이</title>
<style>
    #map { height: 500px; border-radius: 12px; }
    .district-label { font-size: 11px; font-weight: bold; color: #333; background: none; border: none; box-shadow: none; white-space: nowrap; text-align: center; }
    .dong-label { font-size: 10px; color: #444; background: none; border: none; box-shadow: none; white-space: nowrap; text-align: center; }
    .leaflet-control-attribution { display: none; }
</style>
</head>
<body>
	<div class="container py-4">
		<%@ include file="common/menu.jsp"%>

		<%!String greeting = "차쟁이";
		String tagline = "Anyone Here?";
		String description = "차쟁이들을 위한 공간!";%>



		<div class="row align-items-md-stretch   text-center">
			<div class="col-md-12">
				<div class="h-100 p-5">
					<h3><%=tagline%></h3>
					<p>
					<h5><%=description%></h5>


		<div class="row mb-4">
			<div class="col-12">
				<h5 class="mb-3"><b>주변 스팟 보기</b></h5>
				<div id="map"></div>
			</div>
		</div>
		<%@ include file="common/footer.jsp"%>
	</div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    var DONG_ZOOM = 13;
    var GYEONGGI_COLOR = '#c8cfd4';

    var map = L.map('map', {
        center: [37.6, 127.1],
        zoom: 9,
        minZoom: 8,
        maxZoom: 16,
        zoomControl: true
    });

    var sectionColors = {
        'NW': '#90b8d8', 'NE': '#90c490',
        'SW': '#d8bc88', 'SE': '#b890c8'
    };

    function getSectionByCoord(lat, lng) {
        var isNorth = lat > 37.522;
        var isWest  = lng < 127.00;
        if (isNorth && isWest)  return 'NW';
        if (isNorth && !isWest) return 'NE';
        if (!isNorth && isWest) return 'SW';
        return 'SE';
    }

    function toMainPolygon(feature) {
        if (feature.geometry.type === 'MultiPolygon') {
            var largest = feature.geometry.coordinates.reduce(function(max, coords) {
                return coords[0].length > max[0].length ? coords : max;
            });
            return Object.assign({}, feature, {
                geometry: { type: 'Polygon', coordinates: largest }
            });
        }
        return feature;
    }

    function addLabel(feature, className, getText) {
        var center = L.geoJSON(feature).getBounds().getCenter();
        return L.marker(center, {
            icon: L.divIcon({
                className: className,
                html: getText(feature),
                iconSize: [80, 20], iconAnchor: [40, 10]
            })
        });
    }

    var seoulGuLayer, seoulGuLabels = [];
    var gyeonggiLayer, gyeonggiLabels = [];
    var seoulDongLayer, seoulDongLabels = [];
    var gyeonggiDongLayer, gyeonggiDongLabels = [];
    var seoulDongLoaded = false, gyeonggiDongLoaded = false;

    fetch('https://raw.githubusercontent.com/southkorea/seoul-maps/master/kostat/2013/json/seoul_municipalities_geo_simple.json')
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var cleaned = data.features.map(toMainPolygon);
            seoulGuLayer = L.geoJSON({ type: 'FeatureCollection', features: cleaned }, {
                style: function(f) {
                    var c = L.geoJSON(f).getBounds().getCenter();
                    return { fillColor: sectionColors[getSectionByCoord(c.lat, c.lng)], fillOpacity: 0.85, color: '#fff', weight: 2 };
                }
            }).addTo(map);
            cleaned.forEach(function(f) {
                var lbl = addLabel(f, 'district-label', function(f) { return f.properties.name || ''; });
                lbl.addTo(map);
                seoulGuLabels.push(lbl);
            });
            var han = [[37.560,126.795],[37.548,126.840],[37.540,126.865],[37.532,126.893],
                       [37.524,126.920],[37.520,126.942],[37.517,126.970],[37.513,126.995],
                       [37.512,127.015],[37.518,127.038],[37.526,127.058],[37.530,127.080],
                       [37.533,127.100],[37.537,127.135],[37.543,127.165]];
            L.polyline(han, { color: '#4a9abe', weight: 12, opacity: 0.9, lineJoin: 'round', lineCap: 'round' }).addTo(map);
        });

    fetch('https://raw.githubusercontent.com/southkorea/southkorea-maps/master/kostat/2013/json/skorea_municipalities_geo_simple.json')
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var gyeonggi = data.features
                .filter(function(f) { return (f.properties.code || '').startsWith('31'); })
                .map(toMainPolygon);
            gyeonggiLayer = L.geoJSON({ type: 'FeatureCollection', features: gyeonggi }, {
                style: { fillColor: GYEONGGI_COLOR, fillOpacity: 0.8, color: '#fff', weight: 1.5 }
            }).addTo(map);
            gyeonggi.forEach(function(f) {
                var lbl = addLabel(f, 'district-label', function(f) { return f.properties.name || ''; });
                lbl.addTo(map);
                gyeonggiLabels.push(lbl);
            });
        });

    function loadSeoulDong() {
        if (seoulDongLoaded) return;
        seoulDongLoaded = true;
        fetch('https://raw.githubusercontent.com/southkorea/seoul-maps/master/kostat/2013/json/seoul_submunicipalities_geo_simple.json')
            .then(function(r) { return r.json(); })
            .then(function(data) {
                var cleaned = data.features.map(toMainPolygon);
                seoulDongLayer = L.geoJSON({ type: 'FeatureCollection', features: cleaned }, {
                    style: function(f) {
                        var c = L.geoJSON(f).getBounds().getCenter();
                        return { fillColor: sectionColors[getSectionByCoord(c.lat, c.lng)], fillOpacity: 0.75, color: '#fff', weight: 1 };
                    }
                });
                cleaned.forEach(function(f) {
                    var parts = (f.properties.name || '').split(' ');
                    var dongName = parts.slice(1).join(' ') || parts[0];
                    var lbl = addLabel(f, 'dong-label', function() { return dongName; });
                    seoulDongLabels.push(lbl);
                });
                if (map.getZoom() >= DONG_ZOOM) showDongLevel();
            });
    }

    function loadGyeonggiDong() {
        if (gyeonggiDongLoaded) return;
        gyeonggiDongLoaded = true;
        fetch('https://raw.githubusercontent.com/southkorea/southkorea-maps/master/kostat/2013/json/skorea_submunicipalities_geo_simple.json')
            .then(function(r) { return r.json(); })
            .then(function(data) {
                var gyeonggi = data.features
                    .filter(function(f) { return (f.properties.code || '').startsWith('31'); })
                    .map(toMainPolygon);
                gyeonggiDongLayer = L.geoJSON({ type: 'FeatureCollection', features: gyeonggi }, {
                    style: { fillColor: GYEONGGI_COLOR, fillOpacity: 0.7, color: '#fff', weight: 0.8 }
                });
                gyeonggi.forEach(function(f) {
                    var dongName = f.properties.name || '';
                    var lbl = addLabel(f, 'dong-label', function() { return dongName; });
                    gyeonggiDongLabels.push(lbl);
                });
                if (map.getZoom() >= DONG_ZOOM) showDongLevel();
            });
    }

    function showDongLevel() {
        if (seoulGuLayer)   map.removeLayer(seoulGuLayer);
        if (gyeonggiLayer)  map.removeLayer(gyeonggiLayer);
        seoulGuLabels.forEach(function(l)   { map.removeLayer(l); });
        gyeonggiLabels.forEach(function(l)  { map.removeLayer(l); });
        if (seoulDongLayer)    { map.addLayer(seoulDongLayer);    seoulDongLabels.forEach(function(l)    { l.addTo(map); }); }
        if (gyeonggiDongLayer) { map.addLayer(gyeonggiDongLayer); gyeonggiDongLabels.forEach(function(l) { l.addTo(map); }); }
    }

    function showGuLevel() {
        if (seoulDongLayer)    map.removeLayer(seoulDongLayer);
        if (gyeonggiDongLayer) map.removeLayer(gyeonggiDongLayer);
        seoulDongLabels.forEach(function(l)    { map.removeLayer(l); });
        gyeonggiDongLabels.forEach(function(l) { map.removeLayer(l); });
        if (seoulGuLayer)   map.addLayer(seoulGuLayer);
        if (gyeonggiLayer)  map.addLayer(gyeonggiLayer);
        seoulGuLabels.forEach(function(l)   { l.addTo(map); });
        gyeonggiLabels.forEach(function(l)  { l.addTo(map); });
    }

    map.on('zoomend', function() {
        if (map.getZoom() >= DONG_ZOOM) {
            loadSeoulDong();
            loadGyeonggiDong();
            showDongLevel();
        } else {
            showGuLevel();
        }
    });

    var checkIcon = L.divIcon({
        className: '',
        html: '<div style="color:#e03030; font-size:26px; line-height:1; filter: drop-shadow(1px 1px 2px rgba(0,0,0,0.4));">✔</div>',
        iconSize: [26, 26], iconAnchor: [13, 26]
    });

    fetch('<%=request.getContextPath()%>/api/spots')
        .then(function(r) { return r.json(); })
        .then(function(spots) {
            spots.forEach(function(spot) {
                var latlng = L.latLng(spot.latitude, spot.longitude);
                var marker = L.marker(latlng, { icon: checkIcon }).addTo(map);
                var popup = L.popup({ autoClose: false, closeOnClick: false })
                    .setContent(
                        '<strong style="font-size:14px;">' + spot.name + '</strong><br>' +
                        '<span style="color:#888; font-size:12px;">현재 인원</span> ' +
                        '<span style="color:#e03030; font-weight:bold; font-size:14px;">' + spot.activeUserCount + '명</span>'
                    );
                marker.on('click', function() {
                    marker.bindPopup(popup).openPopup();
                    setTimeout(function() { marker.closePopup(); }, 3000);
                });
            });
        });
</script>
</body>
</html>
