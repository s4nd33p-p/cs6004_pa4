import java.util.List;

import soot.*;
import soot.Body;
import soot.NormalUnitPrinter;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPrinter;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.jimple.internal.*;
import soot.options.Options;

import javax.swing.text.html.Option;
public class PA4 {
    public static void main(String[] args) {
        String classPath = "."; 	// change to appropriate path to the test class
		String dir = "./testcases/Test8";

        //Set up arguments for Soot
        String[] sootArgs = {
            "-cp", classPath, "-pp",  // sets the class path for Soot
            "-w",                     // whole program analysis
            "-f", "class",                // jimple file
            "-keep-line-number",      // preserves line numbers in input Java files
            "-main-class", "Test8",	  // specify the main class
            "-process-dir", dir,      // directory of classes to analyze
        };

        // Create transformer for analysis
        AnalysisTransformer analysisTransformer = new AnalysisTransformer();

        // Add transformer to appropriate pack in PackManager; PackManager will run all packs when soot.Main.main is called
        PackManager.v().getPack("jtp").add(new Transform("jtp.gc", analysisTransformer));

        // Call Soot's main method with arguments
        String outputDir = "./sootOutput";  // Directory where the transformed Jimple files are stored
        Options.v().set_output_format(Options.output_format_class);  // Set output format to class files
        Options.v().set_output_dir(outputDir);  // Set output directory for class files
        soot.Main.main(sootArgs);
//        analysisTransformer.printEscapingObjects();
        

    }
}
