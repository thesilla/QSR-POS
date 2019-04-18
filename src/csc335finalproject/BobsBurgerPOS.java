/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc335finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * TIP: declare all app component objects globally, INIT locally
 *
 * App builds/wraps access functions around the respective show(), hide()
 * methods of each respective Window(Stage)
 *
 * each method builds the stage/scene of corresponding component and shows
 *
 *
 * @author Max Gillman
 */
public class BobsBurgerPOS extends Application {

    //DB info:
    //username: max
    //password: password
    // global DB connection
    Label logo;

    Stage currentStage; // always set this to current stage being displayed.

    Connection conn;

    // ----GLOBAL (REUSABLE) BUTTONS----
    Button logoutButton = new Button("LOG OUT"); //global logout button for reuse, add handler in constructor

    Button exitButton = new Button("QUIT"); //global exit button for reuse, add handler in constructor

    Button loginButton = new Button("LOG IN"); //global login button for reuse, add handler in constructor

    Button homeButton = new Button("HOME"); //global login button for reuse, add handler in constructor

    // ----Login window----
    Stage loginStage;
    VBox loginVBox;
    VBox loginInnerVBox;

    Label loginLabel;
    TextField username;
    PasswordField password;
    boolean loggedIn = false;
    String loggedInUsername = "";
    String loggedInTitle = "";
    boolean admin = false;

    //-----logged in banner-----
    Label mainPOSUsername = new Label("");
    Label mainPOSTitle = new Label("");
    Label mainPOSAdmin = new Label("");
    HBox userInfo = new HBox();

    // ----Main POS Window window----
    Stage menuStage;

    VBox mainPOSVBox;

    // ----Manger Reporting window----
    Stage reportingStage;
    HBox reportingHBox1;
    VBox mainReportsVBox;

    // ----POS window----
    Stage POSStage;
    HBox POSHBox1;
    VBox POSVBox;

