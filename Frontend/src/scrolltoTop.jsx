import { useEffect } from "react";
import { useLocation } from "react-router-dom";

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0); // 페이지 상단으로 스크롤 이동
  }, [pathname]); // pathname이 바뀔 때마다 실행

  return null; // 화면에 렌더링되는 UI는 없음
};

export default ScrollToTop;
