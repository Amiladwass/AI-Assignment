package MathOntology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.*;

public class MainFrame1 extends javax.swing.JFrame {

    private JComboBox<String> topicComboBox;
    private JButton fetchButton, loginButton, registerButton, logoutButton;
    private JTextPane resultPane;
    private JScrollPane scrollPane;
    private JPanel loginPanel, registerPanel, mainPanel;
    private JTextField usernameField, registerUsernameField;
    private JPasswordField passwordField, registerPasswordField;
    private CardLayout cardLayout;
    private JPanel containerPanel;

   private static final String OWL_FILE = "MathOntology_1.owl";
   private static final String BASE_URI = "http://www.owl-ontologies.com/Ontology1364995044.owl#";
   private static final String USER_FILE = "users.txt";
   public MainFrame1() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Math Intelligent Tutoring System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
    }

    private void initComponents() {
        // CardLayout for switching between panels
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Initialize Panels
        loginPanel = createLoginPanel();
        mainPanel = createMainPanel();
        registerPanel = createRegisterPanel();

        // Add Panels to CardLayout
        containerPanel.add(loginPanel, "LoginPanel");
        containerPanel.add(registerPanel, "RegisterPanel");
        containerPanel.add(mainPanel, "MainPanel");

        this.add(containerPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(220, 240, 250));

        JLabel titleLabel = new JLabel("Login to Math Intelligent Tutoring System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(100, 50, 400, 30);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(150, 150, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(250, 150, 200, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(150, 200, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(250, 200, 200, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(250, 250, 100, 30);
        loginButton.setBackground(new Color(100, 150, 200));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(370, 250, 100, 30);
        registerButton.setBackground(new Color(150, 100, 200));
        registerButton.setForeground(Color.WHITE);
        panel.add(registerButton);

        loginButton.addActionListener(e -> validateLogin());
        registerButton.addActionListener(e -> cardLayout.show(containerPanel, "RegisterPanel"));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 240, 250));

        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(200, 50, 400, 30);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(150, 150, 100, 25);
        panel.add(userLabel);

        registerUsernameField = new JTextField();
        registerUsernameField.setBounds(250, 150, 200, 25);
        panel.add(registerUsernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(150, 200, 100, 25);
        panel.add(passLabel);

        registerPasswordField = new JPasswordField();
        registerPasswordField.setBounds(250, 200, 200, 25);
        panel.add(registerPasswordField);

        JButton createButton = new JButton("Create Account");
        createButton.setBounds(250, 250, 150, 30);
        createButton.setBackground(new Color(50, 150, 100));
        createButton.setForeground(Color.WHITE);
        panel.add(createButton);

        createButton.addActionListener(e -> registerUser());

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Math Intelligent Tutoring System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(150, 20, 400, 30);
        panel.add(titleLabel);

        JLabel topicLabel = new JLabel("Select Math Topic:");
        topicLabel.setBounds(100, 80, 150, 25);
        panel.add(topicLabel);

        topicComboBox = new JComboBox<>(new String[]{"TriangleArea", "SquareArea", "CircleArea"});
        topicComboBox.setBounds(250, 80, 200, 25);
        panel.add(topicComboBox);

        fetchButton = new JButton("Fetch Example");
        fetchButton.setBounds(250, 130, 150, 30);
        fetchButton.setBackground(new Color(100, 180, 240));
        fetchButton.setForeground(Color.WHITE);
        fetchButton.addActionListener(evt -> fetchExamples());
        panel.add(fetchButton);

        resultPane = new JTextPane();
        resultPane.setEditable(false);
        scrollPane = new JScrollPane(resultPane);
        scrollPane.setBounds(100, 200, 400, 200);
        panel.add(scrollPane);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(450, 20, 100, 30);
        logoutButton.setBackground(new Color(200, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> cardLayout.show(containerPanel, "LoginPanel"));
        panel.add(logoutButton);

        return panel;
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Scanner scanner = new Scanner(new File(USER_FILE))) {
            while (scanner.hasNextLine()) {
                String[] user = scanner.nextLine().split(",");
                if (user[0].equals(username) && user[1].equals(password)) {
                    cardLayout.show(containerPanel, "MainPanel");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String username = registerUsernameField.getText();
        String password = new String(registerPasswordField.getPassword());

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(username + "," + password + "\n");
            JOptionPane.showMessageDialog(this, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(containerPanel, "LoginPanel");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   private void fetchExamples() {
      String selectedTopic = topicComboBox.getSelectedItem().toString();
      String query = "PREFIX math:<" + BASE_URI + "> "
              + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
              + "SELECT ?conceptLabel ?exampleLabel ?answer WHERE { "
              + "math:" + selectedTopic + " math:hasConcept ?concept . "
              + "?concept rdfs:label ?conceptLabel . "
              + "OPTIONAL { ?concept math:hasExample ?example . ?example rdfs:label ?exampleLabel ; math:hasAnswer ?answer } }";

      try {
         ResultSet results = executeSparql(query);
         displayResults(results, selectedTopic);
      } catch (Exception e) {
         appendToPane("Error fetching data.\n", Color.RED);
         e.printStackTrace();
      }
   }

   private ResultSet executeSparql(String queryString) {
      OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
      InputStream in = FileManager.get().open(OWL_FILE);
      if (in == null) {
         throw new IllegalArgumentException("OWL file not found: " + OWL_FILE);
      }
      model.read(in, BASE_URI);
      Query query = QueryFactory.create(queryString);
      QueryExecution qe = QueryExecutionFactory.create(query, model);
      return qe.execSelect();
   }

   private void displayResults(ResultSet results, String topic) {
      resultPane.setText("");
      if (!results.hasNext()) {
         appendToPane("No examples available for " + topic + "\n", Color.RED);
         return;
      }
      appendToPane("Concepts and Examples for " + topic + ":\n\n", Color.BLACK);
      while (results.hasNext()) {
         QuerySolution sol = results.nextSolution();
         String conceptLabel = sol.get("conceptLabel").toString();
         String exampleLabel = sol.contains("exampleLabel") ? sol.get("exampleLabel").toString() : "No example available";
         String answer = sol.contains("answer") ? sol.get("answer").toString() : "Answer not provided";
         appendToPane("Concept: " + conceptLabel + "\n", Color.BLUE);
         appendToPane("Example: " + exampleLabel + "\n", Color.DARK_GRAY);
         appendToPane("Answer: " + answer + "\n\n", Color.MAGENTA);
      }
   }

   private void appendToPane(String msg, Color c) {
      StyledDocument doc = resultPane.getStyledDocument();
      SimpleAttributeSet attributeSet = new SimpleAttributeSet();
      StyleConstants.setForeground(attributeSet, c);
      try {
         doc.insertString(doc.getLength(), msg, attributeSet);
      } catch (BadLocationException e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(() -> new MainFrame1().setVisible(true));
   }
}
