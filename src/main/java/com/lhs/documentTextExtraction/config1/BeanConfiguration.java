//package com.lhs.documentTextExtraction.config1;
//
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.lhs.javautilities.LhsEncryptionDecryptionUtility;
//import com.lhs.javautilities.LhsStringUtility;
//
//@Configuration
//public class BeanConfiguration {
//
//	@Value("${Property_File_Path}")
//	private String propFilePath;
//	
//	
//	
//	@Bean(name = "stringUtil")
//	public LhsStringUtility lhsStringUtility() {
//		return new LhsStringUtility();
//	}
//
//
//	@Bean(name = "encDecUtil")
//	public LhsEncryptionDecryptionUtility lhsEncryptionDecryptionUtility() {
//		return new LhsEncryptionDecryptionUtility();
//	}
//
//
//	@Bean
//	@Qualifier("dataSourceFilePath")
//	public String getDataSourceFilePath() {
//		String dirPath = "";
//		try {
//			String path = propFilePath + File.separator;
//			System.out.println("path..."+path);
//			List<File> roots = Arrays.asList(File.listRoots());
//
//			dirPath = path;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return dirPath;
//	}
//
//}
