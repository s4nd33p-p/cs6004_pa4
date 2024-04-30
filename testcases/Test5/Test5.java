class Test5
{
	Test5 f1;
	public static void main(String[] args) {
		Test5 o1 = new Test5();
		Test5 o2 = new Test5();
		Test5 o3 = new Test5();
		o1.f1 = o2;
		o2.f1 = o3;
        o3 = new Test5();
		Test5 o4 = o1.bar(o1.f1); 
		o4.f1 = new Test5();
	}
	Test5 bar (Test5 p1) {
        for(int i=0;i<100000000;++i)
        {
            Test5 var1 = new Test5();
            i += 2;
        }
		Test5 o5 = new Test5();

		return o5;
	}
}