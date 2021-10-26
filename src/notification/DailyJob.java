package notification;

import java.util.Date;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.spi.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DailyJob implements Job{

    //private static final Logger logger = LogManager.getLogger(DailyJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //logger.info("info msg");
        System.out.println("Daily job "+ new Date());
//        EmailUtil.sendEmail("www.rawnl97@gmail.com", "Lappins El benna", "This is a scheduled email ." );
    }

}
