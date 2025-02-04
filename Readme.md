# Points learned

### Filters

How to add a custom filter in the filter chain is one of the very important aspects of Spring boot, as well as any web
frameworks.

* adding Filter before another filter.
* adding Filter after another filter.
* Replace a filter by adding it at the location of another filter.
* When adding a filter at a specific position, Spring Security does not assume it is the only filter at that position.
  You might add more filters at the same location in the chain. In this case, Spring Security doesn’t
  guarantee the order in which these will act.
* Some developers think that when you apply a filter at a position of a known one, it will be replaced. This is not the
  case! We must make sure we do
  not add filters that we don’t need.
* If you don’t need a UserDetailsService at all because the concept of the user doesn’t exist. We
  only validate that the user requesting to call an endpoint on the server knows a given
  value. Application scenarios are not usually this simple, and they often require a User-
  DetailsService. However, if you anticipate or have a case where this component is
  not needed, you can disable autoconfiguration. To disable the configuration of the
  default UserDetailsService, you can use the exclude attribute of the @Spring-
  BootApplication annotation on the main class: @SpringBootApplication(exclude =
  {UserDetailsServiceAutoConfiguration.class }).
* Spring Boot can sometimes executes the filter more than once per request, to gurantee that the filter
  executes only once, we cn extends OncePerRequestFilter e.g:

```java 
public class AuthenticationLoggingFilter
        extends OncePerRequestFilter {
    private final Logger logger = Logger.getLogger(AuthenticationLoggingFilter.class.getName());

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader("Request-Id");
        logger.info("Successfully authenticated request with id " + requestId);
        filterChain.doFilter(request, response);
    }
}
```

```java 
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsManager extends Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        // casting to HttpServletRequest, and HttpServletResponse
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        if (
            // any logic
                true
        ) {
            filterChain.doFilter(request, response);  // moving to the next Filter in the filter chain
        } else {
            // interrupting the request and send a response back to the user, by setting an appropriate Status code
            httpResponse.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
```

### after that we need to add it to the Filter chain

```java 
import jakarta.servlet.Filter;

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class);
}
```

#### Following figure showing how to implement a filter that replaces another filter

![JSBCUserDetailManager.png](src/main/resources/JSBCUserDetailManager.png)