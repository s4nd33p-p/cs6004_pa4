import java.util.*;
import soot.*;
import soot.SootMethod;
import soot.jimple.AnyNewExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;
//import soot.jimple.internal.;
//import soot.jimple.;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.RValueBox;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
//import soot.jimple.Loop;
//import soot.jimple.;
import soot.jimple.StaticFieldRef;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.jimple.internal.AbstractInstanceFieldRef;
import javafx.util.Pair;
import java.util.concurrent.locks.ReentrantLock;
//import java.jimple.internal;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

class edge{
	String u;
	String v;
	String label;
	edge(String x,String y)
	{
		this.u=x;
		this.v=y;
		this.label="";
	}
	edge(String x,String y,String z)
	{
		this.u=x;
		this.v=y;
		this.label=z;
	}
}
class points_to_graph{
	// Vertices is a set, a vertex can be a variable like 'var1' or an object line no. '2'
	// escaping var names param_escape, global_escape, ret_escape
	Set<String> Vertices = new HashSet<String>();
	Set<String> final_escaping = new HashSet<String>();

	//Below HashMap stores graph as an adjacency list, 
	//where key is Vertex and Value will be a vector of pairs(object,label).
	Map<String, Set<Pair<String,String>>> AdjList = new HashMap<>();
	//    Map<String, Set<Pair<String,String>>> Adj2 = new HashMap<>();
	//edge is of form e.u --> e.v 
	// e.u is the key to the Adjlist and {...,e.v}
	void AddEdge(edge e)
	{
		Vertices.add(e.u);
		Vertices.add(e.v);
		if(AdjList.containsKey(e.u))
		{
			Set<Pair<String,String>> temp=AdjList.get(e.u);
			temp.add(new Pair<String,String>(e.v,e.label));
			AdjList.put(e.u, temp);
		}
		else
		{
			Set<Pair<String,String>> temp=new HashSet<Pair<String,String>>();
			temp.add(new Pair<String,String>(e.v,e.label));
			AdjList.put(e.u,temp);
		}
		//            System.out.println("label inside create edge"+e.label);
		

	}


	void DeleteEdge(edge e)
	{
		Vector<Pair<String,String>> temp = new Vector<Pair<String,String>>();
		AdjList.get(e.u).remove(new Pair<String,String>(e.v,e.label));
	}

	void DeleteRefEdge(String var, String label) // for strong update
	{
		Vector<String> var_objs = this.GetPointsToList(var);
		Vector<String> ref_objs = this.GetPointsToList(var,label);
		if(var_objs!=null)
		{
			for(String u:var_objs)
			{
				if(ref_objs!=null)
				{
					for(String v:ref_objs)
					{
						edge e = new edge(u,v,label);
						this.DeleteEdge(e);

					}
				}
			}
		}
	}

	//This basically prints the Points-to-information for variable u;
	void printadjlist(String u)
	{
		if(AdjList.get(u)!=null)
		{
			System.out.println(u+" points to:");
			Set<Pair<String,String>> t=AdjList.get(u);
			for(Pair<String,String> temp: t)
			{
				System.out.println("  "+temp.getKey()+" "+temp.getValue());
			}
		}
	}

	//this returns points-to set for a reference variable u
	Vector<String> GetPointsToList(String u)
	{
		Vector<String> ToReturn = new Vector<String>();
		if(AdjList.get(u)!=null)
		{
			for(Pair<String,String> p:AdjList.get(u))
			{
				ToReturn.add(p.getKey());
			}
			return ToReturn;
		}
		return null;
	}

	//this returns points-to set for a Object reference O.u
	Vector<String> GetPointsToList(String Base,String u)
	{
		Vector<String> ToReturn = new Vector<String>();
		//    	Vector<Pair<String,String>> temp = AdjList.get()
		if(AdjList.get(Base)!=null)
		{
			for(Pair<String,String> p:AdjList.get(Base))
			{
				Set<Pair<String,String>> innerlist = AdjList.get(p.getKey());
				if(innerlist != null)
				{
					for(Pair<String,String> inp: innerlist)
					{
						if(inp.getValue().equals(u))
						{
							ToReturn.add(inp.getKey());
						}
					}
				}
			}
			return ToReturn;
		}
		return null;
	}