    //constructor
    public BobsBurgerPOS() {

        // add handlers upon initialization
        // attach logout handler
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // takes current parent stage as argument
                // need to find whatever 'current' stage is
                logout();

            }
        });

        // attach exit handler
        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // end process
                System.exit(0);

            }
        });

        // attach login handler
        loginButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                login(username.getText(), password.getText());

            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                menuStage.show();
                //urrentStage.hide();
                
            }
        });

    }

    @Override
    public void start(Stage primaryStage) {

        // app starts logged out
        // must be stated outside of login function otherwise login screen would always imply logout action
        logo = new Label("BOB'S BURGER");
        loginLabel = new Label("LOGIN");

        username = new TextField("");
        username.setPrefWidth(10);
        username.setAlignment(Pos.CENTER);

        password = new PasswordField();
        password.setPrefWidth(10);
        password.setAlignment(Pos.CENTER);

        // show login screen when app starts
        showLogin();

    }

    public void showLogin() {

        // show home button only if logged in
        if (loggedIn) {

            loginInnerVBox = new VBox(homeButton, logo, loginLabel, username, password, loginButton, exitButton);

        } else {

            loginInnerVBox = new VBox(logo, loginLabel, username, password, loginButton, exitButton);

        }

        loginInnerVBox.setPrefWidth(50);
        loginInnerVBox.setAlignment(Pos.CENTER);
        loginInnerVBox.setSpacing(10);

        loginVBox = new VBox(loginInnerVBox);
        loginVBox.setPrefWidth(50);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        Scene scene = new Scene(loginVBox, 600, 250);

        loginStage = new Stage();

        loginStage.setTitle("Bob's Burger POS - Login");
        loginStage.setScene(scene);
        loginStage.show();
        currentStage = loginStage; //set current stage to login stage

    }

    // displays the main POS menu screen
    public void showMainPOS() {

 
        Button openPOS = new Button("POS");
        Button openReporting = new Button("Managment Reporting");
        
        Label mainPOSlabel = new Label("Main POS Screen");

        mainPOSVBox = new VBox(showLoginBanner(), mainPOSlabel, openPOS, openReporting, logoutButton);
        //mainPOSVBox.setAlignment(Pos.CENTER);
        mainPOSVBox.setSpacing(20);

        Scene scene = new Scene(mainPOSVBox, 600, 250);

        menuStage = new Stage();

        menuStage.setTitle("Bob's Burger POS");
        menuStage.setScene(scene);

        //attach event handler to openReporting button here because button only used once
        openReporting.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentStage.hide();
                showManagerReporting();

            }
        });

        //attach event handler to openPOS button here because button only used once
        openPOS.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                currentStage.hide();
                showPOS();

            }
        });

        menuStage.show();

        currentStage = menuStage; // set current stage to Menu Stage

    }

    // sets userInfo HBOX returns HBOX container init of login banner
    public HBox showLoginBanner() {

        //if (loggedIn) {

            mainPOSUsername = new Label("Welcome, " + loggedInUsername);
            mainPOSTitle = new Label("Role: " + loggedInTitle);

            if (admin) {

                mainPOSAdmin = new Label("Admin Access: YES");

            } else {

                mainPOSAdmin = new Label("Admin Access: NO");

            }

            userInfo = new HBox(homeButton, mainPOSUsername, mainPOSTitle, mainPOSAdmin);
            userInfo.setSpacing(20);
            userInfo.setAlignment(Pos.CENTER);

            return userInfo;

       // } else {

        //    return userInfo = new HBox();

       // }

    }

    public void showPOS() {

        POSVBox = new VBox(showLoginBanner());

        Scene scene = new Scene(POSVBox, 600, 250);

        POSStage = new Stage();

        POSStage.setTitle("ORDER ENTRY");
        POSStage.setScene(scene);

        POSStage.show();

        currentStage = POSStage;

    }

    // displays manager reporting window
    public void showManagerReporting() {

        //if admin (manager) allow access to this window
        if (admin) {

            Label l = new Label("Manager Reporting");

            reportingHBox1 = new HBox(l);
            mainReportsVBox = new VBox(showLoginBanner(),reportingHBox1);
            reportingStage = new Stage();

            //scene can be temp and stored locally
            Scene scene = new Scene(mainReportsVBox, 600, 250);
            reportingStage.setScene(scene);
            reportingStage.show();
            currentStage = reportingStage;

            // else prompt user back to login screen
        } else {

            showLogin();

        }

    }

    // takes username and password as strings
    // logs user in if valid credentials entered
    public void login(String username, String password) {

        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        try {

            // use prepared statements to execute and avoid outside tampering
            String sqlStatement = "SELECT title, count(username) as numrecords FROM employees where username = ? and password = ? group by title";

            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, username);
            ps.setString(2, password);

            result = ps.executeQuery();

            //works
            while (result.next()) {

                if (Integer.parseInt(result.getString("numrecords")) == 1) {

                    loggedIn = true;
                    loggedInUsername = username;
                    loggedInTitle = result.getString("title");

                    if (loggedInTitle.equalsIgnoreCase("Manager")) {

                        admin = true;
                        System.out.println("you are an admin");
                    } else {

                        admin = false;
                    }

                    System.out.println("Welcome, " + loggedInUsername);
                    System.out.println("Title: " + loggedInTitle);

                    //show main POS window
                    showMainPOS();

                    //hide login screen
                    loginStage.hide();

                } else {

                    JOptionPane.showMessageDialog(null, "Error: Username and Password combination incorrect. Please try again", "ACCESS DENIED", JOptionPane.OK_OPTION);

                }

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

            // after execution, close out of everything
        } finally {

            // close results set
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {

                }
            }
            // close prepared statement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

            // close db connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }

        }

    }

    //log user out
    // takes parent stage object as argument
    // removes whatever previous stage was and shows loginStage this instead
    public void logout() {

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // reset logged in status trackers
                loggedIn = false;
                loggedInUsername = "";
                loggedInTitle = "";
                admin = false;

                //hide previous currentStage screen saved in this local stage variable
                currentStage.hide();

                //show login again
                loginStage.show();

            }
        });

    }

    public void connect() {

        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/bobsburger", "max", "password");
            //JOptionPane.showMessageDialog(null, "connection success!", "Connection", JOptionPane.OK_OPTION);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "ERROR: " + e, "Connection Failed:", JOptionPane.OK_OPTION);

        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}

/*

        try {
                    //"org.apache.derby.jdbc.EmbeddedDriver"
                    //"org.apache.derby.jdbc.ClientDriver"
                    Class.forName("org.apache.derby.jdbc.ClientDriver");
                    conn = DriverManager.getConnection("jdbc:derby://localhost:1527/bobsburger", "max", "password");
                    JOptionPane.showMessageDialog(null, "connection success!", "Connection", JOptionPane.OK_OPTION);

                    
                    String sqlStatement = "SELECT * FROM combos";
                    Statement stmt = conn.createStatement();
                    ResultSet result = stmt.executeQuery(sqlStatement);
                    
                    
                    //works
                    
                    while (result.next()) {
                        System.out.println(result.getString(1));
                        System.out.println(result.getString(2));
                        System.out.println(result.getString(3));

                    }
                 

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

                }*/
