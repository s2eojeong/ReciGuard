INSERT INTO recipe (recipe_id, user_id, image_path, recipe_name, Serving, cuisine, food_type, cooking_style) VALUES
	(1, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00028_2.png', '새우 두부 계란찜', 1, '양식', '반찬', '찌개'),
	(2, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00029_2.png', '부추 콩가루 찜', 1, '중식', '반찬', '찌개'),
	(3, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00031_2.png', '방울토마토 소박이', 1, '한식', '반찬', '기타'),
	(4, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00032_2.png', '순두부 사과 소스 오이무침', 1, '일식', '반찬', '기타'),
	(5, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00033_2.png', '사과 새우 북엇국', 1, '한식', '국', '끓이기'),
	(6, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00036_2.png', '저염 된장으로 맛을 낸 황태해장국', 1, '한식', '국', '끓이기'),
	(7, NULL, 'http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00037_2.png', '된장국', 1, '일식', '국', '끓이기');

INSERT INTO instruction (recipe_id, instruction_id, instruction, instruction_im) VALUES
	(1, 1, '손질된 새우를 끓는 물에 데쳐 건진다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00028_1.png'),
	(1, 2, '연두부, 달걀, 생크림, 설탕에 녹인 무염버터를 믹서에 곱게 간다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00028_2.png'),
	(1, 3, '시금치를 잘게 다져 혼합물 그릇에 뿌리고 찜기에 넣어 익힌다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00028_3.png'),
	(2, 1, '부추는 깨끗이 씻어 물기를 제거하고, 5cm 길이로 썬다.', 'http://www.foodsafetykorea.go.kr/uploadimg/20230206/20230206054820_1675673300714.jpg'),
	(2, 2, '찜기에 면보를 깔고 부추를 넣은 후 김이 오르게 찐다.', 'http://www.foodsafetykorea.go.kr/uploadimg/20230206/20230206054834_1675673314720.jpg'),
	(2, 3, '저염간장에 다진 대파, 다진 마늘, 고춧가루, 우리당을 섞어 부추 위에 뿌린다.', 'http://www.foodsafetykorea.go.kr/uploadimg/20230206/20230206054908_1675673348152.jpg'),
	(3, 1, '물을 빼고 2cm 정도의 크기로 썬 방울토마토를 접시에 배열한다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00031_1.png'),
	(3, 2, '깨끗이 씻은 방울토마토는 꼭지를 떼고 윗부분에 칼집을 낸다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00031_4.png'),
	(3, 3, '갈집을 낸 방울토마토에 양념소스를 올린다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00031_5.png'),
	(4, 1, '사과, 순두부를 믹서에 넣고 곱게 간다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00032_1.png'),
	(4, 2, '오이와 사과를 썬 후 순두부와 함께 버무린다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00032_2.png'),
	(5, 1, '북어채를 찬물에 담가 적당히 불린다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00033_1.png'),
	(5, 2, '불린 북어채를 물기를 제거하고 프라이팬에 약한 불로 볶는다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00033_2.png'),
	(5, 3, '적당히 볶아낸 북어채를 그릇에 담고 참기름을 살짝 뿌려 마무리한다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00033_3.png'),
	(6, 1, '황태를 손질하여 물에 헹군다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00036_3.png'),
	(6, 2, '콩나물, 다진 마늘, 청양고추를 추가하여 끓인다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00036_4.png'),
	(7, 1, '감자, 양파를 얇게 썰어 냄비에 넣는다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00037_2.png'),
	(7, 2, '끓는 물에 재료를 넣고 된장과 함께 잘 섞어준다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00037_4.png'),
	(7, 3, '완성된 국에 다진 대파와 두부를 올려 마무리한다.', 'http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00037_6.png');
    
INSERT INTO recipe_ingredient (recipe_ingredient_id, ingredient_id, recipe_id, quantity) VALUES
	(1, 1, 1, '75g 3/4모'),
	(2, 1, 1, '20g 5마리'),
	(3, 1, 1, '30g 1/2개'),
	(4, 1, 1, '13g 1큰술'),
	(5, 1, 1, '5g 1작은술'),
	(6, 1, 1, '5g 1작은술'),
	(7, 1, 1, '10g 3줄기'),
	(8, 2, 2, '50g'),
	(9, 2, 2, '7g 1⅓작은술'),
	(10, 2, 2, '저염간장 3g 2/3작은술'),
	(11, 2, 2, '5g 1작은술'),
	(12, 2, 2, '2g 1/2쪽'),
	(13, 2, 2, '2g 1/3작은술'),
	(14, 2, 2, '2g 1/3작은술'),
	(15, 2, 2, '2g 1/3작은술'),
	(16, 2, 2, '약간'),
	(17, 3, 3, '150g 5개'),
	(18, 3, 3, '10g 3×1cm'),
	(19, 3, 3, '10g 5줄기'),
	(13, 3, 3, '4g 1작은술'),
	(20, 3, 3, '3g 2/3작은술'),
	(12, 3, 3, '2.5g 1/2쪽'),
	(21, 3, 3, '2g 1/3작은술'),
	(5, 3, 3, '2g 1/3작은술'),
	(22, 3, 3, '2ml 1/3작은술'),
	(23, 3, 3, '약간'),
	(24, 4, 4, '70g 1/3개'),
	(25, 4, 4, '10g 1큰술'),
	(26, 4, 4, '50g 1/3개'),
	(27, 4, 4, '40g'),
	(28, 5, 5, '25g 15개'),
	(29, 5, 5, '10g 3마리'),
	(26, 5, 5, '30g 1/5개'),
	(18, 5, 5, '40g 1/4개'),
	(30, 5, 5, '20g 2장'),
	(22, 5, 5, '300ml 1½컵'),
	(31, 6, 6, '15g 10개'),
	(32, 6, 6, '30g 1/6봉지'),
	(33, 6, 6, '30g 5×3×2cm'),
	(34, 6, 6, '10g 2작은술'),
	(22, 6, 6, '300ml 1½컵'),
	(35, 6, 6, '5g 1/2개'),
	(12, 6, 6, '2g 1/3작은술'),
	(36, 7, 7, '20g 2×2×2cm'),
	(37, 7, 7, '20g 4가닥'),
	(38, 7, 7, '10g 4×3×1cm'),
	(18, 7, 7, '10g 2×1cm'),
	(11, 7, 7, '10g 5cm'),
	(34, 7, 7, '5g 1작은술'),
	(22, 7, 7, '300ml 1½컵');

INSERT INTO nutrition (nutrition_id, recipe_id, calories, sodium, carbohydrate, fat, protein) VALUES
	(1, 1, 220, 99, 3, 17, 14),
	(2, 2, 215, 240, 20, 9, 14),
	(3, 3, 45, 277, 9, 1, 2),
	(4, 4, 75, 22, 10, 2, 4),
	(5, 5, 65, 78, 2, 1, 12),
	(6, 6, 100, 361, 12, 0, 13),
	(7, 7, 20, 260, 3, 0, 2);

INSERT INTO recipe_stats (recipe_id, view_count, scrap_count) VALUES
	(1, 142, 65),
    (2, 255, 98),
    (3, 81, 25),
    (4, 173, 56),
    (5, 91, 37),
    (6, 218, 108),
    (7, 354, 132);



    
