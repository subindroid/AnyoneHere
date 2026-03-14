function CheckAddSpot() {
    const form = document.forms["newApplicationSpot"];
    const spotName    = form["spotName"].value.trim();
    const location    = form["spot_location"].value.trim();
    const description = form["spot_description"].value.trim();
    const category    = form.querySelector('input[name="category"]:checked');

    if (!spotName) {
        alert("장소명을 입력해주세요.");
        return false;
    }

    if (spotName.length > 50) {
        alert("장소명은 50자 이하로 입력해주세요.");
        return false;
    }

    // spot_location은 "위도,경도" 형태
    if (!location) {
        alert("장소 위치를 입력해주세요.");
        return false;
    }
    const parts = location.split(",");
    if (parts.length !== 2) {
        alert("장소 위치 형식이 올바르지 않습니다. (위도,경도)");
        return false;
    }
    const lat = parseFloat(parts[0].trim());
    const lng = parseFloat(parts[1].trim());
    if (isNaN(lat) || isNaN(lng)) {
        alert("장소 위치에 숫자가 아닌 값이 포함되어 있습니다.");
        return false;
    }
    if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
        alert("위도(-90~90) 또는 경도(-180~180) 범위를 벗어났습니다.");
        return false;
    }

    if (!description) {
        alert("장소 설명을 입력해주세요.");
        return false;
    }
    if (description.length < 10) {
        alert("장소 설명은 10자 이상 입력해주세요.");
        return false;
    }
    if (description.length > 500) {
        alert("장소 설명은 500자 이하로 입력해주세요.");
        return false;
    }

    if (!category) {
        alert("카테고리를 선택해주세요.");
        return false;
    }

    return true;
}
