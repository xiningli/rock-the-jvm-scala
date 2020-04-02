package com.xiningli.scalapractice.lectures.part2afp
// partially applied functions
object CurriesPAF extends App {

  // curried functions
  val superAdder: Int=>Int=>Int =
    x => y => x+y

  val add3 = superAdder(3)//Int=>Int = y=>3+y

  println(add3(5))// this will return the number 8
  println(superAdder(3)(5)) // this is called a curried function
  //Method!
  def curriedAdder(x:Int)(y:Int):Int = x+y // this is a curreid metho


  val add4: Int=>Int = curriedAdder(4)
  val eta: Int => Int = curriedAdder(500) _
  println(eta(3000))
  // converting from curried function to lifted function
  val adder4V2 = curriedAdder(4)(_)

  println(adder4V2(12312))

  // lifting... when you call a method, you have to supply
  // the entire list of arguments
  // AKA eta expansion

  // but using lifting, you are converting the def to val
  // converting a method to a function value type Int=>Int

  // functions != methods(JVM limitation)

  def inc(x:Int)=x+1
  List(1,2,3).map(x=>inc(x))// Eta-expansion


  // Partial Function Application
  val add5 = curriedAdder(5) _ // tell the compiler to turn into the Int=>Int

  // Exercise
  val simpleAddFunction =  (x:Int, y:Int)=>x+y
  val simpleAddFunctionV2: Int=>Int =  x => simpleAddFunction(22,x)
  val simpleAddFunctionV3: Int=>Int=>Int = x=>y=> {
    println("hahaha")
    simpleAddFunction(x,y)
  }
  println(simpleAddFunctionV2(44))
  println(simpleAddFunctionV3(1001)(110))

  def simpleAddMethod(x:Int, y:Int) = x+y


  val useSimpleAddMethodAsCurry = (x:Int)=>(y:Int)=>simpleAddFunction(x,y)
  println(useSimpleAddMethodAsCurry(2)(3))
  val add7 = (x:Int)=>simpleAddFunction(7,x)
  println(add7(7))
  println(simpleAddFunction.curried(12)(123))

  val myCurriedFunc = simpleAddFunction.curried

  println(myCurriedFunc(10)(-100))
  val uncurried = Function.uncurried(myCurriedFunc).tupled
  println(uncurried((9900,990)))

  println((simpleAddMethod _).curried(12)(123))
  val add7_version2 = simpleAddMethod(7,_:Int)// sugar
  println(add7_version2(770))
  // y=>SimpleAddMethod(y,5)
  def concatenator(a:String, b:String,c:String) = a+b+c
  val insertName =  (s:String)=>concatenator("Hello ", s, ". How are you")
      // compiler does the eta expansion for you. .

  println(insertName("Tupolev"))


  // EXERCISE

  /*
  1. process a list of numbers and return their string representations with different formats
  Use the %4.2f, %8.6f with a curried formatter function

  2. difference between
   - functions vs methods
   - parameters: by-name vs 0-lambda
   */
  def curriedFormatter(s:String)(number:Double):String = s.format(number)
  val numbers = List(123230.1237123,Math.PI, Math.E, 9.8, 1.3e-12)
  val simpleFormat = curriedFormatter("%2.2f") _

  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(preciseFormat))
  def byName(n: =>Int):Int = n+1
  def byFunction(f: ()=>Int)=f()+1
  def method:Int = 42//accesser method
  def parenMethod():Int = 42//proper method

  byName(23) // ok
  byName(method) // ok, because the method will be evaluated
  byName(parenMethod) // ok
  println(byName(parenMethod)) // ok but beware ===> byName(parenMethod())
  // it will call the parenMethod and pass the value into byName
  println(  byName((()=>42)()))//ok convert into a value
//  byName(parenMethod _ ) // not ok because this expression is a function value

//  byFunction(45)//not ok
//  byFunction(method) // with a parenmeterless method is not ok
  // because it will be automatically evaluated to 42
    // compiler does not do eta expansion here

  println(byFunction(parenMethod))//does eta expansion

  println(parenMethod)
}
