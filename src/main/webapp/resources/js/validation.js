console.log("✅ validation.js loaded");

function CheckAddProduct() {
  console.log("✅ CheckAddMarket 실행됨!");

  // 입력 요소 가져오기
  var productName = document.getElementById("productName");
  var productImage = document.getElementById("productImage");
  var unitPrice = document.getElementById("unitPrice");
  var description = document.getElementById("description");  var condition = document.querySelector('input[name="condition"]:checked');

  // 상품명 체크
  if (productName.value.trim().length === 0) {
    alert("[상품명]\n상품명을 입력해주세요");
    productName.focus();
    return false;
  }
  if (productName.value.length > 50) {
    alert("[상품명]\n최대 50자까지 입력하세요");
    productName.focus();
    return false;
  }

  // 이미지 업로드 체크
  if (!productImage.value) {
    alert("[상품 이미지]\n이미지를 첨부해주세요");
    productImage.focus();
    return false;
  }

  // 가격 유효성 검사
  if (unitPrice.value.trim().length === 0 || isNaN(unitPrice.value)) {
    alert("[가격]\n숫자만 입력하세요");
    unitPrice.focus();
    return false;
  }
  if (Number(unitPrice.value) < 0) {
    alert("[가격]\n음수를 입력할 수 없습니다");
    unitPrice.focus();
    return false;
  }

  // 상품 상태 선택 확인 (라디오 버튼)
  if (!condition) {
    alert("[상품 상태]\n상품 상태를 선택해주세요");
    return false;
  }

  // 상세 설명 체크
  if (description.value.trim().length < 10) {
    alert("[상세설명]\n최소 10자 이상 입력하세요");
    description.focus();
    return false;
  }

  // 최종 제출
  document.newProduct.submit();
}
