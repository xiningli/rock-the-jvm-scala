run the following command for packaging all jars

``` bash
sbt xitrumPackage
```

``` bash
sbt package
```

Run this to use the scala compiler

``` bash
echo "println(2)" | java -cp "./target/xitrum/lib/*" com.xiningli.scalapractice.Hi
```