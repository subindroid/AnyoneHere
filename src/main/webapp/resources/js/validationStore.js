function CheckAddStore() {
    const form = document.forms["newStore"];
    if (!form) {
        alert("폼을 찾을 수 없습니다.");
        return false;
    }

	const storeNameInput = form.elements["storeName"];
	const contactInput = form.elements["contactTo"];
	const descriptionInput = form.elements["store_description"];

    // 요소 존재 여부 확인 (null-safe)
    if (!storeNameInput || !contactInput || !descriptionInput) {
        alert("필수 입력 요소를 찾을 수 없습니다.");
        return false;
    }

	const storeName = storeNameInput.value.trim();
	const contact = contactInput.value.trim();
	const store_description = descriptionInput.value.trim();

    if (storeName === "") {
        alert("상점명을 입력해주세요.");
        return false;
    }

    if (contact === "") {
        alert("연락처를 입력해주세요.");
        return false;
    }

    if (store_description === "") {
        alert("상점 설명을 입력해주세요.");
        return false;
    }

    if (store_description.length > 1000) {
        alert("상점 설명은 1000자 이하로 작성해주세요.");
        return false;
    }

    return true; // ✅ 폼 정상 제출
}
