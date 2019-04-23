/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc335finalproject;

import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.swing.JComboBox;
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
    String date = "" + java.time.LocalDate.now();
    Label dateLabel = new Label(date);

    Label logo = new Label("BOB'S BURGER");

    Stage rootStage;

    //Stage currentStage; // always set this to current stage being displayed.
    Connection conn;

    // ----GLOBAL (REUSABLE) BUTTONS----
    Button logoutButton = new Button("LOG OUT"); //global logout button for reuse, add handler in constructor

    Button exitButton = new Button("QUIT"); //global exit button for reuse, add handler in constructor

    Button loginButton = new Button("LOG IN"); //global login button for reuse, add handler in constructor

    Button homeButton = new Button("HOME"); //global login button for reuse, add handler in constructor

    Button openPOS = new Button("POS"); //global openPOS button for reuse, add handler in constructor

    Button openReporting = new Button("Managment Reporting"); //global openReporting button for reuse, add handler in constructor

    // navbar
    HBox navbar = new HBox();

    // ----Login window----
    //Stage loginStage;
    VBox loginVBox;
    VBox loginInnerVBox;
    Scene loginScene;

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
    VBox topBanner = new VBox();

    // ----Main POS Window window----
    //Stage menuStage;
    Scene mainPOSScene;

    VBox mainPOSVBox;

    // ----Manger Reporting window----
    //Stage reportingStage;
    HBox reportingHBox1;
    VBox mainReportsVBox;
    Scene reportingScene;

    TableView<Transaction> allOrdersTable;
    TableView<Order> orderDrilldownTable;
    TableView<Count> countsTable;
    DatePicker dp;
    HBox reportingControls;
    VBox countsReportVBox;

    // ----POS window----
    //Stage POSStage;
    HBox POSHBox1;
    VBox POSVBox;

    HBox rootPOSHBox;
    Scene POSScene;

    // ----Burger Menu (ListView)----
    ListView burgerMenu;
    // Create the Lists for the ListViews
    //ObservableList<String> burgerList;
    Button addBurgerButton = new Button("Add To Cart");

    // ----Fries Menu (ListView)----
    ListView friesMenu;
    // Create the Lists for the ListViews
    //ObservableList<String> burgerList;
    Button addFriesButton = new Button("Add To Cart");

    // ----Drinks Menu (ListView)----
    ListView drinksMenu;
    Button addDrinksButton = new Button("Add To Cart");

    // ----Combo Menu (ListView)----
    ListView comboMenu;
    Button addOptionsButton = new Button("Choose Options");
    Button addComboButton = new Button("Add To Cart");

    //----Combo Selection Window (Stage)-----
    Stage comboSelectionWindow = new Stage();
    Scene comboSelectionScene;
    Button comboSelectionButton = new Button("Submit Combo");
    Label comboSelectionLabel = new Label("");
    ComboBox<String> comboFriesComboBox = new ComboBox<String>();
    ComboBox<String> comboDrinksComboBox = new ComboBox<String>();
    VBox combosVBox;

    // for removing item from cart
    Button removeItemButton = new Button("Remove Item");

    int drinkID = -1;
    int friesID = -1;

    // ----Transaction Component (ListView)----
    VBox transDetails;
    ListView transListView = new ListView();
    Label subTotal = new Label("$0.00");
    float subtotalAmount = 0;

    Button completeTransactionButton = new Button("PLACE ORDER");

    String returnString = "";

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

                showMainPOS();
            }
        });

        //attach showManagerReporting handler
        openReporting.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                showManagerReporting();

            }
        });

        //attach showPOS handler 
        openPOS.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                showPOS();

            }
        });

        //attach addBurgerButton handler 
        addBurgerButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                // change to actual functionality
                System.out.println(burgerMenu.getSelectionModel().getSelectedItem());
                transListView.getItems().add(burgerMenu.getSelectionModel().getSelectedItem());
                showTotal();

            }
        });

        //attach addFriesButton handler 
        addFriesButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //System.out.println(friesMenu.getSelectionModel().getSelectedItem());
                transListView.getItems().add(friesMenu.getSelectionModel().getSelectedItem());
                showTotal();

            }
        });

        //attach addDrinksButton handler 
        addDrinksButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //System.out.println(drinksMenu.getSelectionModel().getSelectedItem());
                transListView.getItems().add(drinksMenu.getSelectionModel().getSelectedItem());
                showTotal();

            }
        });

        //attach completeTransactionButton handler 
        completeTransactionButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                completeTransaction();

            }
        });

        //attach addOptionsButton handler 
        addOptionsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                String combo = comboMenu.getSelectionModel().getSelectedItem().toString().substring(0, 10).replaceAll("[^0-9]", "");
                showComboSelection(combo);

            }
        });

        //attach removeItem handler 
        removeItemButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                transListView.getItems().remove(transListView.getSelectionModel().getSelectedIndex());
                showTotal();
            }
        });

    }

    @Override
    public void start(Stage primaryStage) {

        rootStage = primaryStage;
        // app starts logged out
        // must be stated outside of login function otherwise login screen would always imply logout action
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
        loginInnerVBox = new VBox(logo, loginLabel, username, password, loginButton, exitButton);

        loginInnerVBox.setPrefWidth(50);
        loginInnerVBox.setAlignment(Pos.CENTER);
        loginInnerVBox.setSpacing(10);

        loginVBox = new VBox(loginInnerVBox);
        loginVBox.setPrefWidth(50);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setSpacing(10);

        loginScene = new Scene(loginVBox, 600, 250);

        rootStage.setTitle("Bob's Burger POS - Login");
        rootStage.setScene(loginScene);
        rootStage.show();

    }

    // displays the main POS menu screen
    public void showMainPOS() {

        Label mainPOSlabel = new Label("Main POS Screen");

        mainPOSVBox = new VBox(showLoginBanner(), mainPOSlabel);
        //mainPOSVBox.setAlignment(Pos.CENTER);
        mainPOSVBox.setSpacing(20);

        mainPOSScene = new Scene(mainPOSVBox, 1400, 600);

        rootStage.setTitle("Bob's Burger POS");
        rootStage.setScene(mainPOSScene);

    }

    // sets userInfo HBOX returns HBOX container init of login banner
    public VBox showLoginBanner() {

        //if (loggedIn) {
        dateLabel.setText("Date: " + date);
        mainPOSUsername = new Label("Welcome, " + loggedInUsername);
        mainPOSTitle = new Label("Role: " + loggedInTitle);

        if (admin) {

            mainPOSAdmin = new Label("Admin Access: YES");

        } else {

            mainPOSAdmin = new Label("Admin Access: NO");

        }

        userInfo = new HBox(mainPOSUsername, mainPOSTitle, mainPOSAdmin, dateLabel);
        userInfo.setSpacing(20);
        userInfo.setAlignment(Pos.CENTER);

        navbar = new HBox(homeButton, openPOS, openReporting, logoutButton);
        navbar.setSpacing(20);
        navbar.setAlignment(Pos.CENTER);

        logo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        topBanner = new VBox(userInfo, logo, navbar);
        topBanner.setAlignment(Pos.CENTER);

        return topBanner;

        // } else {
        //    return userInfo = new HBox();
        // }
    }

    public void showPOS() {

        subtotalAmount = 0;
        subTotal.setText("$0.00");

        Label burgersl = new Label("Burgers");
        VBox hb_burgers = new VBox(burgersl, showBurgerMenu(), addBurgerButton);
        hb_burgers.setAlignment(Pos.CENTER);

        Label friesl = new Label("Fries");
        VBox hb_fries = new VBox(friesl, showFriesMenu(), addFriesButton);
        hb_fries.setAlignment(Pos.CENTER);

        Label drinksl = new Label("Drinks");
        VBox hb_drinks = new VBox(drinksl, showDrinksMenu(), addDrinksButton);
        hb_drinks.setAlignment(Pos.CENTER);

        Label combosl = new Label("Combos");
        VBox hb_combos = new VBox(combosl, showComboMenu(), addOptionsButton);
        hb_combos.setAlignment(Pos.CENTER);

        POSHBox1 = new HBox(hb_burgers, hb_fries, hb_drinks, hb_combos);
        //POSVBox = new VBox(hb_burgers, hb_fries, hb_drinks, hb_combos);
        POSHBox1.setAlignment(Pos.CENTER);
        POSHBox1.setSpacing(20);

        //rootPOSHBox = new HBox(POSVBox, showTransactionComponent());
        VBox rootPOSVBox = new VBox(showLoginBanner(), POSHBox1, showTransactionComponent());
        rootPOSVBox.setAlignment(Pos.CENTER);
        POSScene = new Scene(rootPOSVBox, 1400, 600);

        //POSStage = new Stage();
        rootStage.setTitle("ORDER ENTRY");
        rootStage.setScene(POSScene);

    }

    // displays manager reporting window
    public void showManagerReporting() {

        //if admin (manager) allow access to this window
        if (admin) {

            //datepicker
            dp = new DatePicker();
            
            Button orderDetailsButton = new Button("Order Details");
            
            //reporting controls, at top of app
            reportingControls = new HBox(dp,orderDetailsButton);
            reportingControls.setAlignment(Pos.CENTER);

            //all orders table and vbox
            Label listOrdersLabel = new Label("List Transactions");
            Label listOrdersTotalLabel = new Label("");
            VBox allOrdersVBox;
            allOrdersTable = new TableView();

            TableColumn<Transaction, Transaction> column1 = new TableColumn<>("Order #");
            column1.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

            TableColumn<Transaction, Transaction> column2 = new TableColumn<>("Date");
            column2.setCellValueFactory(new PropertyValueFactory<>("date"));

            TableColumn<Transaction, Transaction> column3 = new TableColumn<>("Taker");
            column3.setCellValueFactory(new PropertyValueFactory<>("taker"));

            TableColumn<Transaction, Transaction> column4 = new TableColumn<>("Total Paid");
            column4.setCellValueFactory(new PropertyValueFactory<>("total"));

            allOrdersTable.getColumns().add(column1);
            allOrdersTable.getColumns().add(column2);
            allOrdersTable.getColumns().add(column3);
            allOrdersTable.getColumns().add(column4);
            
            allOrdersVBox = new VBox(listOrdersLabel,allOrdersTable,listOrdersTotalLabel);
            
            
            // drilldown of individual orders
            Label drilldownLabel = new Label("Order Drilldown");
            VBox drilldownVBox;
            Label orderTotal = new Label("");
            
            
            orderDrilldownTable = new TableView();
            
            TableColumn<Order, Order> c1 = new TableColumn<>("Order #");
            c1.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));

            TableColumn<Order, Order> c2 = new TableColumn<>("Item ID");
            c2.setCellValueFactory(new PropertyValueFactory<>("menuID"));

            TableColumn<Order, Order> c3 = new TableColumn<>("Item Desc");
            c3.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));

            TableColumn<Order, Order> c4 = new TableColumn<>("Item Size");
            c4.setCellValueFactory(new PropertyValueFactory<>("itemSize"));
            
            TableColumn<Order, Order> c5 = new TableColumn<>("Unit Price");
            c5.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
            
            TableColumn<Order, Order> c6 = new TableColumn<>("Part of Combo?");
            c6.setCellValueFactory(new PropertyValueFactory<>("combo"));

            orderDrilldownTable.getColumns().add(c1);
            orderDrilldownTable.getColumns().add(c2);
            orderDrilldownTable.getColumns().add(c3);
            orderDrilldownTable.getColumns().add(c4);
            orderDrilldownTable.getColumns().add(c5);
            orderDrilldownTable.getColumns().add(c6);
            
            drilldownVBox = new VBox(drilldownLabel,orderDrilldownTable,orderTotal);
           
            
            //item counts report table
            countsTable = new TableView();
            TableColumn<Count, Count> co1 = new TableColumn<>("Item ID");
            co1.setCellValueFactory(new PropertyValueFactory<>("itemID"));

            TableColumn<Count, Count> co2 = new TableColumn<>("Item Desc");
            co2.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));

            TableColumn<Count, Count> co3 = new TableColumn<>("Item Size");
            co3.setCellValueFactory(new PropertyValueFactory<>("itemSize"));

            TableColumn<Count, Count> co4 = new TableColumn<>("Total Sold");
            co4.setCellValueFactory(new PropertyValueFactory<>("count"));


            countsTable.getColumns().add(co1);
            countsTable.getColumns().add(co2);
            countsTable.getColumns().add(co3);
            countsTable.getColumns().add(co4);
            
            Label countsReportLabel = new Label("Product Quantity Sold");
            countsReportVBox = new VBox(countsReportLabel,countsTable);
            

            //Label l = new Label("Manager Reporting");

            // all report containers shown horizontally
            reportingHBox1 = new HBox(allOrdersVBox, drilldownVBox,countsReportVBox);
            
            reportingHBox1.setAlignment(Pos.CENTER);
            reportingHBox1.setSpacing(20);

            
            
            mainReportsVBox = new VBox(showLoginBanner(), reportingControls, reportingHBox1);
            mainReportsVBox.setSpacing(20);

            //scene can be temp and stored locally
            reportingScene = new Scene(mainReportsVBox, 1400, 600);
            rootStage.setTitle("MANAGER REPORTING");
            rootStage.setScene(reportingScene);
            
            // run counts report automatically
            runCountsReport();

            //report will run when any new date is picked
            dp.setOnAction((new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    // clear current table first
                    allOrdersTable.getItems().clear();
                    
                    String date = dp.getValue().toString();
                    runTransactionReport(date);
                    

                }
            }));
            
            // delete when done
            orderDetailsButton.setOnAction((new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    // clear current table first
                    orderDrilldownTable.getItems().clear();
                    
                    String order_number = allOrdersTable.getSelectionModel().getSelectedItem().orderNumber;
                    
                    orderTotal.setText(allOrdersTable.getSelectionModel().getSelectedItem().total);
  
                    runOrderDrilldownReport(order_number);

                }
            }));
       

        } else {

            showLogin();

        }

    }
    
    
    public void runCountsReport(){
        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        try {

    
            // use prepared statements to execute and avoid outside tampering
            String sqlStatement = "select t.menuid, m.item_desc, m.item_size, count(t.menuid) as total  from transactions t inner join menu m on m.id = t.menuid group by t.menuid, m.item_desc, m.item_size";

            ps = conn.prepareStatement(sqlStatement);
            

            result = ps.executeQuery();
            

            //works
            while (result.next()) {
                
                //create order object
                Count count = new Count(result.getString("menuid"),result.getString("item_desc"),result.getString("item_size"),result.getString("total"));
                
      
                countsTable.getItems().add(count);

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
    
    
    public void runOrderDrilldownReport(String order_number){
        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        try {

    
            // use prepared statements to execute and avoid outside tampering
            String sqlStatement = "select t.transactionid, t.menuid,mxc.item_desc, mxc.item_size, mxc.item_price, t.combo from TRANSACTIONS t left join (select * from menu union select c.comboid as id, c.combo_group as item_type, c.combo_group || ': ' || c.entree_desc || ' / ' || c.fries_desc|| ' / ' || c.drink_desc as item_desc, entree_size as item_size, c.combo_price as item_price from combos c) as mxc on t.menuid = mxc.id where transactionid = ?";

            
            
            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, order_number);


            result = ps.executeQuery();
            

            //works
            while (result.next()) {
                
                //create order object
                Order order = new Order(result.getString("transactionid"),result.getString("menuid"),result.getString("item_desc"),result.getString("item_size"),result.getString("item_price"),result.getString("combo"));
                
                //if part of combo, unit price should be 0
                if(result.getString("combo").equalsIgnoreCase("YES")){
                    
                    order.setItemPrice("0.00");
                }

                orderDrilldownTable.getItems().add(order);

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
    

    //takes a string argument and uses it to pull transactions by date
    // TODO - MOST CODE IN HERE NOT VALID, DELETE
    public void runTransactionReport(String date) {

        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        try {

            //tableView.getItems().add("");
            //tableView.getItems().add("");
            // use prepared statements to execute and avoid outside tampering
            String sqlStatement = "select distinct t.transactionid as order_number, t.date, t.taker, t.total as order_total from TRANSACTIONS t left join (select * from menu union select c.comboid as id, c.combo_group as item_type, c.combo_group || ': ' || c.entree_desc || ' / ' || c.fries_desc|| ' / ' || c.drink_desc as item_desc, entree_size as item_size, c.combo_price as item_price from combos c) as mxc on t.menuid = mxc.id where t.date = ?";

            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, date);


            result = ps.executeQuery();

            //works
            while (result.next()) {

                allOrdersTable.getItems().add(new Transaction(result.getString("order_number"),result.getString("date"),result.getString("taker"),result.getString("order_total")));

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

                // show login screen
                showLogin();

            }
        });

    }

    public void connect() {

        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/bobsburger", "max", "password");//, "max", "password"); dont need this created without username password
            //conn = DriverManager.getConnection("jdbc:derby://localhost:1527/bb");
            //JOptionPane.showMessageDialog(null, "connection success!", "Connection", JOptionPane.OK_OPTION);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "ERROR: " + e, "Connection Failed:", JOptionPane.OK_OPTION);

        }

    }

    public ListView showBurgerMenu() {

        try {
            connect();

            burgerMenu = new ListView();
            //burgerMenu.getItems().add("<<None Selected>>");

            //burgerList = FXCollections.<String>observableArrayList();
            String sqlStatement = "SELECT * FROM menu where item_type = 'Burger'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            //works
            while (result.next()) {

                //burgerList.add(result.getString(1));
                //format line
                String line = String.format("%-20s%-20s%-20s%-20s\n", result.getString("id"), result.getString("item_desc"), result.getString("item_size"), result.getString("item_price"));
                burgerMenu.getItems().add(line);

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

        }

        burgerMenu.setPrefHeight(250);
        burgerMenu.setPrefWidth(350);
        return burgerMenu;

    }

    public ListView showFriesMenu() {

        try {
            connect();

            friesMenu = new ListView();
            //friesMenu.getItems().add("<<None Selected>>");

            //burgerList = FXCollections.<String>observableArrayList();
            String sqlStatement = "SELECT * FROM menu where item_type = 'Fries'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            //works
            while (result.next()) {

                //burgerList.add(result.getString(1));
                //format line
                String line = String.format("%-20s%-20s%-20s%-20s\n", result.getString("id"), result.getString("item_desc"), result.getString("item_size"), result.getString("item_price"));
                friesMenu.getItems().add(line);

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

        }

        friesMenu.setPrefHeight(250);
        friesMenu.setPrefWidth(350);
        return friesMenu;

    }

    public ListView showDrinksMenu() {

        try {
            connect();

            drinksMenu = new ListView();
            //drinksMenu.getItems().add("<<None Selected>>");

            //burgerList = FXCollections.<String>observableArrayList();
            String sqlStatement = "SELECT * FROM menu where item_type = 'Drinks'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            //works
            while (result.next()) {

                //burgerList.add(result.getString(1));
                //format line
                String line = String.format("%-20s%-20s%-20s%-20s\n", result.getString("id"), result.getString("item_desc"), result.getString("item_size"), result.getString("item_price"));
                drinksMenu.getItems().add(line);

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

        }

        drinksMenu.setPrefWidth(350);
        drinksMenu.setPrefHeight(250);
        return drinksMenu;

    }

    public ListView showComboMenu() {

        comboMenu = new ListView();
        //comboMenu.getItems().add("<<None Selected>>");
        comboMenu.getItems().add("Combo 1: Hamburger Combo - Large\t 8.99");
        comboMenu.getItems().add("Combo 2: Cheeseburger Combo - Large\t 9.99");
        comboMenu.getItems().add("Combo 3: Hamburger Combo - Small\t 6.99");
        comboMenu.getItems().add("Combo 4: Veggie Burger Combo - Large\t 8.99");

        comboMenu.setPrefWidth(350);
        comboMenu.setPrefHeight(250);
        return comboMenu;

    }

    public VBox showTransactionComponent() {

        transListView = new ListView();

        transListView.setPrefHeight(250);
        transDetails = new VBox(transListView, subTotal, completeTransactionButton, removeItemButton);
        return transDetails;

    }

    public String showTotal() {

        subtotalAmount = 0;

        transListView.getItems().forEach((item) -> {

            double item_price = Double.parseDouble(item.toString().substring(50).replaceAll("[^0-9.]", ""));
            subtotalAmount += item_price;
            returnString = String.format("%.02f", subtotalAmount);
            subTotal.setText(String.format("%.02f", subtotalAmount));

        });
        return returnString;
    }

    // creates transaction record in transactions table of database
    public void completeTransaction() {

        Random rand = new Random();

        //temp transaction ID (FIXME MAKE RANDOM), and check db to see trans ID not taken
        int transactionID = rand.nextInt(50);

        //temp customer ID (FIXME MAKE RECORDS)
        int customerID = 1;

        transListView.getItems().forEach((item) -> {

            String combo = "NO";
            PreparedStatement ps = null;
            ResultSet result = null;
            // connect to DB
            connect();

            int id = Integer.parseInt(item.toString().substring(0, 4).replaceAll("[^0-9]", ""));

            // if price at end is 0.00 its a combo item
            String price = item.toString().substring(15).replaceAll("[^0-9.]", "");

            try {

                // use prepared statements to execute and avoid outside tampering
                String sqlStatement = "INSERT INTO transactions values (?,?,?,?,?,?,?)";

                ps = conn.prepareStatement(sqlStatement);
                ps.setString(1, Integer.toString(transactionID));
                ps.setString(2, Integer.toString(id));
                ps.setString(3, Integer.toString(customerID));
                ps.setString(4, "" + java.time.LocalDate.now());
                ps.setString(5, loggedInUsername);

                // if price at end is 0.00 its a combo item
                if (price.equalsIgnoreCase("0.00")) {

                    combo = "YES";

                } else {

                    combo = "NO";
                }

                ps.setString(6, combo);

                ps.setString(7, showTotal());
                ps.executeUpdate();
                //showTotal();

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
        });
        JOptionPane.showMessageDialog(null, "Your Order Number is: " + transactionID + ";\nOrder Total: " + subtotalAmount, "Transaction Complete!", JOptionPane.OK_OPTION);
        showPOS();

    }

    //takes a combo group, and returns corresponding menu options to UI
    public void showComboSelection(String combo) {

        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        comboFriesComboBox = new ComboBox<String>();
        ObservableList<String> friesArray = FXCollections.observableArrayList();

        comboDrinksComboBox = new ComboBox<String>();
        ObservableList<String> drinksArray = FXCollections.observableArrayList();
        //Button comboSelectionButton = new Button("Submit Combo");

        // add "Combo" to beginning of string for query
        String newCombo = "Combo" + combo;

        try {
            //comboSelectionLabel.setText(result.getString("entree_desc"));
// populate fries combobox      
            // use prepared statements to execute and avoid outside tampering
            String sqlStatement = "select distinct fries_id, fries_desc, fries_size from combos where combo_group = ?";

            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, newCombo);
            result = ps.executeQuery();

            while (result.next()) {

                friesArray.add(result.getString("fries_id") + " - " + result.getString("fries_desc") + " " + result.getString("fries_size"));

            }

            // populate drinks combobox      
            // use prepared statements to execute and avoid outside tampering
            sqlStatement = "select distinct drink_id, drink_desc, drink_size from combos where combo_group = ?";

            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, newCombo);
            result = ps.executeQuery();

            while (result.next()) {

                drinksArray.add(result.getString("drink_id") + " - " + result.getString("drink_desc") + " " + result.getString("drink_size"));

            }

            // set add combo button handler here since its actions are dynamic
            addComboButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                // TODO
                    //friesID = Integer.parseInt(result.getString("fries_id"));
                    friesID = Integer.parseInt(comboFriesComboBox.getSelectionModel().getSelectedItem().toString().substring(0, 4).replaceAll("[^0-9]", ""));
                    drinkID = Integer.parseInt(comboDrinksComboBox.getSelectionModel().getSelectedItem().toString().substring(0, 4).replaceAll("[^0-9]", ""));

                    //drinkID = Integer.parseInt(result.getString("drink_id"));
                    addComboToTransaction(newCombo, friesID, drinkID);

                }
            });

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

        }

        comboFriesComboBox.setItems(friesArray);
        comboDrinksComboBox.setItems(drinksArray);

        combosVBox = new VBox(comboSelectionLabel, comboFriesComboBox, comboDrinksComboBox, addComboButton);

        comboSelectionScene = new Scene(combosVBox, 600, 600);

        comboSelectionWindow.setScene(comboSelectionScene);
        comboSelectionWindow.show();

    }

    // takes parameters, returns exact combo product id line
    public void addComboToTransaction(String combo_group, int fries_id, int drink_id) {

        PreparedStatement ps = null;
        ResultSet result = null;
        // connect to DB
        connect();

        try {

            String sqlStatement = "select * from combos where combo_group = ? and fries_id = ? and drink_id = ?";

            ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, combo_group);
            ps.setString(2, Integer.toString(fries_id));
            ps.setString(3, Integer.toString(drink_id));
            result = ps.executeQuery();

            // add combo item at combo cost
            // add individual lines that make up combo at zero cost FOR TRACKING PURPOSES
            while (result.next()) {

                // add item to transactions listview
                transListView.getItems().add(String.format("%-20s%-20s%-20s%-20s\n", result.getString("comboid"), result.getString("combo_group") + " - " + result.getString("entree_desc"), result.getString("entree_size"), result.getString("combo_price")));

               // add individual items that make up combo at zero cost
                //id desc size price
                transListView.getItems().add(String.format("%-20s%-20s%-20s%-20s\n", result.getString("entree_id"), result.getString("entree_desc"), result.getString("entree_size"), "0.00"));
                transListView.getItems().add(String.format("%-20s%-20s%-20s%-20s\n", result.getString("fries_id"), result.getString("fries_desc"), result.getString("fries_size"), "0.00"));
                transListView.getItems().add(String.format("%-20s%-20s%-20s%-20s\n", result.getString("drink_id"), result.getString("drink_desc"), result.getString("drink_size"), "0.00"));
            }

            showTotal();

            comboSelectionWindow.close();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error: " + e, "Connection Failed", JOptionPane.OK_OPTION);

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