	void handle_invocation(JInvokeStmt u)
	{
		JInvokeStmt stmt = ( JInvokeStmt ) u;
		List<Value> list = stmt.getInvokeExpr().getArgs();
//		System.out.println(u);

			if( list != null) {
				for( Value param : list) {
					Vector<String> templist = this.GetPointsToList(param.toString());

					if( templist != null )
					{
						for( String v : templist )
						{
							edge e = new edge("param_escape", v);
							this.AddEdge(e);
						}
					}
				}
			}
			
			if( stmt.getInvokeExpr() instanceof JVirtualInvokeExpr)
			{
//				System.out.println(stmt);
				JVirtualInvokeExpr ex = ( JVirtualInvokeExpr) stmt.getInvokeExpr();
				String base = ex.getBase().toString();
				Vector<String> temp = this.GetPointsToList(base);
				if(temp != null)
				{
					for( String v: temp)
					{
						edge e = new edge("param_escape",v);
						this.AddEdge(e);
					}
				}
				else
				{
					// dummy
					String obj = new String("O_Dum_"+base.toString());
					edge e = new edge( base.toString(), obj);
					this.AddEdge(e);
					edge e2 = new edge( "param_escape", base.toString());
					this.AddEdge(e2);
				}
			}
		

	}
	// heart of the Points to graph updation here we will update points to information 
	// by processing each kind of statement that 'u' can take
	boolean TransformPointsToGraph(Unit u)
	{
		Map<String, Set<Pair<String,String>>> BeforeAdjList = new HashMap<>(this.AdjList);


		if( u instanceof JReturnStmt)	{
			JReturnStmt stmt = (JReturnStmt) u;
			String retvar= stmt.getOp().toString();
			Vector<String> escapingobjs = this.GetPointsToList(retvar);
			if( escapingobjs != null)
			{
				for( String v: escapingobjs)
				{
					edge e = new edge( "ret_escape", v);
					this.AddEdge(e);
				}
			}
			//    		 return false;
		}

		else if( u instanceof JIdentityStmt)	{	//r7 := @parameter1: Node or r7 := @this: Node
			
//			System.out.println(u);
			JIdentityStmt param = (JIdentityStmt) u ;
			Value lhs = param.getLeftOp();
			Value rhs = param.getRightOp();
			String v = new String ( "implicit_escape");
			String dummy = new String("O_Dum_"+lhs.toString());
			edge e  = new edge ( lhs.toString(), dummy);
			edge e2  = new edge ( v,lhs.toString());
			this.AddEdge(e);
			this.AddEdge(e2);
		
		}

		else if( u instanceof JInvokeStmt)	{	//staticinvoke <Test: Node bar(Node,Node)>($r9, $r5) need not be static
//			    		System.out.println(u);
			JInvokeStmt stmt = ( JInvokeStmt ) u;
			this.handle_invocation(stmt);
			
			if(stmt.getInvokeExpr().getMethod().isConstructor()) {
				return false;
			}
		}

		else if (u instanceof JAssignStmt) { // is it an assignment statement  a= new A()??
			JAssignStmt stmt = (JAssignStmt) u;
			Value rhs = stmt.getRightOp(); 
			Value lhs= stmt.getLeftOp();
			int LineNumber=u.getJavaSourceStartLineNumber();
//			            System.out.println(u.toString());
			if( rhs instanceof JStaticInvokeExpr)
			{
//				System.out.println(rhs);
				JStaticInvokeExpr stmt2 = ( JStaticInvokeExpr ) rhs;
				List<Value> list = stmt.getInvokeExpr().getArgs();

				if( list != null) {
					for( Value param : list) {
						Vector<String> templist = this.GetPointsToList(param.toString());

						if( templist != null)
						{
							for( String v : templist )
							{
								edge e = new edge("param_escape", v);
								this.AddEdge(e);
							}
						}
					}
				}
				
				Vector<String> llist = this.GetPointsToList(lhs.toString());
				if(llist == null)	{
					
					String obj = new String("O_Dum_"+lhs.toString());
					edge e = new edge( lhs.toString(), obj);
					this.AddEdge(e);
					edge e2 = new edge( "param_escape", lhs.toString());
					this.AddEdge(e2);
				}
				else
				{
					for( String v: llist)
					{
						edge e = new edge( "param_escape",v);
					}
				}
				
				
			}
			
			else if( rhs instanceof JVirtualInvokeExpr)
			{
//				System.out.println(u);
				JVirtualInvokeExpr stmt2 = ( JVirtualInvokeExpr ) rhs;
				List<Value> list = stmt.getInvokeExpr().getArgs();
				if (stmt2.getBase()!=null) {
					edge e = new edge ("param_escape",stmt2.getBase().toString());
					this.AddEdge(e);
				}

				if( list != null) {
					for( Value param : list) {
						Vector<String> templist = this.GetPointsToList(param.toString());

						if( templist != null )
						{
							for( String v : templist )
							{
								edge e = new edge("param_escape", v);
								this.AddEdge(e);
							}
						}
					}
				}
				
				Vector<String> llist = this.GetPointsToList(lhs.toString());
				if(llist == null)	{
					
					String obj = new String("O_Dum_"+lhs.toString());
					edge e = new edge( lhs.toString(), obj);
					this.AddEdge(e);
					edge e2 = new edge( "param_escape", lhs.toString());
					this.AddEdge(e2);
				}
				else
				{
					for( String v: llist)
					{
						edge e = new edge( "param_escape",v);
					}
				}
				
				
			}
			else if (rhs instanceof JNewExpr) { 		//... = new ()
				try {				
					if(lhs instanceof JimpleLocal || lhs instanceof RValueBox)
					{
						edge e = new edge(lhs.toString(),Integer.toString(LineNumber));
						this.AddEdge(e);             
					}

				} catch (Exception e) {
					System.out.println(e.toString()+" for "+u.toString());
				}
			}

			// statements of the form ... = $r1 what are possible lhs
			// $r0 = $r1 or $r0.f = $r1
			else if( rhs instanceof JimpleLocal || rhs instanceof RValueBox)
			{
				try {
					if(lhs instanceof JimpleLocal || rhs instanceof RValueBox)
					{
						Vector<String> temp = this.GetPointsToList(rhs.toString());
						if(temp!=null)
						{
							for(String p:temp)
							{
								edge e=new edge(lhs.toString(),p);
								this.AddEdge(e);
							}
						}
					}
				} catch (Exception e) {
					System.out.println(e.toString()+" for "+u.toString());
				}
				
				try {
					if(lhs instanceof JInstanceFieldRef) // $r0.f = $r1 need to add $r0.f--> $r1
					{
						JInstanceFieldRef fref=(JInstanceFieldRef)lhs;
						Vector<String> lhslist=this.GetPointsToList(fref.getBase().toString());
						
						if(lhslist!=null)
						{
							for(String lpair:lhslist)
							{
								Vector<String> rhslist=this.GetPointsToList(rhs.toString());
								if(rhslist != null)
								{
									for(String rpair:rhslist)
									{

										edge e= new edge( lpair,rpair,fref.getField().toString());
										this.AddEdge(e);
									}
								}
							}
						}
						else	{ // add a dummy node
							String obj = new String("O_Dum_"+fref.getBase().toString());
							edge e1 = new edge ( fref.getBase().toString(), obj);
							this.AddEdge(e1);
							Vector<String> rhslist = this.GetPointsToList(rhs.toString());
							if(rhslist!=null)
							{
								for( String r:rhslist)
								{
									edge e = new edge ( obj , r, fref.getField().toString());
									this.AddEdge(e);
								}
							}
						}

					}
				} catch (Exception e)
				{
					System.out.println(e.toString()+" for "+u);
				}
				
				if( lhs instanceof StaticFieldRef)	// needs to be escaped
				{
					Vector<String> templist = this.GetPointsToList(rhs.toString());

					if( templist != null )
					{
						for( String v : templist )
						{
							edge e = new edge("param_escape", v);
							this.AddEdge(e);
						}
					}
				}

			}

			// ... = $r0.f
			else if(rhs instanceof JInstanceFieldRef)
			{
				try {
					if(lhs instanceof JimpleLocal || lhs instanceof RValueBox)
					{
						JInstanceFieldRef fref=(JInstanceFieldRef)rhs;

						String label_rhs=fref.getField().toString();
						String base_rhs = fref.getBase().toString();
						Vector<String> rlist=this.GetPointsToList(base_rhs,label_rhs);
						if(rlist != null)
						{
							for( int i=0;i<rlist.size();i++)
							{
								String r=rlist.elementAt(i);
								edge e = new edge(lhs.toString(),r);
								this.AddEdge(e);
							}
						}

					}
				} catch (Exception e)
				{
					System.out.println(e.toString()+" for "+u);
				}

			}
			else
			{
//				System.out.println(u);
				return false;
			}

		}
//		return false;
		boolean HasUpdated = true;
		if(BeforeAdjList.equals(this.AdjList))
		{
			HasUpdated=false;
		}
		return HasUpdated;

	}

