package application;
	
import java.io.IOException;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import notification.DailyJob;

public class Main extends Application {
    
	double xOffset;
	double yOffset;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("../ui/login.fxml"));
		Scene scene = new Scene(root,600,400);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});
	}	
	
	public static void main(String[] args) {
		/*
		try {

			JobDetail dailyJob = JobBuilder.newJob(DailyJob.class).withIdentity("job1", "goup1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule("0 19 17 03 04 ? 2021")).build();//0/30 * * * * ?
			
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();//new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(dailyJob, trigger);

			Thread.sleep(10000);
			//scheduler.shutdown();

		} catch (SchedulerException | InterruptedException e) {
			e.printStackTrace();
		}
		*/
		// Utils4J.addToClasspath("file:///"+System.getProperty("java.home")+ File.separator+"lib"+File.separator+"jfxrt.jar");
		launch(args);
	}
}
