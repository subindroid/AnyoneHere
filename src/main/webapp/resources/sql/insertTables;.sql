SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS cart, wishlist, chat_log, chat, review, product, store, users;

SET FOREIGN_KEY_CHECKS = 1;

-- 유저
CREATE TABLE users (
    u_id VARCHAR(10) NOT NULL,	-- 로그인 ID
    u_password VARCHAR(10) NOT NULL,	-- 비밀번호
    u_name VARCHAR(10) NOT NULL,	-- 사용자 이름
    u_gender VARCHAR(4),	 -- 성별
    u_birth VARCHAR(20),	-- 생년월일
    u_email VARCHAR(40) UNIQUE,	-- 이메일
    u_phone VARCHAR(20),	-- 연락처
    u_address VARCHAR(100),	-- 주소
    u_joinDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 가입일자
    u_latitude DOUBLE,	-- 위도
    u_longitude DOUBLE,	-- 경도

    PRIMARY KEY(u_id)
) DEFAULT CHARSET=utf8;

-- 상점
CREATE TABLE store (
    s_id INT AUTO_INCREMENT NOT NULL,	-- 상점 고유 아이디(기본키)
    s_userId VARCHAR(10),	-- 상점을 개설한 유저 아이디
    s_name VARCHAR(100) NOT NULL,	-- 상점명
    s_description TEXT,	-- 상점설명
    s_location VARCHAR(100), -- 상점 주소
    s_contactTo VARCHAR(20),	-- 상점연락처
    s_profileImage VARCHAR(100),	-- 상점사진
    s_createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 상점 개시일
    s_latitude DOUBLE,                   -- 상점 위도
    s_longitude DOUBLE,                  -- 상점 경도

    PRIMARY KEY(s_id),
    FOREIGN KEY (s_userId) REFERENCES users(u_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8;

-- 상품
CREATE TABLE product (
    p_id INT AUTO_INCREMENT NOT NULL,	-- 상품 고유 아이디
    seller_id VARCHAR(10),	-- 판매자 아이디
    s_id INT,	-- 상품 판매하는 상점 아이디
    p_name VARCHAR(100) NOT NULL,	-- 상품명
    p_unitPrice INT NOT NULL,	-- 상품 가격
    p_description TEXT,	-- 상품 설명
    p_brand VARCHAR(20),	-- 상품 브랜드
    p_category VARCHAR(50),	-- 상품 카테고리
    p_condition VARCHAR(20),	-- 상품 상태
    p_filename VARCHAR(100),	-- 상품 사진
    is_sold BOOLEAN DEFAULT FALSE,	-- 팔렸는지 여부

    PRIMARY KEY(p_id),
    FOREIGN KEY (seller_id) REFERENCES users(u_id) ON DELETE CASCADE,
    FOREIGN KEY (s_id) REFERENCES store(s_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8;

-- 찜 리스트
CREATE TABLE wishlist (
    u_id VARCHAR(10) NOT NULL,	-- 유저 아이디
    s_id INT NOT NULL,	-- 상품 아이디
    p_id INT NOT NULL,	-- 담은 상품의 아이디

    PRIMARY KEY(u_id, p_id),
    FOREIGN KEY (u_id) REFERENCES users(u_id) ON DELETE CASCADE,
    FOREIGN KEY (p_id) REFERENCES product(p_id) ON DELETE CASCADE,
    FOREIGN KEY (s_id) REFERENCES store(s_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8;

-- 채팅 기록 데베에 저장
CREATE TABLE chat_log (
    c_id INT AUTO_INCREMENT,	-- 채팅 로그 구분용 기본키
    sender_id VARCHAR(10) NOT NULL,	-- 송신자
    receiver_id VARCHAR(10) NOT NULL,	-- 수신자
    message TEXT,	-- 채팅내용

    PRIMARY KEY (c_id)
) DEFAULT CHARSET=utf8;

-- 채팅 유저 정보
CREATE TABLE chat (
    c_id INT AUTO_INCREMENT NOT NULL,	-- 채팅 구분용 기본키
    sender_id VARCHAR(10) NOT NULL,	-- 송신자 유저 아이디
    receiver_id VARCHAR(10) NOT NULL,	-- 수신자 유저 아이디

    PRIMARY KEY (c_id)
) DEFAULT CHARSET=utf8;

-- 리뷰
CREATE TABLE review (
    r_id INT AUTO_INCREMENT NOT NULL,	-- 리뷰 구분용 기본키
    r_userId VARCHAR(10) NOT NULL,	-- 리뷰 작성한 유저 아이디
    s_id INT NOT NULL,	-- 리뷰를 받을 상점 아이디
    r_review TEXT,	-- 리뷰 내용
    r_wroteAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,	-- 작성시간
    r_rating DOUBLE,	-- 별점

    PRIMARY KEY(r_id),
    FOREIGN KEY (r_userId) REFERENCES users(u_id) ON DELETE CASCADE,
    FOREIGN KEY (s_id) REFERENCES store(s_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8;

-- 장바구니
CREATE TABLE cart (
 	u_id VARCHAR(10),	-- 유저 장바구니
	p_id INT,	-- 담은 상품
 	PRIMARY KEY(u_id, p_id),  -- 중복 방지
	FOREIGN KEY (u_id) REFERENCES users(u_id) ON DELETE CASCADE,
	FOREIGN KEY (p_Id) REFERENCES product(p_id) ON DELETE CASCADE

  ) DEFAULT CHARSET=utf8;

