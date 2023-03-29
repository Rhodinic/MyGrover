package htl.steyr.mygrover.interceptor;

import htl.steyr.mygrover.TokenHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    TokenHandler tokenHandler;

    public AuthorizationInterceptor(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if(request.getMethod().equalsIgnoreCase("OPTIONS")) return true;

        if (!tokenHandler.isTokenValid(request.getHeader("authorization").substring(7))) {
            response.sendError(401, "Token not valid.");
            return false;
        }

        return true;
    }
}
