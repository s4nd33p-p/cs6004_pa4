
class Test7
{
	Test7 f1;
	Test7 f2;
	public static Test7 global;
	public static void main(String[] args) {
		int i = 0,num = 1000000;
		Test7 var1 = new Test7();
		Test7 var2 = new Test7();
		for(i=0; i<num ;++i)
		{
			Test7 var = var1.foo(var1,var2,num);
			var.foo(var1,var2,num);
		}
	}
	
	Test7 foo(Test7 p1, Test7 p2, int val ) {
		Test7 t;
		t = new Child4();
		t.foo(p1,p2,val);
		return new Test7();
	}
}

class Child1 extends Test7 {
	Test7 foo(Test7 p1, Test7 p2, int val ) {
		Child2 c1 = new Child2();
		c1.foo(p1,p2,val);
        Test7 obj1 = new Test7();
		Test7 obj2 = new Test7();
		c1 = new Child2();
		return new Test7();
	}
}

class Child2 extends Test7 {
	Test7 foo(Test7 p1, Test7 p2, int val ) {
		Child3 c1 = new Child3();
		c1.foo(p1,p2,val);
		c1 = new Child3();
		c1 = new Child3();
        Test7 obj1 = new Test7();
		Test7 obj2 = new Test7();
		return new Test7();
	}
}

class Child3 extends Test7{
	Test7 foo(Test7 p1, Test7 p2,int val){
        Test7 obj1 = new Test7();
		Test7 obj2 = new Test7();
		for(int i=0;i<val;++i)
		{
			Child4 c1 = new Child4();
			c1.foo(p1,p2,val);
		}
		return new Test7();
	}
}

class Child4 extends Test7{
	Test7 foo(Test7 p1, Test7 p2, int val){
		Test7 obj1 = new Test7();
		Test7 obj2 = new Test7();
		Test7 obj4 = new Test7();
		Test7 obj5 = new Test7();
		Test7 obj6 = new Test7();
		Test7 obj7 = new Test7();
		Test7 obj8 = new Test7();
		Test7 obj9 = new Test7();
		Test7 obj10 = new Test7();
        Test7 obj11 = new Test7();
		Test7 obj12 = new Test7();
		f1 = obj9;
		f2 = obj10;
		f1 = obj10;
		f2 = obj9;
		f1 = f2 = obj1;
		return new Test7();
	}
}