# VlINE

[文档](https://doc.bootvue.com)

## Start

1. install `GraalVm`

2. `gu install native-image` and `install docker` [ **optional**: 如果需要构建*spring native*应用 ]

3. build

   ```bash
   
     # build jar
     gradlew bootJar -Dprofile=dev/prod -Dnacos=false ...
   
     # build native binary
     gradlew nativeCompile
   
     # build docker image
     gradlew bootBuildImage
             
   ```

### 环境变量

| ENV |  默认值   | DESC |
| :-----|:------:| :----: |
| profile |  dev   | 指定构建环境, 不同环境下模块dependencies可能有差异 |
| nacos | false  | 是否集成nacos |
| mysql | false  | 是否集成mysql |
| redis | false  | 是否集成redis |

## short desc

1. 默认集成 `spring-boot-starter-web` & `spring-cloud`, 需要瘦身的场景可以自行裁剪不必要的依赖

## ToDo

- [ ] 坐等`spring cloud alibaba` 支持`springboot2.7+ / 3+` ( 目前基于 `spring native` 低版本构建  )
- [ ] `spring native` 不支持 `logback.xml` (继续观望)