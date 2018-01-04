/**
 * 静态代理
 */
public class HelloProxy implements Hello{
    private Hello hello;
    public HelloProxy(){
        hello=new HelloImpl();
    }
    @Override
    public void sayHello(String name) {
        before();
        hello.sayHello(name);
        after();
    }

    private void after() {
        System.out.println("Before");
    }

    private void before() {
        System.out.println("After");
    }

    public static void main(String[] args){
        HelloProxy helloProxy=new HelloProxy();
        helloProxy.sayHello("Jack");
    }
}
