# Spring Boot Multi Datasource 

## 버전
- Spring Boot 1.3.0.RELEASE 기준

## 소개
- Spring Boot 에서 DataSource 한개 이상 사용시 쉽게 설정할 수 있도록 지원    
- 간단히 Dependency 추가와 어노테이션 만으로도 쉽게 사용 가능 
- @DataSource 어노테이션을 통한 DataSource Routing
- 커스텀 DataSource를 사용하여 이용 가능

## 실행조건
- JDK 1.6 이상 환경 ( JDK 1.7 이상 추천 )
- Maven 설치
- Spring Boot 개발 환경

## 소스 내려받기
```sh
$ git clone https://github.com/sbcoba/spring-boot-multidatasource.git
```

## 샘플 실행
```sh
$ cd spring-boot-multidatasource
$ mvn clean install
# 샘플 실행
$ cd spring-boot-multidatasource-sample
$ mvn -pl spring-boot-multidatasource-sample-default spring-boot:run
```

## Maven Dependency 설정
```xml
<dependency>
  <groupId>org.sbcoba.springboot</groupId>
  <artifactId>spring-boot-multidatasource</artifactId>
  <version>0.3.0.BUILD-SNAPSHOT</version>
</dependency>
```

## 예제설명
- spring-boot-multidatasource-sample-default
	- 다양한 데이타 소스를 변경하며 테스트 가능 
	- H2 DB를 이용하여 쉽게 확인 가능
