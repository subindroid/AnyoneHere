function validateApplicationForm() {
    const form = document.forms["newApplicationSpot"];
    const spotName = form["spotName"].value.trim();
    const content = form["content"].value.trim();
    const latitude = form["latitude"].value;
    const longitude = form["longitude"].value;

    if (spotName === "") {
        alert("장소명을 입력해주세요.");
        return false;
    }

    if (latitude < 0 || longitude < 0) {
        alert("장소 입력이 잘못되었습니다");
        return false;
    }

    if (isNaN(latitude) || isNaN(longitude)) {
        alert("장소 입력이 잘못되었습니다");
        return false;
    }

    // 유효성 검사 통과
    return true;
}