	void populate_escapelist( String obj)
	{
		try {
		    int intValue = Integer.parseInt(obj);
		    this.final_escaping.add(obj);
		} catch (NumberFormatException e) {
		}
		Vector<String> toiterate = this.GetPointsToList(obj);
		if(toiterate != null)
		{
			for(String s: toiterate)
				populate_escapelist(s);

		}
		return;
	}

	void print_final_escaping()
	{
		if( !this.final_escaping.isEmpty())
		{
			for( String s : this.final_escaping)
			{
				System.out.println(s);
			}
		}
	}

}

class WorkingList{

	Queue<Unit> worklistqueue;

	WorkingList()
	{
		this.worklistqueue = new LinkedList<>();

	}

	void AppendToWL(Unit u)
	{
		worklistqueue.offer(u);
	}

	Unit getNextFromWL()
	{
		return worklistqueue.poll();
	}

	boolean IsWLEmpty()
	{
		return worklistqueue.isEmpty();
	}
}

public class AnalysisTransformer extends BodyTransformer {
	
	Map<String , Set<String>> all_methods = new HashMap<>();
	@Override
	protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
		// Construct CFG for the current method's body
		//internal transform will be called on each method
		// As we are doing inter-procedural analysis the whole analysis logic should be here
		// and we don't need to look into the other function definitions as well
		//        PatchingChain<Unit> units = body.getUnits();
		// Iterate over each unit of CFG- Control Flow Graph
		// Shown how to get the line numbers if the unit is a "new" Statement.
		// Unit or Node . Fixed point logic should be there . stop when out-graph does not change.
		// Then perform Your analysis on the Graph to find escaping variables.
		//JInvoke corresponds to a function call, we can get parameters there.
		points_to_graph PGraph = new points_to_graph();
		UnitGraph CFG= new BriefUnitGraph(body);
		WorkingList  wl = new WorkingList();
		for(Unit startunit: body.getUnits())
		{
			
				wl.AppendToWL(startunit);

		}
	
