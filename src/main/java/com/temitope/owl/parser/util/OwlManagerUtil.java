package com.temitope.owl.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.temitope.ifc.parser.IFCFileParser;

public class OwlManagerUtil {
	private static OwlManagerUtil instance;
	private static OWLOntologyManager owlOntologyManager;
	private static OWLOntology owlOntology;

	static {
		owlOntologyManager = OWLManager.createOWLOntologyManager();
	}

	// private constructor which will not allow create object directly to other
	// classes.
	private OwlManagerUtil() {
		// intentionally created empty constructor
	}

	public static OwlManagerUtil getInstance() {
		if (instance == null) {
			synchronized (OwlManagerUtil.class) {
				if (instance == null) {
					instance = new OwlManagerUtil();

				}
			}
		}
		return instance;
	}

	public OWLOntology getOwlOntologyByUrl(String url) {
		OWLOntology owlOntology = null;
		if (url != null || !url.isEmpty()) {
			try {
				IRI iri = IRI.create(url);
				owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(iri);

			} catch (OWLOntologyCreationException e) {
				System.out.println("Failed to create OWLOntology");
				e.printStackTrace();
			}
		}
		System.out.println("Loaded ontology From URL: " + owlOntology);
		System.out.println("    from: " + owlOntologyManager.getOntologyDocumentIRI(owlOntology));
		return owlOntology;
	}

	public OWLOntology getOwlOntologyByFile(String fileName) {
		if (fileName != null || !fileName.isEmpty()) {
			try {
				File file = new File("inDir/" + fileName + ".owl");
				if (owlOntology == null) {
					owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(file);
				}
			} catch (OWLOntologyCreationException e) {
				System.out.println("Failed to create OWLOntology");
				e.printStackTrace();
			}
		}
		printLocationOfOwlFile(owlOntologyManager.getOntologyDocumentIRI(owlOntology));
		return owlOntology;
	}

	public static OWLOntologyManager getOwlOntologyManager() {
		return owlOntologyManager;
	}

	private void printLocationOfOwlFile(IRI ontologyDocumentIRI) {
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("\t\tloading the owl file from location: " + ontologyDocumentIRI + "\t\t");
		System.out.println(
				"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

	}

	public static final class Floor {
		public static final String CARPET = "Carpet (1)";
		public static final String PLYWOOD_SHEATHING = "Plywood, Sheathing";
		public static final String STRUCTURE_WOODJOISTRAFTERLAYER = "Structure, Wood Joist/Rafter Layer";

	}

	public static final class Wall {
		public static final String GYPSUM_WALL_BOARD = "Gypsum Wall Board";
		public static final String BATTIN_SULATION = "Batt Insulation";
		public static final String BLANKET_INSULATION = "Blanket Insulation";
		public static final String INSULATION = "Insulation";
		public static final String STUD = "Stud";
		public static final String PLYWOOD_SHEATHING = "Plywood Sheathing";

	}

	public static final class Constant {
		public static final String equivalentClasses = "EquivalentClasses(";
		public static final String objectUnionOf = " ObjectUnionOf(";
		public static final String openParenthesis = "(";
		public static final String closeParenthesis = ")";
		public static final String spaceCloseParenthesis = " )";

		public static final String space = " ";
		public static final String subClassOf = "SubClassOf(";
	}
}
