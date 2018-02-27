package sysu.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import sysu.web.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
    private MyAuthenticationProvider provider;//自定义验证
    @Autowired
    private UserDetailsService userDetailsService;//自定义用户服务
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
    	auth.userDetailsService(userDetailsService);
    }
	@Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //允许所有用户访问"/"和"/home"
        http.authorizeRequests()
                .antMatchers("/", "/upload","/home","/signin","/login","/coreclass/coreclass","/commentstatistics/commentstatistic","/changestatistics/changestatistic").permitAll()
                //其他地址的访问均需验证权限
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //指定登录页是"/login"
                .loginPage("/login")
                .defaultSuccessUrl("/")//登录成功后默认跳转到主页
                .successForwardUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")//退出登录后的默认为login
                .permitAll();
        http.csrf().disable();

    }

    /**
     * 设置用户密码的加密方式为MD5加密
     * @return
     */
    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();

    }

    /**
     * 自定义UserDetailsService，从数据库中读取用户信息
     * @return
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService(){
        return new CustomUserDetailsService();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //将验证过程交给自定义验证工具
        auth.authenticationProvider(provider);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
 
        web.ignoring().antMatchers("/assets/**","/images/**","/bootstrap/**","/bootstrap/css/**","/bootstrap/js/**","/vendors/**","/fonts/**","/chart/**","/goJS/**","/foamtree/**","/chart/**");
    }


}
