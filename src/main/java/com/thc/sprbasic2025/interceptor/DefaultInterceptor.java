package com.thc.sprbasic2025.interceptor;

import com.thc.sprbasic2025.exception.NoAuthException;
import com.thc.sprbasic2025.util.TokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@RequiredArgsConstructor
public class DefaultInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String prefix = "Bearer ";

    final TokenFactory tokenFactory;

    //컨트롤러 진입 전에 호출되는 메서드
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle");
        logger.info("accessToken : " + request.getHeader("Authorization"));

        Long reqUserId = null;
        String accessToken = request.getHeader("Authorization");
        if(accessToken != null && accessToken.startsWith(prefix)) {
            accessToken = accessToken.substring(prefix.length());
            reqUserId = tokenFactory.validateAccessToken(accessToken);
            if(reqUserId == null) {
                throw new NoAuthException("Invalid access token");
            }
        }

        // accessToken 을 이용해서, 정상 로그인 했을 경우 그 userId 추출해서 담아 컨트롤러로 보내기!
        request.setAttribute("reqUserId", reqUserId);

        //response.setHeader("temp1122", "11223344");
        /*
        logger.info("getRequestURI!! : " + request.getRequestURI());
        Enumeration<String> set = request.getHeaderNames();
        while (set.hasMoreElements()) {
            String name = set.nextElement();
            logger.info("name !! : " + name + " : " + request.getHeader(name));
        }
        */

        return true;
    }

    //컨트롤러 실행 후에 호출되는 메서드
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("postHandle");
    }

    //모든것을 마친 후 실행되는 메서드
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion");
    }
}
