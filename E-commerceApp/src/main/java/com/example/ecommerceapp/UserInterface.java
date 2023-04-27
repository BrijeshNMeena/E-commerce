package com.example.ecommerceapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;
    VBox body;
    Button signInButton;
    Button homeButton;
    Label welcomeLabel;
    ProductList productList = new ProductList();
    VBox productPage;
    Customer loggedInCustomer;
    ObservableList<Product> itemsInCart = FXCollections.observableArrayList();
    Button placeOrderButton = new Button("Place Order");

    public BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(500, 500);
        //root.getChildren().add(loginPage);
        //root.setCenter(loginPage);
        body = new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        productPage = productList.getAllProducts();
        root.setCenter(body);
        body.getChildren().add(productPage);
        root.setTop(headerBar);
        root.setBottom(footerBar);
        return root;
    }

    public UserInterface() {
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    private void createLoginPage() {
        Text usernameText = new Text("User Name");
        Text passwaordText = new Text("Password");

        TextField username = new TextField();
        username.setPromptText("Enter your user name");
        username.setText("brijesh@gmail.com");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");
        password.setText("123zxc");

        Label messageLabel = new Label("Hi");

        Button loginButton = new Button("Login");


        loginPage = new GridPane();
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);

        loginPage.add(usernameText, 0, 0);
        loginPage.add(username, 1, 0);
        loginPage.add(passwaordText, 0, 1);
        loginPage.add(password, 1, 1);
        loginPage.add(loginButton, 1, 2);
        loginPage.add(messageLabel, 0, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = username.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name, pass);
                if (loggedInCustomer != null) {
                    messageLabel.setText("welcome " + loggedInCustomer.getName());
                    welcomeLabel = new Label("Welcome " + loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                } else {
                    messageLabel.setText("Login Failed !! please give correct username and password");
                }
            }
        });

    }

    private void createHeaderBar() {
        headerBar = new HBox();

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setPrefWidth(180);
        Button searchButton = new Button("Search");
        Button cartButton = new Button("Cart");
        Button homeButton = new Button("Home");

        signInButton = new Button("Sign In");

        headerBar.setAlignment(Pos.CENTER);
        headerBar.setPadding(new Insets(20));
        headerBar.setSpacing(15);
        headerBar.setStyle("-fx-background-color: gray");
        headerBar.getChildren().addAll(homeButton, searchBar, searchButton, signInButton, cartButton);

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                productPage = productList.getSearchedProducts(searchBar.getText());
                body.getChildren().add(productPage);
            }
        });
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                productPage = productList.getAllProducts();
                body.getChildren().add(productPage);
            }
        });
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headerBar.getChildren().remove(signInButton);
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage = productList.getProductsFromCart(itemsInCart);
                prodPage.getChildren().add(placeOrderButton);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                body.getChildren().add(prodPage);
                //footerBar.setVisible(false);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(itemsInCart == null) {
                    showDialog("Please add products in the cart first to place order");
                    return;
                }
                if(loggedInCustomer == null){
                    showDialog("Please login first to place order");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer, itemsInCart);
                if(count != 0) {
                    showDialog("Order for " + count + " Products placed successfully!");
                    itemsInCart.clear();
                }
                else
                    showDialog("Order Failed");
            }
        });

    }

    private void createFooterBar() {
        footerBar = new HBox();

        Button buyNow = new Button("Buy Now");
        Button addToCartButton = new Button("Add To Cart");
        buyNow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product == null) {
                    showDialog("Please select a product first to place order");
                    return;
                }
                if(loggedInCustomer == null){
                    showDialog("Please login first to place order");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer, product);
                if(status)
                    showDialog("Order placed successfully!");
                else
                    showDialog("Order Failed");
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product == null) {
                    showDialog("Please select a product first to add it to the cart");
                    return;
                }
                itemsInCart.add(product);
                showDialog("Selected item has been added to the cart successfully!");
            }
        });

        footerBar.setAlignment(Pos.CENTER);
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.getChildren().addAll(buyNow, addToCartButton);

    }

    private void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
