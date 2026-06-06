package titulares.entrenamiento.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import titulares.entrenamiento.filter.JwtFilter;
import titulares.entrenamiento.security.JwtUtil;
/**
 * configuración de registrar y configurar los filtros utilizados por la aplicación
 * Esta configuración permite asociar el filtro JWT a las rutas
 * protegidas de la API
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtUtil jwtUtil) {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
