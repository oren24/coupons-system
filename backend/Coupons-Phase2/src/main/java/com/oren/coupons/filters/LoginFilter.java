package com.oren.coupons.filters;

import com.oren.coupons.utils.JWTUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFilter implements Filter {
	/**
	 * checks if the request is white listed
	 *
	 * @param methodType - the type of the method
	 * @param url        - the url of the request
	 * @return true if the request is white listed, false if not
	 */
	private boolean isRequestWhiteListed(String methodType, String url) {
		if (methodType.equals("POST") && url.endsWith("users/login")) {
			return true;
		}
		if (methodType.equals("POST") && url.endsWith("users")) {
			return true;
		}
		if (methodType.equals("PUT") && url.endsWith("users")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("users")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("companies")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("coupons")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("coupons/byCompany")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("coupons/byCategory")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("coupons/byMaxPrice")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("categories")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("category/byName")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("purchases")) {
			return true;
		}
		if (methodType.equals("GET") && url.endsWith("purchases/byCategory")) {
			return true;
		}
		return methodType.equals("GET") && url.endsWith("purchases/byDateRange");
	}

	/**
	 * the filter that checks if the user is logged in
	 *
	 * @param servletRequest  - the request from the client
	 * @param servletResponse - the response from the server
	 * @param filterChain     - the chain of filters
	 * @throws IOException      - if there is an error with the input/output
	 * @throws ServletException - if there is a servlet error
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest myRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse myResponse = (HttpServletResponse) servletResponse;
		String methodType = myRequest.getMethod();

		if (methodType.equals("OPTIONS")) {
			filterChain.doFilter(myRequest, myResponse);
			return;
		}
		String url = myRequest.getRequestURL().toString();

		if (isRequestWhiteListed(methodType, url)) {
			filterChain.doFilter(myRequest, myResponse);
			return;
		}
		String token = myRequest.getHeader("Authorization");
		try {
			JWTUtils.validateToken(token);
			filterChain.doFilter(myRequest, myResponse);
		} catch (Exception e) {
			myResponse.setStatus(401);
		}
	}

	/**
	 * @param filterConfig - the filter configuration
	 * @throws ServletException - if there is a servlet error
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	/**
	 * destroy the filter
	 *
	 * @throws ServletException - if there is a servlet error
	 */
	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
