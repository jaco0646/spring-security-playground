package org.playground.security;

import jakarta.servlet.Filter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String... args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(List<SecurityFilterChain> chains) {
        return args -> {
            System.out.println("SecurityFilterChain count == " + chains.size());
            List<Filter> filters = chains.get(0).getFilters();
            System.out.println("Servlet Filter count == " + filters.size());
            for (int i = 0; i < filters.size(); i++) {
                System.out.println("  " + (i + 1) + ")\t" + filters.get(i).getClass());
            }
        };
    }

/* Default Filters (org.springframework.security.web.*)
  1)	session.DisableEncodeUrlFilter
  2)	context.request.async.WebAsyncManagerIntegrationFilter
  3)	context.SecurityContextHolderFilter
  4)	header.HeaderWriterFilter
  5)	csrf.CsrfFilter
  6)	authentication.logout.LogoutFilter
  7)	authentication.UsernamePasswordAuthenticationFilter     <| These three Filters are removed if
  8)	authentication.ui.DefaultLoginPageGeneratingFilter      <|  a custom SecurityFilterChain
  9)	authentication.ui.DefaultLogoutPageGeneratingFilter     <|   bean is added.
  10)	authentication.www.BasicAuthenticationFilter
  11)	savedrequest.RequestCacheAwareFilter
  12)	servletapi.SecurityContextHolderAwareRequestFilter
  13)	authentication.AnonymousAuthenticationFilter
  14)	access.ExceptionTranslationFilter
  15)	access.intercept.AuthorizationFilter
 */

}