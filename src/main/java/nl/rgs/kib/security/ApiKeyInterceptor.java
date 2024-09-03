package nl.rgs.kib.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.service.ApiAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiAccountService apiAccountService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String apiKey = request.getHeader("api-key");

        if (apiKey == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing API Key");
            return false;
        }

        ApiAccount apiAccount = apiAccountService.findByApiKey(apiKey).orElse(null);

        if (apiAccount == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return false;
        }

        if (!apiAccount.getActive()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inactive API Key");
            return false;
        }

        Date currentDate = new Date();

        if (apiAccount.getStartDate().after(currentDate)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inactive API Key");
            return false;
        }

        if (apiAccount.getEndDate() != null && apiAccount.getEndDate().before(currentDate)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired API Key");
            return false;
        }

        return true;
    }
}