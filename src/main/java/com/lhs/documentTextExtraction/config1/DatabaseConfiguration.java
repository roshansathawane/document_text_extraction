//package com.lhs.documentTextExtraction.config1;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.Arrays;
//import java.util.Properties;
//import java.util.StringTokenizer;
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.lhs.documentTextExtraction.utility.Utility;
////import com.lhs.encryptdecrypt.utility.Util;
//import com.lhs.javautilities.LhsEncryptionDecryptionUtility;
//import com.lhs.javautilities.LhsStringUtility;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 
// * @author sushma.manusmare
// *
// */
//@Configuration
//@Slf4j
//@EnableTransactionManagement
//public class DatabaseConfiguration {
//
//	@Autowired
//	private DataSourceProperties properties;
//
//	@Autowired
//	private Utility taxcpcUtl;
//
//	@Autowired
//	private LhsStringUtility strUtl;
//
//	@Autowired
//	private LhsEncryptionDecryptionUtility lhsEncDecUtl;
//
//	@Autowired
//	@Qualifier("dataSourceFilePath")
//	private String dataSourcePath;
//	
//	
//	
//
//	
//	@Autowired 
//	private Environment env;
//	
//
//	@Bean(name = "dataSource")
//	public DataSource dataSource() {
//		File[] files = new File(dataSourcePath).listFiles();
//		@SuppressWarnings("rawtypes")
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//		
//		
//		Arrays.stream(files).forEach(file -> {
//			try {
//				if (file.getName().toLowerCase().endsWith("properties")) {
//					String tenantId = taxcpcUtl.getBaseName(file.getName());
//					Properties tenantProperties = new Properties();
//					tenantProperties.load(new FileInputStream(file));
//
//					String db_ip = tenantProperties.getProperty("ServerName");
//					String db_port = tenantProperties.getProperty("PortNumber");
//					String db_sid = tenantProperties.getProperty("Sid");
//					String datasourceUrl = "jdbc:oracle:thin:@" + db_ip + ":" + db_port + "/" + db_sid;
//					
//					String db_username = tenantProperties.getProperty("UserName");
//					String db_password = tenantProperties.getProperty("PassWord");
//					String driverName = properties.getDriverClassName();
//					System.out.println("db_username.."+db_username);
//					System.out.println("db_username.."+db_username);
//					System.out.println("datasourceUrl-->"+datasourceUrl);
//
//					dataSourceBuilder.username(db_username);
//					dataSourceBuilder.password(db_password);
//					dataSourceBuilder.url(datasourceUrl);
//					dataSourceBuilder.driverClassName(driverName);
//					
//					log.info("Tenant {} ready.", tenantId);
//	                
//				}else if (file.getName().toLowerCase().endsWith("properties.enc")) {
//					String tenantId = taxcpcUtl.getBaseName(taxcpcUtl.getBaseName(file.getName()));
//
//					Properties tenantProperties = this.readEncryptedDbProps(file);
//
//					lhsEncDecUtl.decryptFile(tenantId, tenantId, tenantId);
//				
//					String datasourceUrl = tenantProperties.getProperty("db_url");
//					String db_username = tenantProperties.getProperty("db_username");
//					String db_password = tenantProperties.getProperty("db_password");
//					System.out.println("db_username.."+db_username);
//					System.out.println("db_username.."+db_username);
//					String driverName = properties.getDriverClassName();
//					
//					dataSourceBuilder.username(db_username);
//					dataSourceBuilder.password(db_password);
//					dataSourceBuilder.url(datasourceUrl);
//					dataSourceBuilder.driverClassName(driverName);
//					
//					
//					
//					log.info("Tenant {} ready.", tenantId);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		return dataSourceBuilder.build();
//	}// end method
//	
//	
//	private Properties readEncryptedDbProps(File decryptedFile) {
//
//		Properties props = new Properties();
//		if (decryptedFile != null) {
//			if (decryptedFile.exists()) {
//				try {
//					final String secretKey = "fgs@taxcpc#hsag##87@lhs";
//					final String salt = "dcbqpodcbyxwbazuts";
//
//					byte[] bytes = Files.readAllBytes(decryptedFile.toPath());
//					String string = new String(bytes);
//
//					String decryptedContents = lhsEncDecUtl.decrypt(string, secretKey, salt);
//
//					Arrays.stream(decryptedContents.split("\n")).forEach(property -> {
//						if (!strUtl.isNull(property)) {
//							property = property.replaceAll("[\r\n]", property);
//							StringTokenizer tokens = new StringTokenizer(property, "=");
//							props.put(tokens.nextToken(), tokens.nextToken());
//						}
//					});
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return props;
//	}// end method
//
//	@Bean(name = "jdbcCustom")
//    @Autowired
//    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
