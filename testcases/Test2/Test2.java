// Testcase 1:
class Test2
{
	Test2 f1;
	Test2 f2;
	public static Test2 global;
	public static void main(String[] args) {
		int i = 0,num = 1000000;
		Test2 var1 = new Test2();
		Test2 var2 = new Test2();
		for(i=0; i<num ;++i)
		{
			Test2 var = var1.foo(var1,var2,num);
			var.foo(var1,var2,num);
		}
	}
	
	Test2 foo(Test2 p1, Test2 p2, int val ) {
		Test2 t;
		t = new Child4();
		if(val%3 == 0)
		{
			t = new Child1();
			t.foo(p1,p2,val);
		}
		else if(val%6 == 0)
		{
			t = new Child2();
			t.foo(p1,p2,val);

		}
		else{
			t = new Child3();
			t = new Child4();
			t.foo(p1,p2,val);
			t.foo(p1,p2,val);
		}

		t = new Child4();
		t.foo(p1,p2,val);
		return new Test2();
	}
}

class Child1 extends Test2 {
	Test2 foo(Test2 p1, Test2 p2, int val ) {
		Child2 c1 = new Child2();
		c1.foo(p1,p2,val);
		c1 = new Child2();
		return new Test2();
	}
}

class Child2 extends Test2 {
	Test2 foo(Test2 p1, Test2 p2, int val ) {
		Child3 c1 = new Child3();
		c1.foo(p1,p2,val);
		c1 = new Child3();
		c1 = new Child3();
		return new Test2();
	}
}

class Child3 extends Test2{
	Test2 foo(Test2 p1, Test2 p2,int val){
		for(int i=0;i<val;++i)
		{
			Child4 c1 = new Child4();
			c1.foo(p1,p2,val);
		}
		return new Test2();
	}
}

class Child4 extends Test2{
	Test2 foo(Test2 p1, Test2 p2, int val){
		Test2 obj1 = new Test2();
		Test2 obj2 = new Test2();
		Test2 obj4 = new Test2();
		Test2 obj5 = new Test2();
		Test2 obj6 = new Test2();
		Test2 obj7 = new Test2();
		Test2 obj8 = new Test2();
		Test2 obj9 = new Test2();
		Test2 obj10 = new Test2();
		f1 = obj9;
		f2 = obj10;
		f1 = obj10;
		f2 = obj9;
		f1 = f2 = obj1;
		return new Test2();
	}
}