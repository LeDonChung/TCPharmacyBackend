package vn.edu.iuh.fit.pharmacy.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import vn.edu.iuh.fit.pharmacy.utils.HelperUtils;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        BaseResponse<Object> responseDTO = BaseResponse
//                .builder()
//                .success(false)
//                .data(SystemConstraints.ACCESS_DENIED)
//                .code(String.valueOf(HttpStatus.FORBIDDEN.value()))
//                .build();

        ErrorDetail errorDetail = new ErrorDetail(SystemConstraints.ACCESS_DENIED, SystemConstraints.ACCESS_DENIED, LocalDateTime.now());

        String json = HelperUtils.JSON_WRITER.writeValueAsString(errorDetail);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
