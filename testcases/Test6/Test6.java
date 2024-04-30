class Test6
{
	static class Element
	{
		int data;
		Element next;
        int[] arr;
		Element()
		{
			data=0;
			next=null;
            arr = new int[1000000];
            for(int i=0;i<1000000;++i)
            {
                arr[i] = i *10;
            }
		}
		Element(int d)
		{
			data=d;
			next=null;
		}
	}
	static void L(int p,int q,Element obj)
	{
		Element u=new Element();
		Element t=u;
		if(p>q)
		{
			t.next=new Element(q);
			t.data=p;
			t=t.next;
		}
        u = new Element();
		obj.next=t;
        obj = obj.next;
        obj.next= new Element();
        obj = obj.next;
        obj.next= new Element();
        obj = obj.next;
        obj.next= new Element();
        obj.next = new Element();
        obj.next = u;
        u = new Element();
	}
	public static void main(String[] args) {
		Element obj=new Element(10);
		Element o=new Element();
		L(10,20,obj);
	}
}