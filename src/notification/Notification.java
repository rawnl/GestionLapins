package notification;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Notification {

    // periodically search for notifications 
    public void searchNotification(){
        // notify when there's any

    }
    // To send email at a specific date
    public static void NotifyJob(){
        try {

            JobDetail dailyJob = JobBuilder.newJob(DailyJob.class).withIdentity("job1","goup1").build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")).build();
            
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(dailyJob, trigger);
    
            Thread.sleep(10000);
            scheduler.shutdown();
            
        } catch (SchedulerException | InterruptedException e) {
            e.printStackTrace();
        }        
    }
/*
    public static void main (String [] args){
        NotifyJob();
    }
    */

}
