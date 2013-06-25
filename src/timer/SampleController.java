
package timer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.util.Duration;



public class SampleController implements Initializable {
    @FXML
    private TextField maxTimeField;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;
    @FXML
    public Label timerLabel;
    @FXML
    private MenuItem closeButton;
    @FXML
    private TextField fromField;
    @FXML
    private TextField toField;
    
    /********** global variables **********/
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    
    private boolean keep_going = true;   
    Timeline timeline;
    private int fromInterval;
    private int toInterval;
    private int[] interval;
    int nextSeconds;
    int nowSeconds;
    
    /**************************************/
        
    /************ user methods *************/
    
    /**
     * reduces displayed hour count by one
     * @return 
     */
    private boolean reduceHour(){
        if(hour > 0){
            hour = hour - 1;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * reduces displayed minute count by one
     * @return 
     */
    private boolean reduceMinute(){
        if (minute == 0){
            if(reduceHour()){
                minute = 59;
                return true;
            } else {
                return false;
            }
        } else {
            minute = minute - 1;
            return true;
        }
    }
    
    /**
     * reduces global second count by one
     */
    public boolean reduceSecond(){
        if(hour == 0 & minute == 0 & second == 0){
            keep_going = false;
            return false;
        }
        if(second == 0){
            if(reduceMinute()){
                second = 59;                 
            } 
        } else {
            second = second - 1;            
        }    
        displayTime();
        return true;
    }
    
    /**
     * displays the wanted time on the display
     * @param hours    
     * @param minutes
     * @param seconds 
     */
    public void displayTime(){
        String h;
        String m;
        String s;
        
        if(hour < 10){
            h = "0" + hour;
        } else {
            h = String.valueOf(hour);
        }
        
        if(minute < 10){
            m = "0" + minute;
        } else {
            m = String.valueOf(minute);
        }
        
        if(second < 10){
            s = "0" + second;
        } else {
            s = String.valueOf(second);
        }
        
        String time = h + ":" + m + ":" + s;
        
        this.timerLabel.setText(time);
    }
    
    
    /***************************************/
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayTime();        
    }    

    @FXML
    private void startButtonFired(ActionEvent event) {
        
        // displaying max time
        hour = Integer.parseInt(this.maxTimeField.getText()) / 60;
        minute = Integer.parseInt(this.maxTimeField.getText()) % 60;
        second = 0;        
        displayTime();
        
        int count = second + (60 * minute) + (3600 * hour);
        nextSeconds = count; // copy for later use
        nowSeconds = count;
        
        // creating the array of possible values
        fromInterval = Integer.parseInt(this.fromField.getText());
        toInterval = Integer.parseInt(this.toField.getText());
        int amount = toInterval - fromInterval + 1;
        interval = new int[amount];
        int j = 0;
        for(int i = fromInterval; i <= toInterval; i++) {
            interval[j] = i;
            System.out.println(interval[j]);
            j++;
        }
               
        // countdown as timeline
        timeline = new Timeline( new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // if equal --> get next random number and substract it
                if(nextSeconds == nowSeconds) {
                    int nextTone = getNextTimeValue();
                    nextSeconds = nextSeconds - nextTone; 
                }  
                
                // reduce one second
                reduceSecond();                 
                nowSeconds = nowSeconds - 1; // nowSeconds is the actual second count left
                
                // if after substracting they are equal
                // -> play a beep sound
                if(nextSeconds == nowSeconds) {
                    Beep b = Beep.getInstance();
                    b.beep();  
                }                
            }
        }));
        
        // starting the timeline        
        timeline.setCycleCount(count);
        timeline.play();        
    }

    /**
     * @return  the random value in the array of possible numbers
     */
    private int getNextTimeValue() {
        int random = (int) (Math.random() * ((toInterval + 1) - fromInterval) + fromInterval);
        int indexRandom = random % interval.length; // get index for array                
        int nextTone = interval[indexRandom]; // get random value from interval
        
        return nextTone;
    }
    
    @FXML
    private void pauseButtonFired(ActionEvent event) {
        if( pauseButton.getText().equals("Pause") ) {
            timeline.pause();
            pauseButton.setText("Resume");
        } else if ( pauseButton.getText().equals("Resume") ) {
            timeline.play();
            pauseButton.setText("Pause");
        }
        
    }

    @FXML
    private void resetButtonFired(ActionEvent event) {
        hour = 0;
        minute = 0;
        second = 0;
        displayTime();
        
        timeline.stop();
        pauseButton.setText("Pause");
    }

    @FXML
    private void closeButtonFired(ActionEvent event) {
        System.exit(0);
    }
    
    
}
