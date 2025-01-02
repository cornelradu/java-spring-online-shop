package elefant.clone.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CookieUtil {
    private static final String RECENTLY_VIEWED_COOKIE_NAME = "recently_viewed_products";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 days
    private static final int MAX_RECENT_PRODUCTS = 5;

    public Cookie createRecentlyViewedCookie(List<Integer> productIds) {
        String encodedValue = Base64.getEncoder().encodeToString(
                productIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining("-"))
                        .getBytes());

        Cookie cookie = new Cookie(RECENTLY_VIEWED_COOKIE_NAME, encodedValue    );
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public List<Integer> getRecentlyViewedProducts(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (RECENTLY_VIEWED_COOKIE_NAME.equals(cookie.getName())) {
                    // Decode the Base64 string
                    String decodedValue = new String(Base64.getDecoder().decode(cookie.getValue()));
                    return Arrays.stream(decodedValue.split("-"))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }

    public void updateRecentlyViewedProducts(HttpServletResponse response,
                                             List<Integer> existingProducts,
                                             Integer newProductId) {
        List<Integer> updatedProducts = new ArrayList<>();
        updatedProducts.add(newProductId);

        // Add existing products, excluding the new one if it's already there
        existingProducts.stream()
                .filter(id -> !id.equals(newProductId))
                .forEach(updatedProducts::add);

        // Keep only the most recent MAX_RECENT_PRODUCTS
        if (updatedProducts.size() > MAX_RECENT_PRODUCTS) {
            updatedProducts = updatedProducts.subList(0, MAX_RECENT_PRODUCTS);
        }

        response.addCookie(createRecentlyViewedCookie(updatedProducts));
    }
}