<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.Product" %>
<%@ page import="dao.ProductRepository" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="jakarta.servlet.http.Cookie" %>

    <%
    ArrayList<Product> cartList = (ArrayList<Product>) session.getAttribute("cartlist");
    if (cartList != null) {
        for (Product p : cartList) {
            System.out.println("🛒 판매 처리 상품 ID: " + p.getProductId());
            ProductRepository.productIsSold(p.getProductId());
        }
    }

    session.removeAttribute("cartlist");

    Cookie[] cookies = request.getCookies();
    String shipping_cartId = "";
    String shipping_shippingDate = "";

    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            Cookie thisCookie = cookies[i];
            String n = thisCookie.getName();

            if (n.equals("Shipping_cartId"))
                shipping_cartId = URLDecoder.decode(thisCookie.getValue(), "utf-8");
            if (n.equals("Shipping_shippingDate"))
                shipping_shippingDate = URLDecoder.decode(thisCookie.getValue(), "utf-8");

            if (n.startsWith("Shipping_")) {
                thisCookie.setMaxAge(0);
                response.addCookie(thisCookie);
            }
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css" />
    <meta charset="UTF-8">
    <title>주문 완료</title>
</head>
<body>
<div class="container py-4">
    <%@ include file = "../common/menu.jsp" %>

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">신청 완료</h1>
            <p class="col-md-8 fs-4">Application Completed</p>
        </div>
    </div>


    <div class="container">
        <p> <a href="./spots.jsp" class="btn btn-secondary">&laquo; 장소 목록</a>
    </div>

    <%@include file="../common/footer.jsp" %>
</div>
</body>
</html>

