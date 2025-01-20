import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: './', // 상대 경로 설정
  build: {
    outDir: 'dist', // 빌드 결과물이 저장될 디렉토리
    assetsDir: 'assets', // 정적 파일이 저장될 디렉토리
  },
})
