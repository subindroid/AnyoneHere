function validateSignInForm() {
    const form = document.forms["newMember"];

    const id = form["id"].value.trim();
    const password = form["password"].value.trim();
    const password_confirm = form["password_confirm"].value.trim();
    const name = form["name"].value.trim();
    const birthyy = form["birthyy"].value.trim();
    const birthmm = form["birthmm"].value;
    const birthdd = form["birthdd"].value.trim();
    const mail1 = form["mail1"].value.trim();
    const mailSelect = form["mail2_select"].value;
    const mail2 = form["mail2"].value.trim();
    const phone = form["phone"].value.trim();
    const address = form["address"].value.trim();

    const gender = form.querySelector('input[name="gender"]:checked');

    const idRegex = /^[a-zA-Z0-9]{8,17}$/;
    const pwRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}$/;
    const phoneRegex = /^01[0-9]-?\d{3,4}-?\d{4}$/;

    if (!idRegex.test(id)) {
        alert("아이디는 영문자와 숫자만 사용하여 8~17자로 입력하세요.");
        return false;
    }

    if (!pwRegex.test(password)) {
        alert("비밀번호는 8~20자이며, 영문/숫자/특수문자를 모두 포함해야 합니다.");
        return false;
    }

    if (password !== password_confirm) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    if (!name) {
        alert("이름을 입력하세요.");
        return false;
    }

    if (!gender) {
        alert("성별을 선택하세요.");
        return false;
    }

    if (!/^\d{4}$/.test(birthyy) || !birthmm || !/^\d{2}$/.test(birthdd)) {
        alert("생년월일을 올바르게 입력하세요.");
        return false;
    }

    if (!mail1) {
        alert("이메일을 입력하세요.");
        return false;
    }

    if (mailSelect === "custom" && !mail2) {
        alert("이메일 도메인을 입력하세요.");
        return false;
    }

    if (!phoneRegex.test(phone)) {
        alert("전화번호 형식이 올바르지 않습니다.");
        return false;
    }

    if (!address) {
        alert("주소를 입력하세요.");
        return false;
    }

    return true;
}
