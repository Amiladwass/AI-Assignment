package MathOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;

public class OpenOWL {
    private static final String OWL_FILE = "MathOntology_1.owl";
    private static final String BASE_URI = "http://www.owl-ontologies.com/Ontology1364995044.owl#";

    // Load OWL file and return OntModel
    public static OntModel loadOntology() {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream in = FileManager.get().open(OWL_FILE);
        if (in == null) {
            throw new IllegalArgumentException("OWL file not found: " + OWL_FILE);
        }
        model.read(in, BASE_URI);
        return model;
    }

    // Execute SPARQL query
    public static ResultSet executeSparql(String queryString) {
        OntModel model = loadOntology();
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execSelect();
    }
}
