package controllers;

import play.db.jpa.NoTransaction;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
    
    public static void test() {
        renderJSON("test");
    }
    
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                this.cancel();
                System.out.println(new Date());
            }
        };
        timer.schedule(task, new Date(), 1000);
    }
}