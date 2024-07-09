package scrips.datamigration.jpa.confg;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
 entityManagerFactoryRef = "customerEntityManagerFactory",
 transactionManagerRef = "customerTransactionManager",
 basePackages = {
 "scrips.datamigration.jpa"// "scrips.datamigration.jpa.sss.transaction","scrips.datamigration.jpa.sss.securities","scrips.datamigration.business","scrips.datamigration.jpa.accountposition","scrips.datamigration.jpa.fileupload","scrips.datamigration.jpa.member","scrips.datamigration.jpa.account","scrips.datamigration.jpa.cbm","scrips.datamigration.jpa.transaction","scrips.datamigration.jpa.sss.account","scrips.datamigration.jpa.sss.securities","scrips.datamigration.jpa.sss.transaction","scrips.datamigration.jpa.sss.coupon","scrips.datamigration.jpa.sss.Member"
 }
)
public class MySqlConfig {

 @Primary
 @Bean(name = "customerDataSource")
 @ConfigurationProperties(prefix = "spring.datasource")
 public DataSource customerDataSource() {
  return DataSourceBuilder.create().build();
 }

 @Primary
 @Bean(name = "customerEntityManagerFactory")
 public LocalContainerEntityManagerFactoryBean
 entityManagerFactory(
  EntityManagerFactoryBuilder builder,
  @Qualifier("customerDataSource") DataSource dataSource
 ) {
	 Map<String,Object> properties=new HashMap<>();
   properties.put("hibernate.hbm2ddl.auto","update");
   properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
  return builder
   .dataSource(dataSource)//,"scrips.datamigration.jpa.sss.securities"
   .packages("scrips.datamigration.jpa.transaction","scrips.datamigration.jpa.sss.transaction","scrips.datamigration.business","scrips.datamigration.jpa.accountposition","scrips.datamigration.jpa.fileupload","scrips.datamigration.jpa.member","scrips.datamigration.jpa.account","scrips.datamigration.jpa.cbm","scrips.datamigration.jpa.transaction","scrips.datamigration.jpa.sss.account","scrips.datamigration.jpa.sss.securities","scrips.datamigration.jpa.sss.transaction","scrips.datamigration.jpa.sss.coupon","scrips.datamigration.jpa.sss.Member","scrips.datamigration.jpa.user")
   .persistenceUnit("data")
   .build();
 }

 @Primary
 @Bean(name = "customerTransactionManager")
 public PlatformTransactionManager customerTransactionManager(
  @Qualifier("customerEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory
 ) {
  return new JpaTransactionManager(customerEntityManagerFactory);
 }
}
