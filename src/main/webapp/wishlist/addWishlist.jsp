<!-- 이 파일 안 쓸수도 있을 듯?-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.Spot" %>
<%@ page import="dao.SpotRepository" %>

<%
request.setCharacterEncoding("UTF-8");

String id = request.getParameter("id");

if (id == null || id.trim().equals("")) {
   response.sendRedirect("spots.jsp");
   return;
}

int spotId = Integer.parseInt(id);

// 상품 정보 가져오기
SpotRepository dao = SpotRepository.getInstance();
Spot Spot = dao.getSpotBtSpotId(spotId);
if (product == null) {
   response.sendRedirect("exceptionNoProduct.jsp");
   return;
}

// 세션에서 장바구니 꺼내오기
ArrayList<Product> cartList = (ArrayList<Product>) session.getAttribute("cartlist");
if (cartList == null) {
    cartList = new ArrayList<Product>();
}

// 이미 장바구니에 있는지 확인
boolean found = false;
for (Product p : cartList) {
    if (p.getProductId() == product.getProductId()) {
        found = true;
        break;
    }
}

if (found) {
%>
    <script>
        alert("이미 장바구니에 담긴 상품입니다!");
        history.back();
    </script>
<%
} else {
    product.setQuantity(1);
    cartList.add(product);
    session.setAttribute("cartlist", cartList);
    response.sendRedirect("cart.jsp");
}
%>
