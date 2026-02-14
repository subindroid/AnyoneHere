INSERT INTO users (
    u_id, u_password, u_name, u_gender, u_birth,
    u_email, u_phone, u_address, u_joinDate,
    u_latitude, u_longitude
) VALUES (
    'user1', '1234', '홍길동', '남성', '2000-12-23',
    'hondgildong@naver.com', '010-1234-1111',
    '서울시 서초구 반포1동 주흥 13길 38', CURDATE(),
    37.5078999589812, 127.017440030885
);

INSERT INTO store (
    s_userId, s_name, s_description, s_contactTo, s_profileImage, s_createdAt, s_latitude, s_longitude
) VALUES (
    'user1', '베스트클로징', '깔끔한 상태의 상품 아니면 안 파는 사람입니다. ', '010-1234-1111', 'storeImageSample.png', CURDATE(), 37.5078999589812, 127.017440030885
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '네이비 반팔 카라티', 15000,
    '네이비 반팔 카라티입니다.',
    '좋은옷', '상의', 'NEW',
    'navyHalfTShirt.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '고양이 티셔츠', 15000,
    '귀여운 고양이가 그려져있는 티셔츠입니다.',
    '고양이사랑', '상의', 'NEW',
    'catHalfTShirt.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '청바지', 17000,
    '심플한 긴 청바지입니다.',
    '착한옷', '하의', 'NEW',
    'longJean.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '핑크가디건', 20000,
    '코랄색 베이직한 가디건입니다.',
    '착한옷', '아우터', 'NEW',
    'pinkBasicCardigan.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '핑크 꽈배기 니트 가디건', 20000,
    '코랄색 꽈배기 패턴의 귀여운 가디건입니다.',
    '착한옷', '아우터', 'NEW',
    'pinkKnitCardigan.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '블랙 롱코트', 48000,
    '심플한 블랙 롱코트입니다.',
    '착한옷', '아우터', 'NEW',
    'blackLongCoat.png'
);

INSERT INTO product (
    seller_id, s_id, p_name, p_unitPrice, p_description,
    p_brand, p_category, p_condition, p_filename
) VALUES (
    'user1', 1, '고양이 귀 모자', 11000,
    '고양이 귀가 포인트인 검정색 볼캡입니다. ',
    '귀여운모자', '패션소품', 'NEW',
    'catBallcap.png'
);

