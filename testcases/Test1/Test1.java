

class Test1Node {
	Test1Node f;
	Test1Node g;
}
public class Test1 {
	public static Test1Node global;
	public static void main(String[] args) {
		foo();
	}
	public static Test1Node foo(){ 
		Test1Node x = new Test1Node();
		x.f = new Test1Node();
		Test1Node y = new Test1Node(); 
		Test1Node z = new Test1Node();
		return x;
	}

}
