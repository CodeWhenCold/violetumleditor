package com.horstmann.violet.product.diagram.classes;
import com.horstmann.violet.product.diagram.abstracts.StatisticalGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//import com.horstmann.violet.framework.util.Statistics;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.classes.edge.*;
import com.horstmann.violet.product.diagram.classes.node.*;

import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.property.text.LineText;

/**
 * A UML class diagram.
 */
public class ClassDiagramGraph extends AbstractGraph implements StatisticalGraph{
	
	public void evaluateStatistics() {
		
	}
	public List<String> evaluateViolations(){
		List<String> violations = new ArrayList<String>();
		violations.addAll(CheckRecursionConstraint());
		violations.addAll(CheckBiDirectionalConstraint());
		violations.addAll(CheckInheritanceConstraint());
		return violations;

	}
	public List<INode> getNodePrototypes() {
		return NODE_PROTOTYPES;
	}

	public List<IEdge> getEdgePrototypes() {
		return EDGE_PROTOTYPES;
	}

	private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(new ClassNode(),
			new InterfaceNode(), new EnumNode(), new PackageNode(), new BallAndSocketNode(), new NoteNode()));

	private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(
			Arrays.asList(new DependencyEdge(), new InheritanceEdge(), new InterfaceInheritanceEdge(),
					new AssociationEdge(), new AggregationEdge(), new CompositionEdge(), new NoteEdge()));


	/**
	 * Check for multiple recursive relationships on class nodes and warn the user 
	 */
	private List<String> CheckRecursionConstraint() {
		List<String> violations = new ArrayList<String>();

		Collection<INode> nodes = getAllNodes();
		for (INode node : nodes) {
			boolean hasRecursiveRelationship = false;
			if (node instanceof ClassNode) {
				List<IEdge> edges = ((ClassNode) node).getConnectedEdges();
				for (IEdge edge : edges) {
					if (edge.getStartNode() == edge.getEndNode()) {
						if (hasRecursiveRelationship) {
							ClassNode cnode = (ClassNode) node;
							violations.add("Multiple recursive relationships in node "
									+ (cnode.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : cnode.getName()));
						}
						hasRecursiveRelationship = true;
					}
				}

			}

		}
		return violations;
	}

	/**
	 * Check for bi-directional aggregation/composition relationships between class nodes and warn the user 
	 */
	private List<String> CheckBiDirectionalConstraint() {
		List<String> violations = new ArrayList<String>();
		Collection<IEdge> edges = getAllEdges();
		IEdge[] edgesArray = edges.toArray(new IEdge[edges.size()]);

		for (int i = 0; i < edgesArray.length; i++) {
			if (!(edgesArray[i] instanceof CompositionEdge || edgesArray[i] instanceof AggregationEdge)) {
				continue; // Edge is not Composition or Aggregation edge
			}
			for (int j = 0; j < edgesArray.length; j++) {
				if (j <= i) { // Check if two edges have already been compared
					continue; // Edges already checked
				}
				if (!(edgesArray[j] instanceof CompositionEdge || edgesArray[j] instanceof AggregationEdge)) {
					continue; // Edge is not a Composition or Aggregation edge
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getStartNode()) {
					continue; //Both nodes are the same
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getEndNode()) {
					if (edgesArray[j].getStartNode() == edgesArray[i].getEndNode()) {
						ClassNode class1 = (ClassNode) edgesArray[i].getStartNode();
						ClassNode class2 = (ClassNode) edgesArray[i].getEndNode();
						violations.add("Multiple composition/aggregation relationships between "
								+ (class1.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class1.getName())
								+ " and "
								+ (class2.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class2.getName()));
					}
				}

			}

		}
		return violations;

	}

	/**
	 * Check for bi-directional inheritance relationships between class nodes and warn the user 
	 */
	private List<String> CheckInheritanceConstraint(){
		List<String> violations = new ArrayList<String>();
		Collection<IEdge> edges = getAllEdges();
		IEdge[] edgesArray = edges.toArray(new IEdge[edges.size()]);

		for (int i = 0; i < edgesArray.length; i++) {
			if (!(edgesArray[i] instanceof InheritanceEdge)) {
				continue; // Edge is not an inheritance edge
			}
			for (int j = 0; j < edgesArray.length; j++) {
				if (j <= i) { // Check if two edges have already been compared
					continue; // Edges already checked
				}
				if (!(edgesArray[j] instanceof InheritanceEdge)) {
					continue; // Edge is not an inheritance edge
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getStartNode()) {
					continue; //Both nodes are the same
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getEndNode()) {
					if (edgesArray[j].getStartNode() == edgesArray[i].getEndNode()) {
						ClassNode class1 = (ClassNode) edgesArray[i].getStartNode();
						ClassNode class2 = (ClassNode) edgesArray[i].getEndNode();
						violations.add("Multiple inheritance relationships between "
								+ (class1.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class1.getName())
								+ " and "
								+ (class2.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class2.getName()));
					}
				}

			}

		}
		return violations;

    }
	
