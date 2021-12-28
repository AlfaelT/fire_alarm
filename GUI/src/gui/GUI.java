/*
 * ITI INTAKE 42 EMBEDDED SYSTEM 
 * JAVA COURSE 
 * GROUP 8
 * STUDENTS ARE 1-Michael Safwat Sobhy Nakhla --> "LEADER"
 *              2-Abdelrahman Mahmoud Mohamed Saleh
 *              3-Abdelrahman Omar Mohamed Shafik
 *              4-Mostafa Hani Imam
 *              5-mohamed maged abdrabuh 
 */

package gui;

import com.fazecast.jSerialComm.SerialPort;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.FlatSkin;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {

    // The whole comming are for the application Scene
    Scene app_Scene;                        // Application Scene
    Gauge tempG = new Gauge();              // Gauge for Temperature
    Gauge humidG = new Gauge();             // Gauge for Humidity
    VBox vbox = new VBox();                 // To handle the Application Scene
    Label label;                            // Label for the name of the Arduino name and port                      
    HBox hGauge = new HBox();               // For Humidity Gauge 
    HBox bButtons = new HBox();             // For the four buttons in the Application Scene
    
    //The four buttons in the Applicatioin Scene
    Button test = new Button("Test");       // To test the whole System     
    Button stop = new Button("Stop");       // To stop the led and buzzer after a high temperature is detected 
    Button log = new Button("Log");         // To open the Log Scene and see the Log data
    Button start = new Button("Start");     // To start the system again after the stop is pressed 
    
    // Used serial port connection
    static SerialPort chosenPort;
    
    int alertFlag = 0;                      // Used as a flag to check if the temperature is higher than 26 C
    
    // The Whole comming is for the Log Scene
    Scene log_Scene;

    Label log_Label_DataT;                  // For Temperature data    
    Label log_Label_DataH;                  // For Humidity data
    Label log_Label_Time;                   // For Time data
    Button log_Ok;                          // To return to the Application Scene
    HBox log_HBox;                          // To handle the labels 
    HBox log_Data_HBox;                     // To handle the Data Areas 
    VBox log_VBox;                          // To handle the Log Scene
    TextArea log_AreaT;                     // For Temperature data
    TextArea log_AreaH;                     // For Humidity data
    TextArea log_AreaTD;                    // For Time data
    
    Group root2;                            // To handle the background image
    Comm comm;                              // Comm object to start connection
    

    @Override
    public void init() {
        
        comm = new Comm();  // Start the communication
        
        label = new Label(" Ardunio Nano(" + comm.comPort + ")");       //Arduino name and connection port
        
        settempGauge(tempG);                                            // Set Temperature Gauge
        sethumidGauge(humidG);                                          // Set Humidity Gauge
        hGauge = new HBox(200, tempG, humidG);                          // Add gauges to Hbox
        hGauge.setAlignment(Pos.CENTER);                                // Center the gauge
        bButtons = new HBox(100, start, test, stop, log);               // Add four buttons to Hbox
        bButtons.setAlignment(Pos.CENTER);                              // Center the buttons
        label.setTextFill(Color.WHITE);                                 // Make the text white 
        label.setFont(Font.font(26));                                   // Make the label font 26 
        vbox.getChildren().add(bButtons);                               // Add buttons to Vbox
        vbox.getChildren().add(label);                                  // Add label to Vbox
        test.setId("test");                                             // Setid for the CSS file
        stop.setId("stop");                                             // Setid for the CSS file
        log.setId("log");                                               // Setid for the CSS file
        start.setId("start");                                           // Setid for the CSS file

        
        //The whole comming are for the log
        Font font = Font.font("Verdana", FontWeight.BOLD, 18);

        log_Label_DataT = new Label("          Temperature");
        log_Label_DataT.setFont(font);
        log_Label_DataH = new Label("         Humidity");
        log_Label_DataH.setFont(font);
        log_Label_Time = new Label("           Time");
        log_Label_Time.setFont(font);
        log_Ok = new Button("Ok");              
        log_HBox = new HBox(log_Label_Time, log_Label_DataT, log_Label_DataH); // add labels

        log_VBox = new VBox();                
        
        // Adjust Text area for Temperature 
        log_AreaT = new TextArea();
        log_AreaT.setPrefHeight(400);
        log_AreaT.setPrefWidth(300);
        log_AreaT.setEditable(false);

        // Adjust Text area for Humidity
        log_AreaH = new TextArea();
        log_AreaH.setPrefHeight(400);
        log_AreaH.setPrefWidth(300);
        log_AreaH.setEditable(false);

        // Adjust Text area for Time 
        log_AreaTD = new TextArea();
        log_AreaTD.setPrefHeight(400);
        log_AreaTD.setPrefWidth(300);
        log_AreaTD.setEditable(false);

        log_Data_HBox = new HBox();

        log_Data_HBox.getChildren().addAll(log_AreaTD, log_AreaT, log_AreaH); // add areas

        log_VBox.getChildren().addAll(log_HBox, log_Data_HBox, log_Ok); // add all parts in log Scene

    }

    // Print the Temp, Hum, and Time to the areas 
    public void print_Log_Data() {
        int i;
        log_AreaT.clear();
        log_AreaH.clear();
        log_AreaTD.clear();
        for (i = 0; i < comm.get_Counter_Log(); i++) {
            log_AreaTD.appendText(comm.time.get(i) + "\n");
            log_AreaT.appendText(comm.data_T.get(i) + "\n");
            log_AreaH.appendText(comm.data_H.get(i) + "\n");
        }
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        StackPane root = new StackPane();
        
        /* Set Scaling for The Buttons */
        test.setScaleX(1.25);
        stop.setScaleX(1.25);
        log.setScaleX(1.25);
        start.setScaleX(1.25);

        /* Chnging Color of the Buttons */
        // test.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        test.setTextFill(Color.WHITE);

        // stop.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        stop.setTextFill(Color.WHITE);

        // log.setStyle("-fx-background-color:grey; -fx-border-color: black;");
        log.setTextFill(Color.WHITE);

        /* Pressing Escape will affect The stop Button */
        stop.setCancelButton(true);

        /* Pressing Enter Will affect the test Button */
        test.setDefaultButton(true);

        BorderPane Pane = new BorderPane();

        // Read an image to be used as a background for Applicarion Scene
        Image image = new Image(new FileInputStream("1.jpg"));
        ImageView imageView = new ImageView(image);
        root2 = new Group(imageView);
        
        imageView.setFitHeight(1000);
        imageView.setFitWidth(1500);

        // The handling of Test key: just send "1" to Arduino to make it start led and buzzer
        test.setOnAction(e -> {
            comm.send("1");
        });

        // The handling of Stop key: just send "2" to Arduino to make it stop led and buzzer
        stop.setOnAction(e -> {
            comm.send("2");
        });

        // The handling of Start key: just send "3" to Arduino to make it start its funtion again
        start.setOnAction(e -> {
            comm.send("3");
            alertMethod();
        });

        // The handling of Log key: Change the Scene from Applocation to Log
        // And add the data to the areas
        log.setOnAction(e -> {
            primaryStage.setScene(log_Scene);
            print_Log_Data();
        });

        // The handling of Log key: Change the Scene from Log to Applocation
        log_Ok.setOnAction(e -> {
            primaryStage.setScene(app_Scene);
        });

        comm.recieve();             // Start recieving from Arduino Thread

        alertMethod();              // Start the alert Thread 
        
        // Start a Thread to update the Gauges values
        new Thread(() -> {
            while (true) {
                tempG.setValue(comm.temper);
                humidG.setValue(comm.humid);
            }
        }).start();


        Pane.setBottom(vbox);
        Pane.setCenter(hGauge);
        root.getChildren().add(root2);
        root.getChildren().add(Pane);

        app_Scene = new Scene(root, 1500, 600, Color.WHITE);
        log_Scene = new Scene(log_VBox, 600, 600);
        app_Scene.getStylesheets().add(getClass().getResource("CSS.css").toString());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("FireAlarm");
        primaryStage.setScene(app_Scene);
        primaryStage.show();
        
    }
    // Set the Temperature gauge data
    public void settempGauge(Gauge gauge) {
        gauge.setSkin(new FlatSkin(gauge));
        gauge.setDecimals(0);
        gauge.setValueColor(Color.WHITE);
        gauge.setTitleColor(Color.WHITE);
        gauge.setSubTitleColor(Color.WHITE);
        gauge.setScaleX(1);
        gauge.setScaleY(1);
        gauge.setGradientBarEnabled(true);
        gauge.setTitle("Temprature");
        gauge.setSubTitle("°C");
        gauge.setUnitColor(Color.WHITE);
        gauge.setUnit("°C");
        gauge.setValue(0);
    }
    // Set the Humidity gauge data
    public void sethumidGauge(Gauge gauge) {
        gauge.setSkin(new FlatSkin(gauge));
        gauge.setDecimals(0);
        gauge.setValueColor(Color.WHITE);
        gauge.setTitleColor(Color.WHITE);
        gauge.setSubTitleColor(Color.WHITE);
        gauge.setScaleX(1);
        gauge.setScaleY(1);
        gauge.setGradientBarEnabled(true);
        gauge.setTitle("Humidity");
        gauge.setSubTitle("%rh");
        gauge.setUnitColor(Color.WHITE);
        gauge.setUnit("%rh");
        // gauge.setBarColor(Color.BLUE);
        gauge.setValue(0);
    }

    // Alert Thread appears when Temp >= 26
    public void alertMethod() {
        new Thread(() -> {
            while (alertFlag == 0) {
                
                if (comm.temper >= 26) {
                    alertFlag = 1;
                }
            }
            if (alertFlag == 1) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Temprature Warning !");
                    alert.setContentText("Temprature is above or equal 26 °C");
                    alert.show();
                });
                alertFlag =0;
            }
        }).start();
    }

}