		while(!wl.IsWLEmpty())
		{
			Unit curr_unit = wl.getNextFromWL();
//			System.out.println(curr_unit);
			boolean modified= PGraph.TransformPointsToGraph(curr_unit);
			if(modified)
			{
				if(CFG.getSuccsOf(curr_unit)!= null)
				{
					for( Unit succ_unit : CFG.getSuccsOf(curr_unit))
					{
						wl.AppendToWL(succ_unit);
					}
				}
			}

		}
		// Printing Points to information
//		for(String s:PGraph.Vertices)
//		{
//			PGraph.printadjlist(s);
//			System.out.println();
//		}

		Vector<String> escaping_vars = new Vector<String>();
		escaping_vars.add(new String("param_escape"));
		escaping_vars.add(new String("ret_escape"));
		escaping_vars.add(new String("global_escape"));
		escaping_vars.add(new String("implicit_escape"));

		for( String var: escaping_vars)
		{
			//        	final_escaping.add(var);
			Vector<String> escaping_objs = PGraph.GetPointsToList(var);
			if( escaping_objs != null)
			{
				for( String obj: escaping_objs)	{
					PGraph.populate_escapelist( obj);
				}
			}

		}

		all_methods.put(body.getMethod().getDeclaringClass()+":"+body.getMethod().getName().toString(),PGraph.final_escaping);
		
	}
	
	  public void printEscapingObjects() {	// Does Lexicographical sort of all method names and prints them
	        List<String> sortedMethods = new ArrayList<>(all_methods.keySet());
	        Collections.sort(sortedMethods);

	        for (String method : sortedMethods) {
	            Set<String> escapingVars = all_methods.get(method);
	            if (escapingVars != null && !escapingVars.isEmpty()) {
	                System.out.print(method+" ");
	                List<Integer> lineNumbers = new ArrayList<>();
	                for (String var : escapingVars) {
	                    lineNumbers.add(Integer.parseInt(var));
	                }
	                Collections.sort(lineNumbers);
	                for (int lineNumber : lineNumbers) {
	                    System.out.print(lineNumber + " ");
	                }
	                System.out.println();
	            }
	        }
	  }
	
}