import java.util.*;


class Node{
    int f;
    int g;
    Node(int u,int v){
        this.f = u;
        this.g = v;
    }
}

class Child1 extends Node{
    int h;
    Child1(int u1,int v1,int w1){
        super(u1, v1);
        this.h = w1;
		Node var = new Node(u1,v1);
    }
}

class Child2 extends Child1{
    int var1;
    Child2(int u1,int u2,int u3,int u4){
        super(u1, u2, u3);
        this.var1 = u4;
		Node n = new Node(u1, u2);
		Child1 var = new Child1(u1, u2, u3);
    }
}
class Child3 extends Child2{
    int var2;
    Child3(int u1,int u2,int u3,int u4,int u5){
        super(u1, u2, u3, u4);
        this.var2 = u5;
		Node n = new Node(u1, u2);
		Child1 var = new Child1(u1, u2, u3);
		Child2 var1= new Child2(u1, u2, u3, u4);
    }
}
class Child4 extends Child3{
    int var3;
    Child4(int u1,int u2,int u3,int u4,int u5,int u6){
        super(u1, u2, u3, u4, u5);
        this.var3 = u6;
		Node n = new Node(u1, u2);
		Child1 var = new Child1(u1, u2, u3);
		Child2 var1= new Child2(u1, u2, u3, u4);
		Child3 var2 = new Child3(u1, u2, u3, u4, u5);
    }
    void foo(){

    }
}
class Test8{
    public static void main(String [] args){
        int i,j,n = 100000;
        for(i=0;i<n;++i)
        {
            Child4 c4 = new Child4(10, 20, 30, 40, 50, 60);
            c4 = new Child4(30, 40, 50, 60, 70, 80);
            c4.foo();
        }
        
    }
}