public void writeStats() throws IOException{

		File dir = new File("ClassModelStatistics");
    	File file = new File("ClassModelStatistics/Diagram.txt");
    	
    	if (!dir.exists()) {
            dir.mkdir();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        
        bw.write("Number of Classes: ");
        bw.write(Integer.toString(getNumOfClasses()));
        bw.newLine();
        bw.newLine();
       
        
        bw.write("Name of Classes: ");
        for (int i = 0; i < getNamesOfClasses().size(); i++) {
            bw.write(getNamesOfClasses().get(i).toString() + " ");
        }
        bw.newLine();
        bw.newLine();
        
        
        bw.write("Number of RelationShips between Classes and Interfaces: ");
        bw.write(Integer.toString(getNumOfRelationships()));
        bw.newLine();
        bw.newLine();
        
        
        bw.write("Class sizes in terms of functions per class: ");
        bw.newLine();
        for(int i=0; i<sizeOfClasses().size(); i++){
        	bw.write(sizeOfClasses().get(i).toString());
        	bw.newLine();
        }
        
        bw.newLine();
        bw.write("Coupling Between objects: ");
        bw.newLine();
        for(int i=0; i<cbo().size(); i++){
        	bw.write(cbo().get(i).toStringCouple());
        	 bw.newLine();
        }
        
        
        bw.flush();
        bw.close();
    }

public int getNumOfClasses(){
	int numOfClasses = 0;
	Collection <INode> nodes = getAllNodes();
	ArrayList <INode> arrNode = new ArrayList<INode>();
	for (INode node : nodes) {
		if (node instanceof ClassNode)
			arrNode.add(node);
	}
	numOfClasses= arrNode.size();
	
	return numOfClasses;
}





public List<LineText> getNamesOfClasses(){
	ArrayList<LineText> listNames = new ArrayList<LineText>();
	Collection<INode> nodes = getAllNodes();
	INode[] nodesArray = nodes.toArray(new INode[nodes.size()]);
	for(int i=0; i < nodesArray.length; i++){
		if(nodesArray[i] instanceof ClassNode){
			ClassNode node = (ClassNode) nodesArray[i];
			listNames.add(node.getName());
		}
	}
	
	
	return listNames;
}


public int getNumOfRelationships(){
	int numOfRelationships=0;
	Collection <IEdge> edges = getAllEdges();
	ArrayList<IEdge> arrEdge = new ArrayList<IEdge>();
	for(IEdge edge : edges){
		//relationship between classes
		if(edge.getStartNode() instanceof ClassNode && edge.getEndNode() instanceof ClassNode)
		{
			arrEdge.add(edge);
		}
		//relationship between a class and interface (suggestion from TA)
		if(edge.getStartNode() instanceof ClassNode && edge.getEndNode() instanceof InterfaceNode)
		{
			arrEdge.add(edge);
		}
	}
	numOfRelationships=arrEdge.size();
	
	return numOfRelationships;
}






public List<ClassStatistics> sizeOfClasses() {
	
	List<ClassStatistics> list= new ArrayList<ClassStatistics>();
	Collection<INode> nodes = getAllNodes();
	INode[] nodesArray = nodes.toArray(new INode[nodes.size()]);
	
	for(int i=0; i < nodesArray.length; i++){
		if(nodesArray[i] instanceof ClassNode){
			ClassNode node = (ClassNode) nodesArray[i];
			int size;
			size=node.getNumOfMethods();
			LineText name=node.getName();
			ClassStatistics classNode = new ClassStatistics(name,size);
			list.add(classNode);
			
		}
	}
	
	
	
	return list;
}

public List<ClassStatistics> cbo(){
	List<ClassStatistics> listCBO= new ArrayList<ClassStatistics>();
	Collection<INode> nodes = getAllNodes();
	INode[] nodesArray = nodes.toArray(new INode[nodes.size()]);
	
	for(int i=0; i < nodesArray.length; i++){
		if(nodesArray[i] instanceof ClassNode){
			ClassNode node = (ClassNode) nodesArray[i];
			LineText name = node.getName();
			List<IEdge> edges = node.getConnectedEdges();
			int coupling = edges.size();
			ClassStatistics classNode = new ClassStatistics (name,coupling);
			listCBO.add(classNode);
		}
	}
	
	return listCBO;
	
}



}
