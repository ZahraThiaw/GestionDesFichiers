package sn.zahra.thiaw.gestiondesfichiers.Web.Filters;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ResponseFilter> responseFilter() {
        FilterRegistrationBean<ResponseFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseFilter());
        registrationBean.addUrlPatterns("/api-docs/*", "/swagger-ui/*", "/swagger-ui.html");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}