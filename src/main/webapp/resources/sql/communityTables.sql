-- 커뮤니티 관련 테이블 (기존 insertTables.sql에 추가하거나 별도 실행)

-- 게시글
CREATE TABLE posts
(
    post_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id    VARCHAR(30)  NOT NULL,
    category   VARCHAR(20)  NOT NULL,              -- FREE, REVIEW, QUESTION, RECOMMEND
    title      VARCHAR(100) NOT NULL,
    content    TEXT         NOT NULL,
    view_count INT       DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN   DEFAULT FALSE,

    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CHECK (category IN ('FREE', 'REVIEW', 'QUESTION', 'RECOMMEND'))
) DEFAULT CHARSET = utf8mb4;

-- 게시글 첨부 이미지
CREATE TABLE post_images
(
    image_id   INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT          NOT NULL,
    image_path VARCHAR(100) NOT NULL,
    sort_order INT DEFAULT 0,

    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE
) DEFAULT CHARSET = utf8mb4;

-- 댓글
CREATE TABLE post_comments
(
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT         NOT NULL,
    user_id    VARCHAR(30) NOT NULL,
    content    TEXT        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN   DEFAULT FALSE,

    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) DEFAULT CHARSET = utf8mb4;

-- 좋아요
CREATE TABLE post_likes
(
    like_id    INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT         NOT NULL,
    user_id    VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_post_like (post_id, user_id)
) DEFAULT CHARSET = utf8mb4;

-- 게시글 신고
CREATE TABLE post_reports
(
    report_id     INT AUTO_INCREMENT PRIMARY KEY,
    post_id       INT          NOT NULL,
    reporter_id   VARCHAR(30)  NOT NULL,
    reason        VARCHAR(300) NOT NULL,
    report_status VARCHAR(20) DEFAULT 'PENDING',
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    FOREIGN KEY (reporter_id) REFERENCES users (user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_post_reporter (post_id, reporter_id),
    CHECK (report_status IN ('PENDING', 'REVIEWED', 'DISMISSED'))
) DEFAULT CHARSET = utf8mb4;
