package com.oren.coupons.filters;

import com.oren.coupons.consts.Consts;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is a filter that allows CORS requests with security controls.
 * CORS is a security measure that prevents malicious websites from sending requests to your server.
 * SECURITY FIX: Now uses environment-configurable allowed origins instead of hardcoded localhost
 */
@Component
public class CORSFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// SECURITY FIX: Get allowed origins from environment configuration
		String allowedOrigins = Consts.CORS_ALLOWED_ORIGINS;
		
		// Get the origin from the request
		String origin = request.getHeader("Origin");
		
		// Validate that origin matches allowed origins (simple check - can be enhanced with patterns)
		if (origin != null && (allowedOrigins.contains(origin) || allowedOrigins.equals("*"))) {
			response.addHeader("Access-Control-Allow-Origin", origin);
		}

		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");
		response.addHeader("Access-Control-Allow-Headers",
				"Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		
		// SECURITY FIX: Add security headers
		response.addHeader("X-Content-Type-Options", "nosniff");
		response.addHeader("X-Frame-Options", "DENY");
		response.addHeader("X-XSS-Protection", "1; mode=block");

		// For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		// pass the request along the filter chain
		chain.doFilter(request, servletResponse);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}